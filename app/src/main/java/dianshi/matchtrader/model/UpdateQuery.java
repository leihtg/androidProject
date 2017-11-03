package dianshi.matchtrader.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 用于更新
 *
 * @param
 * @author 钱智慧[qzhforthelife@163.com]
 * @return
 * @date 2014-11-12 上午9:04:46
 */
public class UpdateQuery {
    private boolean IsNeedUpdate;

    private List<NewFile> FileList;

    private List<VersionInfo> VersionList;

    public boolean getIsNeedUpdate() {
        return IsNeedUpdate;
    }

    public void setIsNeedUpdate(boolean isNeedUpdate) {
        this.IsNeedUpdate = isNeedUpdate;
    }

    public List<NewFile> getFileList() {
        return FileList;
    }

    public void setFileList(List<NewFile> fileList) {
        this.FileList = fileList;
    }

    public List<VersionInfo> getVersionList() {
        return VersionList;
    }

    public void setVersionList(List<VersionInfo> versionList) {
        this.VersionList = versionList;
    }

    public UpdateQuery() {
    }


}

