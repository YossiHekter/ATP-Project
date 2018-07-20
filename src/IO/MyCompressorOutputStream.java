package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class compress the maze to a byte array and write it into a file
 * @author Yossi Hekter
 */
public class MyCompressorOutputStream extends OutputStream {
    /**
     * The stream to write to
     */
    private OutputStream out;

    /**
     * Constructor
     * @param out The new output stream
     */
    public MyCompressorOutputStream(OutputStream out){
        this.out = out;
    }

    /**
     * This function write a integer in the output stream
     * @param num The integer to write
     */

    public void write(int num){
        while (num > 253){
            writeByte((byte)253);
            writeByte((byte)0);
            num-=253;
        }
        if(num>0)
            writeByte((byte)num);
    }

    /**
     * This function write a byte array in the output stream
     * @param byteArray The byte array to write
     */
    public void write(byte[] byteArray) {
        int counter = 0;
        int index = 0;
        while (counter < 4) {
            writeByte(byteArray[index]);
            if (byteArray[index] == 0)
                counter++;
            index++;
        }

        //compress the regular bute array to a compressed butr array
        byte[] compreesArray = compressArray(byteArray,index);
        for(index = 0;index<compreesArray.length;index++)
            index = getBinary(compreesArray, index);

    }

    /**
     * This function do the compress proses by taking the first 8 bytes from the array
     * and return the integer that represented in binary by them
     * @param byteArray The byte array
     * @param index The index in the array to take from
     * @return int that represented the binary number of the 8 byte
     */
    private int getBinary(byte[] byteArray, int index){
        int res;
        if(index + 7 < byteArray.length ) {
            String binery = "";
            int size = index + 8;
            for(;index<size;index++){
                binery = binery+byteArray[index];
            }
            //int toWrite = Integer.parseInt(binery, 2);
            int toWrite = binaryToInt(binery);
            writeByte((byte) toWrite);
            res = index - 1;
        }

        //if there is'nt 8 more byte in the array
        else{
            String binery = "";
            for(;index < byteArray.length;index++){
                binery = binery+byteArray[index];
            }
            //int toWrite = Integer.parseInt(binery, 2);
            int toWrite = binaryToInt(binery);
            writeByte((byte) toWrite);
            res = index + 1;
        }
        return res;
    }

    private int binaryToInt(String binery) {
        int ans = 0;
        int pow = 0;
        for(int i=binery.length()-1;i>=0;i--){
            if(binery.charAt(i) == '1'){
                ans += Math.pow(2, pow);
            }
            pow++;
        }

        return ans;
    }

    /**
     * This function do also a compress proses by taking off the frame of the maze's matrix
     * @param byteArray The byte array
     * @param start The start index of the array
     * @return The compressed byte array
     */
    private byte[] compressArray(byte[] byteArray, int start){
        int rowSize = getData(byteArray, 1);
        int rowNum = getData(byteArray, 0);

        byte[] ans = new byte[byteArray.length-2*rowSize-2*rowNum-start+4];
        start += rowSize+1;
        int counter = 0;
        for (int i=0;i<ans.length;i++, counter++, start++){
            if(counter == rowSize-2){
                start+=2;
                counter=0;
            }
            ans[i]=byteArray[start];
        }
        return ans;
    }

    /**
     * This function write a byte in the output stream
     * @param b The byte to write
     */
    private void writeByte(byte b){
        try{
            out.write(b);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
