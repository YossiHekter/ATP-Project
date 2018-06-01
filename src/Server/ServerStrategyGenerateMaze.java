package Server;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.util.stream.Stream;

public class ServerStrategyGenerateMaze implements IServerStrategy {

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
            byte[] compressedMaze = f.readAllBytes();
            f.close();
            toClient.writeObject(compressedMaze);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private byte[] generatorMazeForClient(ObjectInputStream fromClient, Server.Configurations configurations) {
        try {
            int[] input = (int[]) fromClient.readObject();
            Maze maze = configurations.getMazeGenerator().generate(input[0], input[1]);
            return maze.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

}
