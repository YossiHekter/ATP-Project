package Server;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private Map<String,String> mazeOnDiskMap;

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

    private Solution getMazeSolution(Maze mazeFromClient,  Server.Configurations configurations) {
        SearchableMaze searchableMaze = new SearchableMaze(mazeFromClient);
        Solution solution = configurations.getSolvingAlgorithm().solve(searchableMaze);
        return solution;
    }

    private void writeNewFileToDisk(String s, Object objectToWrite) {
        try {
            File file = new File(s);
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

    private int getMazeNum(String tempDirectoryPath) {
        int mazeNum = 0;
        while (mazeOnDiskMap.containsKey(tempDirectoryPath + "\\m" + mazeNum + ".maze"))
            mazeNum++;
        return mazeNum;
    }

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
