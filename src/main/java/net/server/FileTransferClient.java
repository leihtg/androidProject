
package net.server;

import static com.anser.contant.Contant.FILE_SOCKET_PORT;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.function.Consumer;

import com.anser.model.FileModel;
import com.anser.util.BitConvert;
import com.google.gson.Gson;

/**
 * @auth leihtg
 * @date 2018/12/17 14:39
 */
public class FileTransferClient {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("输入服务器IP:");
        String localhost = readLine(scanner);
        System.out.println("输入发送文件:");
        String fDir = readLine(scanner);

        File file = new File(fDir);
        dirLen = file.getParent().length();

        Socket socket = new Socket(localhost, FILE_SOCKET_PORT);
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        os.write(1);// 1上传
        scan(file, f -> {
            try {
                sendFile(f, os, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        socket.close();

    }

    private static String readLine(Scanner scanner) {

        String str = null;
        while (scanner.hasNext()) {
            str = scanner.nextLine();
            if (str.trim().length() != 0) {
                break;
            }
        }
        return str;
    }

    static FileOutputStream efos;

    static void log(String type, String msg) {
        if (null == efos) {
            try {
                efos = new FileOutputStream("err.log");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            efos.write((type+" : "+msg).getBytes("utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void scan(File file, Consumer<FileModel> fun) throws IOException {

        if (null == file || !file.canRead()) {
            log("can not read", file.getAbsolutePath());
            return;
        }
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        boolean isdir = file.isDirectory();

        FileModel e = new FileModel();
        e.setPath(file.getAbsolutePath());
        e.setLength(file.length());
        e.setDir(isdir);
        e.setCreationTime(attr.creationTime().toMillis());
        e.setLastModified(attr.lastModifiedTime().toMillis());
        e.setLastAccessTime(attr.lastAccessTime().toMillis());
        e.setName(file.getName());
        if (isdir) {
            File[] files = file.listFiles();
            if (null == files) {
                return;
            }
            for (File f : files) {
                scan(f, fun);
            }
            fun.accept(e);
        } else {
            fun.accept(e);
        }
    }

    static int  dirLen;

    static long start, end, prePos = 0;

    static void sendFile(FileModel model, OutputStream os, InputStream is) throws IOException {

        String path = model.getPath();
        File file = new File(path);
        if (!file.canRead()) {
            log("can not read", file.getAbsolutePath());
            return;
        }
        model.setPath(path.substring(dirLen));
        String fileInfo = new Gson().toJson(model);
        byte[] bs = fileInfo.getBytes("utf8");
        byte[] head = BitConvert.convertToBytes(bs.length, 4);
        write(os, head);
        write(os, bs);
        int send = is.read();
        if (send != 1) {// 如果=1时才传输
            return;
        }
        if (model.isDir()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(path)) {
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buf = new byte[4096];
            int len = 0;
            while ((len = bis.read(buf)) != -1) {
                write(os, buf, len);
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int showLen;

    private static void write(OutputStream os, byte[] buf, int len) throws IOException {

        os.write(buf, 0, len);
        prePos += len;
        if ((end = System.currentTimeMillis()) - start >= 1000) {// 每秒发送一次
            if (showLen != 0) {
                while (showLen-- > 0)
                    System.out.print("\b \b");
            }
            long per = prePos / (1024 * 1024);
            String unit = " MB/s";
            if (per == 0) {
                per = prePos / 1024;
                unit = " KB/s";
            }
            String speed = "prePos = " + per + unit;
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
