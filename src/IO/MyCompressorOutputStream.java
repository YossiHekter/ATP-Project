package IO;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;

    public MyCompressorOutputStream(OutputStream out){
        this.out = out;
    }

    @Override
    public void write(int num){
        while (num > 253){
            writeByte((byte)253);
            writeByte((byte)0);
            num-=253;
        }
        if(num>0)
            writeByte((byte)num);
    }

    public void write(byte[] b){
        int counter = 0;
        int index = 0;
        while (counter < 4){
            writeByte(b[index]);
            if(b[index] == 0)
                counter++;
            index++;
        }
        int rowSize = getRowSize(b);
        byte[] com = compressArray(b,index);

        byte tmp = com[0];
        if(tmp==0)
            writeByte((byte)0);

        for(index = 0;index<com.length;index++){
            int i = specialCase(com, index);
            if(i!=-1){
                index = i;
                continue;
            }
            else {
                int secquence = countSecquence(com, index);
                index += secquence;
                write(secquence+1);
            }
        }
    }

    private int countSecquence(byte[] b, int index){
        byte cur = b[index];
        index++;
        int counter = 0;
        while (index<b.length && cur == b[index]){
            counter++;
            index++;
        }
        return counter;
    }

    private int specialCase(byte[] b, int index){
        int res = -1;
        if(index + 4 < b.length ) {
            if ((b[index] == 1 && b[index + 1] == 0 && b[index + 2] == 1 && b[index + 3] == 0)
                    || (b[index] == 0 && b[index + 1] == 1 && b[index + 2] == 0 && b[index + 3] == 1)) {//254 represent 010/101
                writeByte((byte) 254);
                res = index + 2;
            }
        }
        if(index + 3 < b.length && res == -1){
            if((b[index]==1 && b[index+1]==0 && b[index+2]==1)
                    || (b[index]==0 && b[index+1]==1 && b[index+2]==0)) {//255 represent 01/10
                writeByte((byte) 255);
                res = index+1;
            }
        }
        return res;
    }

    private byte[] compressArray(byte[] b, int start){
        int row = getRowSize(b);
        int col = getColumnSize(b);

        byte[] ans = new byte[b.length-2*row-2*col-start+4];
        start += row+1;
        int counter = 0;
        for (int i=0;i<ans.length;i++){
            if(counter == row-2){
                start+=2;
                counter=0;
            }
            ans[i]=b[start];
            counter++;
            start++;
        }
        return ans;
    }

    private void writeByte(byte b){
        try{
            //System.out.println("write byte: " + b);
            out.write(b);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getRowSize(byte[] b){
        int ans = 0;
        int i = 0;
        while (b[i] != 0){
            i++;
        }
        i++;
        for(;b[i]!=0;i++){
            if(b[i] < 0)
                ans += b[i] + 256;
            else
                ans += b[i];
        }
        return ans;
    }

    private int getColumnSize(byte[] b){
        int ans = 0;
        int i = 0;
        for(;b[i]!=0;i++){
            if(b[i] < 0)
                ans += b[i] + 256;
            else
                ans += b[i];
        }
        return ans;
    }




}
