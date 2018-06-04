package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private Map<String,String> mazeOnDiskMap;

    /**
     * This function handles a client request to solver a maze
     * @param inFromClient The stream from the client to the server
     * @param outToClient The stream from the server to the client
     * @param configurations Properties file
     */
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient,  Server.Configurations configurations){
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            String tempDirectoryPath = System.getProperty("java.io.tmpdir");
            tempDirectoryPath = tempDirectoryPath + "\\maze_sol";
            readMazeOnDisk(tempDirectoryPath);

            Maze mazeFromClient = (Maze)fromClient.readObject();
            byte[] mazeBytesFromClient = mazeFromClient.toByteArray();

            String address = findMazeOnDisk(mazeBytesFromClient);

            if(address != ""){
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(mazeOnDiskMap.get(address)));
                Solution solution = (Solution) in.readObject();
                toClient.writeObject(solution);
            }
            else {

                int mazeNum = getMazeNum(tempDirectoryPath);
                mazeOnDiskMap.put(tempDirectoryPath + "\\m" + mazeNum + ".maze", tempDirectoryPath + "\\s" + mazeNum + ".solution");

                writeNewFileToDisk(tempDirectoryPath + "\\mazeMap.map", mazeOnDiskMap);

                writeNewFileToDisk(tempDirectoryPath + "\\m" + mazeNum + ".maze", mazeFromClient);

                Solution solution = getMazeSolution(mazeFromClient, configurations);

                writeNewFileToDisk(tempDirectoryPath + "\\s" + mazeNum + ".solution", solution);

                toClient.writeObject(solution);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function solves the maze and returns the solution
     * @param mazeFromClient The maze from the client
     * @param configurations Properties file
     * @return The solution of the maze
     */
    private Solution getMazeSolution(Maze mazeFromClient,  Server.Configurations configurations) {
        SearchableMaze searchableMaze = new SearchableMaze(mazeFromClient);
        Solution solution = configurations.getSolvingAlgorithm().solve(searchableMaze);
        return solution;
    }

    /**
     * This function saves a new file to disk
     * @param path The path where we will save the new file
     * @param objectToWrite The object to save
     */
    private void writeNewFileToDisk(String path, Object objectToWrite) {
        try {
            File file = new File(path);
            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(output);
            objectOutput.writeObject(objectToWrite);
            objectOutput.flush();
            objectOutput.close();
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function returns the number of the next maze to be saved on the disk
     * @param tempDirectoryPath The path where the mazes are kept
     * @return
     */
    private int getMazeNum(String tempDirectoryPath) {
        int mazeNum = 0;
        while (mazeOnDiskMap.containsKey(tempDirectoryPath + "\\m" + mazeNum + ".maze"))
            mazeNum++;
        return mazeNum;
    }

    /**
     * This function looks for whether the maze we received was previously resolved by the server
     * If the maze is resolved in the past, we will return the path to the solution
     * @param mazeBytesFromClient The maze from the client
     * @return path to the solution or empty string
     */
    private String findMazeOnDisk(byte[] mazeBytesFromClient) {
        String ans="";

        try{
        for(String mazeAddrees : mazeOnDiskMap.keySet()){
            String addrees = mazeAddrees;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(mazeAddrees));
            Maze fromDisk = (Maze) in.readObject();
            byte[] savedMazeBytes = fromDisk.toByteArray();
            in.close();
            if(Arrays.equals(mazeBytesFromClient, savedMazeBytes)) {
                ans = addrees;
                break;
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * This function reads the map that preserves all their mazes and solutions
     * If the map does not exist, it creates a new map
     * @param tempDirectoryPath The path where the map are kept
     */
    private void readMazeOnDisk(String tempDirectoryPath) {
        try {
            new File(tempDirectoryPath).mkdir();
            File f = new File(tempDirectoryPath + "\\mazeMap.map");
            if (f.exists()) {
                FileInputStream in = new FileInputStream(f);
                ObjectInputStream map = new ObjectInputStream(in);
                mazeOnDiskMap = (Map<String, String>) map.readObject();
            } else {
                mazeOnDiskMap = new HashMap<>();
                FileOutputStream out = new FileOutputStream(f);
                ObjectOutputStream map = new ObjectOutputStream(out);
                map.writeObject(mazeOnDiskMap);
                map.flush();
                map.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
