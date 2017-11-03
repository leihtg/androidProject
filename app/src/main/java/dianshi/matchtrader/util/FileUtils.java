package dianshi.matchtrader.util;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

/**
 * 文件、目录辅助类
 *
 * @author qzh
 */
public class FileUtils {
    /**
     * 获取某目录下所有的文件[绝对路径，文件名]
     *
     * @param dir
     * @return
     */
    public static void getAllFiles(String dir, Map<String, String> files) {
        File dirF = new File(dir);
        File[] fList = dirF.listFiles();
        if (fList != null && fList.length > 0) {
            for (File f : fList) {
                if (f.isFile()) {
                    files.put(f.getAbsolutePath(), f.getName());
                } else {
                    getAllFiles(f.getAbsolutePath(), files);
                }
            }
        }
    }

    /**
     * 获取某目录下所有的文件文件名[绝对路径]
     *
     * @param dir
     * @return
     */
    public static void getAllFiles(String dir, ArrayList<String> files) {
        File dirF = new File(dir);
        File[] fList = dirF.listFiles();
        if (fList != null && fList.length > 0) {
            for (File f : fList) {
                if (f.isFile()) {
                    files.add(f.getAbsolutePath());
                } else {
                    getAllFiles(f.getAbsolutePath(), files);
                }
            }
        }
    }

    /**
     * 写入sd卡
     *
     * @param crashFilePath
     * @param fileName
     * @param content
     */
    public static void writeFile2SDCard(String crashFilePath, String fileName, String content) {

        try {
            String SDPATH = Environment.getExternalStorageDirectory() + crashFilePath + fileName;

            FileWriter fw = new FileWriter(SDPATH);
            File f = new File(SDPATH);
            fw.write(content);
            FileOutputStream os = new FileOutputStream(f);
            DataOutputStream out = new DataOutputStream(os);
            out.writeShort(2);
            out.writeUTF("");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ;


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }


    /**
     * 保存文件
     */
    public static void saveFile(String contentStr, String folderName, String fileName) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断Sd卡是否挂载

                File dir = new File(folderName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(folderName + fileName);
                fos.write(contentStr.getBytes());
                fos.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 读文件
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {

        String res = "";

        try {

            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];

            fin.read(buffer);

//            res = URLEncoder.encode(new String(buffer),"UTF-8");
            res = new String(buffer);

            fin.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return res;

    }

}
