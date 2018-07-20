package Server;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class represents an abstract server
 * @author Yossi Hekter
 */
public class Server {
    /**
     * The server port
     */
    private int port;
    /**
     * The time the server will wait for a client request
     */
    private int listeningInterval;
    /**
     * The server strategy
     */
    private IServerStrategy serverStrategy;
    /**
     * True if the server is stop run
     */
    private volatile boolean stop;
    /**
     * Configurations class have all properties
     */
    private Configurations config;
    /**
     * A tool that schedules all threads on a server
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * Constructor
     * @param port The server port number
     * @param listeningInterval The time the server will wait for a client request
     * @param serverStrategy The server strategy
     */
    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.config = new Configurations();
        this.threadPoolExecutor= (ThreadPoolExecutor) Executors.newCachedThreadPool();
        threadPoolExecutor.setCorePoolSize(config.getSizeOfThreadPool());
    }

    /**
     * This function starts the server
     */
    public void start() {
        new Thread(() -> {
            runServer();
        }).start();
    }

    /**
     * This function defines the server job
     * Receiving a request from the client and returning a reply according to the server's strategy
     */
    private void runServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(listeningInterval);

            while (!stop) {
                try {
                    Socket clientSocket = server.accept(); // blocking call
/*
                    new Thread(() -> {
                        handleClient(clientSocket);
                    }).start();
*/
                    threadPoolExecutor.submit(new Thread(() -> {
                                handleClient(clientSocket);
                            })
                    );

                } catch (SocketTimeoutException e) {
                    //e.printStackTrace();
                }
            }
            server.close();
            threadPoolExecutor.shutdown();
        } catch (Exception e) {

        }
    }

    /**
     * This function handles a client's request
     * @param clientSocket The streams of the client
     */
    private void handleClient(Socket clientSocket) {
        try {
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream(), config);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function stops the server job
     */
    public void stop() {
        stop = true;
        try {
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * A static class that holds the server properties
     */
    public static class Configurations {

        /**
         * The algorithm with which to solve the maze
         */
        ISearchingAlgorithm solvingAlgorithm;
        /**
         * The number of clients that can be used on the server simultaneously
         */
        int sizeOfThreadPool;
        /**
         * The algorithm with which to create the maze
         */
        IMazeGenerator mazeGenerator;

        /**
         * Constructor
         */
        public Configurations() {
            try {
                String filename = "config.properties";
                InputStream input2 = Server.class.getClassLoader().getResourceAsStream(filename);

                if(input2 == null){
                    solvingAlgorithm = new BestFirstSearch();
                    sizeOfThreadPool = 100;
                    mazeGenerator = new MyMazeGenerator();
                }
                else{
                    Properties prop = new Properties();
                    prop.load(input2);

                    //init the solving algorithm
                    String tmpString = prop.getProperty("solvingAlgorithm");
                    if (tmpString == null)
                        solvingAlgorithm = new BestFirstSearch();
                    else if (tmpString.equals("BestFirstSearch") )
                        solvingAlgorithm = new BestFirstSearch();
                    else if (tmpString.equals("BreadthFirstSearch"))
                        solvingAlgorithm = new BreadthFirstSearch();
                    else
                        solvingAlgorithm = new DepthFirstSearch();

                    //init the size of the thread pool
                    tmpString = prop.getProperty("sizeOfThreadPool");
                    if(tmpString != null) {
                        int size = Integer.parseInt(tmpString);
                        if(size>0)
                            sizeOfThreadPool = size;
                        else
                            sizeOfThreadPool = 100;
                    }
                    else
                        sizeOfThreadPool = 100;
                    //init the maze generetor
                    tmpString = prop.getProperty("mazeGenerator");
                    if (tmpString == null)
                        mazeGenerator = new MyMazeGenerator();
                    else if (tmpString.equals("MyMazeGenerator"))
                        mazeGenerator = new MyMazeGenerator();
                    else
                        mazeGenerator = new SimpleMazeGenerator();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        /**
         * This function returns the algorithm with which to create the maze
         * @return The algorithm with which to solve the maze
         */
        public ISearchingAlgorithm getSolvingAlgorithm() {
            return solvingAlgorithm;
        }

        /**
         *  This function returns the number of clients that can be used on the server simultaneously
         * @return The number of clients that can be used on the server simultaneously
         */
        public int getSizeOfThreadPool() {
            return sizeOfThreadPool;
        }

        /**
         *  This function returns the algorithm with which to create the maze
         * @return The algorithm with which to create the maze
         */
        public IMazeGenerator getMazeGenerator() {
            return mazeGenerator;
        }

    }
}
