package com.anser.comminute;

import com.anser.model.FileModel;
import com.anser.util.BitConvert;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth leihtg
 * @date 2018/12/17 14:39
 */
public class FileTransferTest {
    static String dir = "E:\\soft";

    public static void main(String[] args) throws IOException {
        File file = new File(dir);
        List<FileModel> list = new ArrayList<>();
        scan(file, list);

        Socket socket = new Socket("localhost", 8181);
        OutputStream os = socket.getOutputStream();
        os.write(1);//1上传
        for (FileModel fm : list) {
            System.out.println("fm = " + fm.getPath());
            sendFile(fm, os);
        }
        socket.close();

    }

    static void scan(File file, List<FileModel> list) {
        if (null == file || !file.canRead()) {
            return;
        }
        boolean isdir = file.isDirectory();
        FileModel e = new FileModel();
        e.setPath(file.getAbsolutePath());
        e.setLength(file.length());
        e.setDir(isdir);
        e.setLastModified(file.lastModified());
        e.setName(file.getName());
        if (isdir) {
            File[] files = file.listFiles();
            if (null == files) {
                return;
            }
            for (File f : files) {
                scan(f, list);
            }
            list.add(e);
        } else {
            list.add(e);
        }
    }

    static int dirLen = dir.length();
    static Gson gson = new Gson();

    static long start, end, prePos = 0;

    static void sendFile(FileModel model, OutputStream os) throws IOException {
        String path = model.getPath();
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        model.setPath(path.substring(dirLen));
        String fileInfo = gson.toJson(model);
        byte[] bs = fileInfo.getBytes("utf8");
        byte[] head = BitConvert.convertToBytes(bs.length, 4);
        os.write(head);
        if (file.isDirectory()) {
            os.write(BitConvert.convertToBytes(0));
            os.write(bs);
            return;
        }
        long length = model.getLength();
        os.write(BitConvert.convertToBytes(length));
        os.write(bs);
        try (FileInputStream fis = new FileInputStream(path)) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buf = new byte[2048];
            int len = 0;
            int pos = 0;

            start = System.currentTimeMillis();
            while ((len = bis.read(buf)) != -1) {
                os.write(buf, 0, len);
                pos += len;
                prePos += len;
                if ((end = System.currentTimeMillis()) - start >= 1000) {//每秒发送一次
                    System.out.println("prePos = " + prePos / (1024 * 1024) + "MB/s");
                    start = end;
                    prePos = 0;
                }
            }
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
