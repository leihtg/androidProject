package dianshi.matchtrader.model;

import java.io.Serializable;


/**
 * @author 钱智慧[Email:qzhforthelife@163.com]
 * @date 2014-10-21 上午11:34:20
 */
public class BaseModel_in implements Serializable {
    private String Timestamp;

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.Timestamp = timestamp;
    }


}
