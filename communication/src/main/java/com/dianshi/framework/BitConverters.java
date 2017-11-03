package com.dianshi.framework;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BitConverters {
    public  static int ToInt32(byte[] buf,int offSet) {
        int result = 0;
        //result =  +  +  + b[3];

        for (int i = 4 + offSet - 1; i >= offSet; i--) {
            result <<= 8;
            result |= (buf[i] & 0x000000ff);
        }

        return result;
    }

    public  static byte[] GetBytes(int res){
        byte[] result = new byte[4];
        for (int i=0;i<4;i++){
            result[4-i-1] = (byte)(res>>24-i*8);
        }
        return  result;
    }

    public static float ToSingle(byte[] b, int index){
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

}
