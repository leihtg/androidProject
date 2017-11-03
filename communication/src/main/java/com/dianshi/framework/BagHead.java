package com.dianshi.framework;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BagHead {
    public int Length;
    public int Version;
    public int Type;


    /**
     * 拆包
     * @param buf
     * @param version
     * @return
     */
    public  static BagHead SplitBagHead(byte[] buf, int version){
        BagHead result = null;

        int length = BitConverters.ToInt32(buf,0);
        int v = BitConverters.ToInt32(buf,4);
        int type = BitConverters.ToInt32(buf,8);

        if(v == version){
            result = new BagHead();
            result.Length = length;
            result.Type = type;
        }
        return  result;
    }

    /**
     *装包
     * @param postBytes
     * @param type
     * @param version
     * @return
     */
    public static byte[] AssembleBag(byte[] postBytes, int type, int version){
        byte[] result = new byte[12];

        byte[] lbyte = BitConverters.GetBytes(postBytes.length);
        byte[] tbyte = BitConverters.GetBytes(type);
        byte[] vbyte = BitConverters.GetBytes(version);

        //System.arraycopy实现数组之间的复制
        System.arraycopy(lbyte,0,result,0,4);
        System.arraycopy(vbyte,0,result,4,4);
        System.arraycopy(tbyte,0,result,8,4);
        return  result;
    }
}
