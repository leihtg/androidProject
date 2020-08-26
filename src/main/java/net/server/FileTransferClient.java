
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

    public static void main(String[] args) {

        System.out.println("start...");
        Scanner scan = new Scanner(System.in);
        String host = null;
        while (true) {
            System.out.print(">>");
            String input = scan.nextLine();
            if (input.equals("quit")) {
                break;
            } else if (input.equals("send")) {
                if (null == host) {
                    System.out.println(">>输入服务器IP:");
                    host = readLine(scan);
                }
                System.out.println(">>输入发送文件:");
                String fDir = readLine(scan);
                sendFile(host, fDir);

                System.out.println("send over");
            } else if (input.equals("changeHost")) {
                System.out.println(">>输入服务器IP:");
                host = readLine(scan);
            } else {
                System.out.println("Type \"send\" to send file!");
                System.out.println("Type \"changeHost\" to change host!");
                System.out.println("Type \"quit\" to leave !");
            }
        }

    }

    private static void sendFile(String localhost, String fDir) {

        try {
            File file = new File(fDir);
            if (!file.exists()) {
                System.out.println("文件不存在:" + file);
                return;
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            efos.write((type + " : " + msg).getBytes("utf8"));
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
        write(path, os, head);
        write(path, os, bs);
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
                write(path, os, buf, len);
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write(String path, OutputStream os, byte[] buf, int len) throws IOException {

        os.write(buf, 0, len);
        prePos += len;
        if ((end = System.currentTimeMillis()) - start >= 1000) {// 每秒发送一次

            long per = prePos / (1024 * 1024);
            String unit = " MB/s";
            if (per == 0) {
                per = prePos / 1024;
                unit = " KB/s";
            }
            String speed = "发送文件: " + path + ", speed = " + per + unit;
            writeOneLine(speed);
            start = end;
            prePos = 0;
        }
    }

    static int showLen;

    private static void writeOneLine(String str) {

        if (showLen != 0) {
            while (showLen-- > 0)
                System.out.print("\b \b");
        }
        System.out.print(str);
        showLen = str.length();
    }

    private static void write(String path, OutputStream os, byte[] bs) throws IOException {

        write(path, os, bs, bs.length);
    }
}
