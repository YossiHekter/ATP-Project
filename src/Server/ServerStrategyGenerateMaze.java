package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;
import java.util.ArrayList;

/**
 * This class represents a server that produces mazes
 * @author Roee Sanker & Yossi Hekter
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {

    /**
     * This function handles a client request to create a maze the size the customer gives
     * @param inFromClient The stream from the client to the server
     * @param outToClient The stream from the server to the client
     * @param configurations Properties file
     */
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient, Server.Configurations configurations){
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            byte[] mazeForClient = generatorMazeForClient(fromClient, configurations);
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(tempDirectoryPath + "\\tmp.maze"));
            out.write(mazeForClient);
            out.flush();
            out.close();

            FileInputStream f = new FileInputStream(tempDirectoryPath + "\\tmp.maze");

            ArrayList<Byte> arrTmp = new ArrayList<>();
            while (f.available() > 0)
                arrTmp.add((byte) f.read());
            byte[] compressedMaze = new byte[arrTmp.size()];
            for(int i=0;i<arrTmp.size();i++){
                compressedMaze[i] = arrTmp.get(i);
            }

            f.close();
            toClient.writeObject(compressedMaze);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This function creates a maze and returns the maze as an array of byte
     * @param fromClient The input stream from the client
     * @param configurations Properties file
     * @return
     */
    private byte[] generatorMazeForClient(ObjectInputStream fromClient, Server.Configurations configurations) {
        try {
            Object o = fromClient.readObject();
            int[] input;
            if(o instanceof int[]){
                input = (int[]) o;
            }
            else {
                input = new int[2];
                input[0] = 10;
                input[1] = 10;
            }
            //int[] input = (int[]) fromClient.readObject();
            Maze maze = configurations.getMazeGenerator().generate(input[0], input[1]);
            return maze.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Maze maze = configurations.getMazeGenerator().generate(10, 10);
        return maze.toByteArray();
    }

}
