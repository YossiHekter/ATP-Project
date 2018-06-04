package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class read a compressed maze from a file and decompress it
 * @author Roee Sanker & Yossi Hekter
 */
public class MyDecompressorInputStream extends InputStream {

    /*
     *the stream to read from
     * */
    private InputStream in;

    /**
     * Constructor
     * @param in The new input stream
     */
    public MyDecompressorInputStream(InputStream in){this.in = in;}

    /**
     * This function read a integer in the input stream
     */

    public int read() {
        return 0;
    }

    /**
     * This function read a byte array from the input stream and decompress it
     * @param input The array to decompress
     * @return '-1' if fail or '0' if success
     */

    public int read(byte[] input) {
        try {
            ArrayList<Byte> arrTmp = new ArrayList<>();
            while (in.available() > 0)
                arrTmp.add((byte) in.read());
            byte[] arr = new byte[arrTmp.size()];
            for(int i=0;i<arrTmp.size();i++){
                arr[i] = arrTmp.get(i);
            }
            //byte[] arr = in.readAllBytes();
            decompressMaze(input, arr);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This function decompressed the array into the 'decompressed' array
     * @param decompress The array to decompress byte array
     * @param compress The compress byte array
     */
    private void decompressMaze(byte[] decompress, byte[] compress) {
        int index = 0;
        int i = 0;
        int counter = 0;

        //copies the param (row, column, start and goal position) from the compress array to the decompress array
        while (counter < 4) {
            if (compress[i] == 0) {
                counter++;
            }
            decompress[index] = compress[i];
            i++;
            index++;
        }
        //add the frame of the maze's matrix to the decompressed byte array
        index = addLimit(decompress, compress, index);

        //decompress the data from the compress array into the decompressed array
        for(;i<compress.length - 1;i++){
            byte b = compress[i];
            index = addData(decompress, index, b);
        }

        //the final byte in the compress array
        byte b = compress[i];
        int bin = b;
        if(bin < 0)
            bin = bin + 256;
        String bineryToWrite = Integer.toBinaryString(bin);
        bineryToWrite = addZeroToBinery(bineryToWrite);
        i = index;
        int size = 0;
        while (i < decompress.length){
            if (decompress[i] == 0)
                size++;
            i++;
        }
        int j = 8 - size;
        for(;j<8;j++) {
            index = nextFreeSpace(decompress, index);
            char x = bineryToWrite.charAt(j);
            if(x == '0')
                decompress[index] = 0;
            else
                decompress[index] = 1;
            index++;
        }
        addStartGoalPos(decompress, compress);
    }

    /**
     * This function decompress the data from the byte that represent the 8 byte to the
     * real 8 bytes
     * @param decompress The decompressed byte array
     * @param index The current index unthe array to write
     * @param b The byte to to write
     * @return The new index
     */
    private int addData(byte[] decompress, int index, byte b) {
        int bin = b;
        if(bin < 0)
            bin = bin + 256;
        String bineryToWrite = Integer.toBinaryString(bin);
        bineryToWrite = addZeroToBinery(bineryToWrite);
        for(int i=0;i<bineryToWrite.length();i++){
            index = nextFreeSpace(decompress, index);
            char x = bineryToWrite.charAt(i);
            if(x == '0')
                decompress[index] = 0;
            else
                decompress[index] = 1;
            index++;
        }
        return index;
    }

    /**
     * This function add the a zero to the start of the string
     * @param bineryTowrite The string to add the zero
     * @return The string wight zero at the start
     */
    private String addZeroToBinery(String bineryTowrite) {
        for(int i=bineryTowrite.length();i<8;i++)
            bineryTowrite = "0"+bineryTowrite;
        return bineryTowrite;
    }

    /**
     * This function add the start and goal position in the decompress array
     * @param compress The byte array without the start and goal position
     * @param decompress The byte array with the start and goal position
     */
    private void addStartGoalPos(byte[] decompress, byte[] compress) {
        int rowSize = getData(compress, 1);
        int startRow = getData(compress, 2);
        int goalRow = getData(compress, 3);
        int rowCount = getData(compress, 0)-1;
        int index = decompress.length - 1;
        for (; rowCount > 0; rowCount--, index--) {
            if(rowCount == goalRow)
                decompress[index] = 0;
            index -= rowSize - 1;
            if(rowCount == startRow)
                decompress[index] = 0;
        }
    }

    /**
     * This function found the next free space in the maze matrix
     * @param decompress The byte array with the frame
     * @param index The start index of the array
     * @return The next free space
     */
    private int nextFreeSpace(byte[] decompress, int index){
        while (index < decompress.length && decompress[index]==1)
            index++;
        return index;
    }

    /**
     * This function add the frame of the maze's matrix
     * @param compress The byte array without the frame
     * @param decompress The byte array with the frame
     * @param index The start index of the array
     * @return The new index
     */
    private int addLimit(byte[] decompress, byte[] compress, int index) {
        int rowSize = getData(compress, 1);
        int rowNum = getData(compress, 0);
        int rowCount = 0;
        for(int i=0;i<rowSize+1;i++, index++){
            decompress[index]=1;
        }

        int ans = index;
        rowCount++;
        for(;rowCount<rowNum-1;rowCount++, index+=2){
            index+=rowSize-2;
            decompress[index]=1;
            decompress[index+1]=1;
        }

        for (;index<decompress.length;index++)
            decompress[index] = 1;
        return ans;
    }

    /**
     * This function return the size of paramaters in the maze from the byte array
     * @param byteArray The byte to write
     * @param data The type of data to return
     * data=0 -> row, data=1 -> col, data=2 -> Start row, data=3 -> Goal row
     * @return The date that asked
     */
    private int getData(byte[] byteArray, int data){
        int ans = 0;
        int i = 0;
        int counter = 0;
        while (counter < data){
            if(byteArray[i] == 0)
                counter++;
            i++;
        }
        for(;byteArray[i]!=0;i++){
            if(byteArray[i] < 0)
                ans += byteArray[i] + 256;
            else
                ans += byteArray[i];
        }
        return ans;
    }
}
