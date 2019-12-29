
package net.socket;

import com.anser.contant.Contant;
import com.anser.model.FileModel;
import com.anser.util.BitConvert;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Scanner;

/**
 * @author lht
 * @time 2018/12/15 19:48
 */
public class FileTransferThread extends Thread {

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(Contant.FILE_SOCKET_PORT);
            while (true) {
                Socket socket = server.accept();
                InputStream is = socket.getInputStream();
                int read = is.read();
                switch (read) {
                case 1:// 上传
                    new UploadThread(socket).start();
                    break;
                case 2:// 下载
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class UploadThread extends Thread {

        Socket socket;

        public UploadThread(Socket socket) {

            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                Gson gson = new Gson();
                String homeDir = Contant.HOME_DIR;
                Scanner scanner = new Scanner(System.in);
                System.out.println("请输入保存文件目录:");
                while (scanner.hasNext()) {
                    String str = scanner.nextLine();
                    if (str.trim().length() != 0) {
                        homeDir = str;
                        break;
                    }
                }
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                int bufLen = 4096, read = 0;
                byte[] buf = new byte[bufLen];
                while (true) {
                    try {
                        int headLen = readHeadLen(is);
                        if (headLen == 0) {// 对方已关闭
                            break;
                        }
                        long readLen = 0;
                        int pos = 0;
                        StringBuilder sb = new StringBuilder();
                        while (readLen < headLen) {
                            pos = (int) (headLen - readLen > bufLen ? bufLen : headLen - readLen);
                            read = read(is, buf, pos);
                            if (-1 == read) {
                                return;
                            }
                            System.out.println("headLen = " + headLen);
                            sb.append(new String(buf, 0, read, "utf8"));
                            readLen += read;
                        }
                        FileModel model = gson.fromJson(sb.toString(), FileModel.class);
                        String path = model.getPath();
                        System.out.println("path = " + path);
                        File file = new File(homeDir, path);

                        processFileAttr(model, file);

                        if (file.exists() || model.isDir()) {// 文件存在就不传输了
                            os.write(0);
                            continue;
                        } else {
                            os.write(1);
                        }

                        long fileLen = model.getLength();
                        try (FileOutputStream fos = new FileOutputStream(file);) {
                            // 写文件内容
                            readLen = 0;
                            while (readLen < fileLen) {
                                pos = (int) (fileLen - readLen > bufLen ? bufLen : fileLen - readLen);
                                read = read(is, buf, pos);
                                if (-1 == read) {
                                    return;
                                }
                                if (fos != null) {
                                    fos.write(buf, 0, read);
                                }
                                readLen += read;
                            }
                            processFileAttr(model, file);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processFileAttr(FileModel model, File file) throws IOException {

            long lastModified = model.getLastModified();
            boolean exists = file.exists();

            if (!exists && model.isDir()) {
                file.mkdirs();
            }
            if (!exists) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return;
            }
            Path path = file.toPath();
            Files.setAttribute(path, "creationTime", FileTime.fromMillis(model.getCreationTime()));
            Files.setAttribute(path, "lastAccessTime", FileTime.fromMillis(model.getLastAccessTime()));
            Files.setLastModifiedTime(path, FileTime.fromMillis(lastModified));
        }
    }

    private int read(InputStream is, byte[] buf, int len) throws IOException {

        return is.read(buf, 0, len);
    }

    private int readHeadLen(InputStream is) throws IOException {

        byte[] buf = new byte[4];
        read(is, buf, buf.length);
        return BitConvert.convertToInt(buf, 0, 4);
    }
}
