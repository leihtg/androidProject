package dianshi.matchtrader.model;

/**
 * 操作日志的实体类
 * Created by Administrator on 2016/5/5 0005.
 *
 * @author DR
 */
public class OperateLogModel extends LoginResultModel_out {

    private String Province;
    private String IP;
    private String City;
    private String SysCreateTime;
    private String Note;

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSysCreateTime() {
        return SysCreateTime;
    }

    public void setSysCreateTime(String sysCreateTime) {
        SysCreateTime = sysCreateTime.replace('T', ' ');
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
