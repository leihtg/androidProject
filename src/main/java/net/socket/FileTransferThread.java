package net.socket;

import com.anser.contant.Contant;
import com.anser.model.FileModel;
import com.anser.util.BitConvert;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lht
 * @time 2018/12/15 19:48
 */
public class FileTransferThread extends Thread {
    Gson gson = new Gson();

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(Contant.FILE_SOCKET_PORT);
            while (true) {
                Socket socket = server.accept();
                InputStream is = socket.getInputStream();
                int read = is.read();
                switch (read) {
                    case 1://上传
                        new UploadThread(socket).start();
                        break;
                    case 2://下载
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
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                int bufLen = 2048, read = 0;
                byte[] buf = new byte[bufLen];
                while (true) {
                    try {
                        read = is.read(buf, 0, 4);
                        if (-1 == read) {
                            return;
                        }
                        int headLen = BitConvert.convertToInt(buf, 0, 4);
                        read = is.read(buf, 0, 8);
                        if (-1 == read) {
                            return;
                        }
                        long bodyLen = BitConvert.convertToLong(buf, 0, 8);

                        long readLen = 0;
                        int pos = 0;
                        StringBuilder sb = new StringBuilder();
                        while (readLen < headLen) {
                            pos = (int) (headLen - readLen > bufLen ? bufLen : headLen - readLen);
                            read = is.read(buf, 0, pos);
                            if (-1 == read) {
                                return;
                            }
                            System.out.println("headLen = " + headLen);
                            if (headLen > 1000) {
                                System.out.println("bodyLen = " + bodyLen);
                            }
                            sb.append(new String(buf, 0, read, "utf8"));
                            readLen += read;
                        }
                        FileOutputStream fos = null;
                        File file = null;
                        long lastModified = 0;
                        try {
                            FileModel model = gson.fromJson(sb.toString(), FileModel.class);
                            String path = model.getPath();
                            System.out.println("path = " + path);
                            file = new File(Contant.HOME_DIR, path);
                            lastModified = model.getLastModified();
                            if (model.isDir()) {
                                if (file.exists()) {
                                    if (file.lastModified() > lastModified) {
                                        file.setLastModified(lastModified);
                                    }
                                } else {
                                    file.mkdirs();
                                    file.setLastModified(lastModified);
                                }
                                continue;
                            }
                            if (!file.exists()) {
                                if (!file.getParentFile().exists()) {
                                    file.getParentFile().mkdirs();
                                }
                            }
                            if (file.length() == model.getLength()) {
                                if (file.lastModified() > lastModified) {
                                    file.setLastModified(lastModified);
                                }
                            } else if (bodyLen > 0) {
                                try {
                                    fos = new FileOutputStream(file);
                                } catch (FileNotFoundException e) {
                                    System.out.println("file = " + file);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("sb = " + sb);
                            e.printStackTrace();
                        }
                        //写文件内容
                        readLen = 0;
                        while (readLen < bodyLen) {
                            pos = (int) (bodyLen - readLen > bufLen ? bufLen : bodyLen - readLen);
                            read = is.read(buf, 0, pos);
                            if (-1 == read) {
                                return;
                            }
                            if (fos != null) {
                                fos.write(buf, 0, read);
                            }
                            readLen += read;
                        }
                        if (null != fos) {
                            fos.close();
                            file.setLastModified(lastModified);
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
    }
}
