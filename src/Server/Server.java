package Server;

import Server.IServerStrategy;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    public Configurations confi;
    public ThreadPoolExecutor threadPoolExecutor;

    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        this.confi = new Configurations();
        this.threadPoolExecutor= (ThreadPoolExecutor) Executors.newCachedThreadPool();
        threadPoolExecutor.setCorePoolSize(confi.getSizeOfThreadPool());
    }

    public void start() {
        new Thread(() -> {
            runServer();
        }).start();
    }

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

                    threadPoolExecutor.execute(() -> {
                        handleClient(clientSocket);
                    });

                } catch (SocketTimeoutException e) {

                }
            }
            server.close();
        } catch (IOException e) {

        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream(), confi);
            clientSocket.close();
        } catch (IOException e) {

        }
    }

    public void stop() {
        stop = true;
        try {
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Configurations {

        ISearchingAlgorithm solvingAlgorithm;
        int sizeOfThreadPool;
        IMazeGenerator mazeGenerator;



        public Configurations() {
            try {

                FileInputStream input = new FileInputStream("Resources\\config.properties");
                Properties prop = new Properties();
                prop.load(input);

                //init the solving algorithm
                String tmpString = prop.getProperty("solvingAlgorithm");
                System.out.println(tmpString);
                if (tmpString.equals("BestFirstSearch") )
                    solvingAlgorithm = new BestFirstSearch();
                else if (tmpString.equals("BreadthFirstSearch"))
                    solvingAlgorithm = new BreadthFirstSearch();
                else
                    solvingAlgorithm = new DepthFirstSearch();

                //init the size of the thread pool
                sizeOfThreadPool = Integer.parseInt(prop.getProperty("sizeOfThreadPool"));

                //init the maze generetor
                tmpString = prop.getProperty("mazeGenerator");
                if (tmpString.equals("MyMazeGenerator"))
                    mazeGenerator = new MyMazeGenerator();

            } catch (IOException io) {
                io.printStackTrace();
            }
        }


        public ISearchingAlgorithm getSolvingAlgorithm() {
            return solvingAlgorithm;
        }

        public int getSizeOfThreadPool() {
            return sizeOfThreadPool;
        }

        public IMazeGenerator getMazeGenerator() {
            return mazeGenerator;
        }

    }
}
