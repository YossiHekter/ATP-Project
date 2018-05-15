import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import java.io.*;


public class m {

    public static void main(String[] args) {


        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();

        Maze maze = mazeGenerator.generate(100, 100); //Generate new maze

        try {
// save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            byte[] b = maze.toByteArray();
            long s = System.currentTimeMillis();
            out.write(b);
            long end = System.currentTimeMillis();
            System.out.println(end-s);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


/*
        MyMazeGenerator m = new MyMazeGenerator();
        Maze maze1 = m.generate(1000,1000);
        byte[] b = maze1.toByteArray();
        System.out.println("b array size "+b.length);
        Maze maze2 = new Maze(b);
        sameMaze(maze1,maze2);
*/
    }

    public static void sameMaze(Maze m1, Maze m2){
        System.out.println("Start pos m1: "+m1.getStartPosition().toString());
        System.out.println("Start pos m2: "+m2.getStartPosition().toString());

        System.out.println("Goal pos m1 :"+m1.getGoalPosition().toString());
        System.out.println("Goal pos m2 :"+m2.getGoalPosition().toString());

        System.out.println("Rows m1: "+ m1.getMazeMatrix().length);
        System.out.println("Rows m2: "+ m2.getMazeMatrix().length);

        System.out.println("Col m1: "+ m1.getMazeMatrix()[0].length);
        System.out.println("Col m2: "+ m2.getMazeMatrix()[0].length);

        for (int i=0;i<m1.getMazeMatrix().length;i++){
            for (int j=0;j<m1.getMazeMatrix()[0].length;j++){
                if(m1.getMazeMatrix()[i][j]!=m2.getMazeMatrix()[i][j])
                    System.out.println("BugBug");
            }
        }
    }




}