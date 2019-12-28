package com.anser.comminute;

import com.anser.model.FileModel;
import com.anser.util.BitConvert;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.anser.contant.Contant.FILE_SOCKET_PORT;

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

        Socket socket = new Socket("localhost", FILE_SOCKET_PORT);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        os.write(1);//1上传
        for (FileModel fm : list) {
            System.out.println("fm = " + fm.getPath());
            sendFile(fm, os, is);
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

    static void sendFile(FileModel model, OutputStream os, InputStream is) throws IOException {
        String path = model.getPath();
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        model.setPath(path.substring(dirLen));
        String fileInfo = gson.toJson(model);
        byte[] bs = fileInfo.getBytes("utf8");
        byte[] head = BitConvert.convertToBytes(bs.length, 4);
        write(os, head);
        write(os, bs);
        int send = is.read();
        if (send != 1) {//如果=1时才传输
            return;
        }
        try (FileInputStream fis = new FileInputStream(path)) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = bis.read(buf)) != -1) {
                write(os, buf, len);
            }
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int showLen;

    private static void write(OutputStream os, byte[] buf, int len) throws IOException {
        os.write(buf, 0, len);
        prePos += len;
        if ((end = System.currentTimeMillis()) - start >= 1000) {//每秒发送一次
            if (showLen != 0) {
                while (showLen-- > 0)
                    System.out.print("\b \b");
            }
            String speed = "prePos = " + prePos / (1024 * 1024) + " MB/s";
            System.out.print(speed);
            showLen = speed.length();
            start = end;
            prePos = 0;
        }
    }

    private static void write(OutputStream os, byte[] bs) throws IOException {
        write(os, bs, bs.length);
    }
}
