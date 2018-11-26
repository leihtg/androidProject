/**
 *
 */
package com.anser.business;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;

import com.anser.annotation.BusinessType;
import com.anser.annotation.Scope;
import com.anser.business.inter.BusinessInter;
import com.anser.contant.Contant;
import com.anser.contant.ReceiveData;
import com.anser.enums.ActionType;
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

    private static ConcurrentHashMap<String, RandomAccessFile> fileMap = new ConcurrentHashMap<>();

    @Override
    @BusinessType(ActionType.DOWN_LOAD)
    public ModelOutBase call(ReceiveData rd) {

        return null;
    }

    @BusinessType(ActionType.UP_LOAD)
    public ModelOutBase callUp(ReceiveData rd) {
        try {
            FileTransfer_in fi = gson.fromJson(rd.data, FileTransfer_in.class);
            String path = fi.getPath();
            RandomAccessFile raf = fileMap.get(path);
            if (raf == null) {
                File file = new File(Contant.HOME_DIR + File.separator + path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                raf = new RandomAccessFile(file, "rw");
                fileMap.put(path, raf);
            }
            raf.seek(fi.getPos());
            raf.write(fi.getBuf());
            if (fi.getPos() + fi.getBuf().length == fi.getLength()) {
                raf.close();
                fileMap.remove(path);
            }
            FileTransfer_out out = new FileTransfer_out();
            out.setPath(path);
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
