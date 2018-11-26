/**
 *
 */
package com.anser.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.anser.annotation.BusinessType;
import com.anser.annotation.Scope;
import com.anser.business.inter.BusinessInter;
import com.anser.contant.Contant;
import com.anser.contant.ReceiveData;
import com.anser.enums.ActionType;
import com.anser.model.FileModel;
import com.anser.model.FileTransfer_in;
import com.anser.model.FileTransfer_out;
import com.anser.model.base.ModelOutBase;
import com.google.gson.JsonSyntaxException;

/**
 * @author leihuating
 * @time 2018年1月22日 下午1:37:36
 */
@Scope
public class UpAndDownloadFile implements BusinessInter {

    private static ConcurrentHashMap<String, RandomAccessFile> upFileMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RandomAccessFile> dwFileMap = new ConcurrentHashMap<>();

    /**
     * 从服务器下载到安卓
     *
     * @param rd
     * @return
     */
    @Override
    @BusinessType(ActionType.DOWN_LOAD)
    public ModelOutBase call(ReceiveData rd) {
        try {
            FileTransfer_in in = gson.fromJson(rd.data, FileTransfer_in.class);
            FileModel model = in.getModel();
            String path = model.getPath();
            RandomAccessFile rf = dwFileMap.get(path);
            File file = new File(Contant.HOME_DIR, path);
            if (!file.exists()) {
                throw new RuntimeException(path + " is not exist");
            }
            if (null == rf) {
                if (file.exists()) {
                    rf = new RandomAccessFile(file, "r");
                    dwFileMap.put(path, rf);
                }
            }
            FileTransfer_out out = new FileTransfer_out();
            FileModel of = new FileModel();
            of.setPath(path);
            of.setDir(file.isDirectory());
            of.setLength(file.length());
            of.setLastModified(file.lastModified());

            out.setModel(of);

            rf.seek(in.getPos());
            byte[] buf = new byte[2048];
            int len = rf.read(buf);
            boolean over = false;
            if (len == -1) {
                over = true;
            } else if (len == 2048) {
                out.setBuf(buf);
            } else {
                out.setBuf(Arrays.copyOf(buf, len));
                over = true;
            }
            if (over) {
                rf.close();
                dwFileMap.remove(path);
            }

            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从安卓上传到服务器
     *
     * @param rd
     * @return
     */
    @BusinessType(ActionType.UP_LOAD)
    public ModelOutBase callUp(ReceiveData rd) {
        try {
            FileTransfer_in fi = gson.fromJson(rd.data, FileTransfer_in.class);
            FileModel model = fi.getModel();
            String path = model.getPath();
            RandomAccessFile raf = upFileMap.get(path);
            if (raf == null) {
                File file = new File(Contant.HOME_DIR + File.separator + path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.setLastModified(model.getLastModified());
                raf = new RandomAccessFile(file, "rw");
                upFileMap.put(path, raf);
            }
            raf.seek(fi.getPos());
            raf.write(fi.getBuf());
            if (fi.getPos() + fi.getBuf().length == model.getLength()) {
                raf.close();

                upFileMap.remove(path);
            }
            FileTransfer_out out = new FileTransfer_out();
            FileModel fm = new FileModel();
            fm.setPath(path);
            out.setData("ok");
            return out;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
