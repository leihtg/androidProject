package dianshi.matchtrader.catchexception;

/**
 * Created by Administrator on 2016/7/26 0026.
 * <p>
 * http://bugs.dianshi-tech.com/home/receive?Version=2.0.6&Resolution=1776*1080&UserName=%E8%BF%98%E6%9C%AA%E7%99%BB%E5%BD%95&HardwareInfo=%E5%93%81%E7%89%8C%3AHuawei%E5%9E%8B%E5%8F%B7%3APE-TL20%E6%80%BB%E5%86%85%E5%AD%98%3A-1136377856.00B%E5%8F%AF%E7%94%A8%E5%86%85%E5%AD%98%3A1.33GB&Soft=%E7%82%B9%E7%9F%B3%E9%82%AE%E5%B8%81%E5%8D%A1&Mac=04%3A02%3A1f%3A28%3A1c%3Acc&OS=API19%2CAndroid4.4.2&DotNet=%E6%97%A0&SoftType=android%E7%A7%BB%E5%8A%A8%E7%AB%AF&Content=111&
 */
public class Bug_in {

    /// <summary>
    /// 操作系统
    /// </summary>
//   //[MaxLength(255)]
    public String OS;

    /// <summary>
    /// 软件类型，如：PC端，移动端，Web端等
    /// </summary>
    //[MaxLength(100)]
    public String SoftType;

    /// <summary>
    /// 软件
    /// </summary>
    //[MaxLength(100)]
    public String Soft;

    /// <summary>
    /// 软件版本
    /// </summary>
    //[MaxLength(20)]
    public String Version;

    /// <summary>
    /// .Net版本
    /// </summary>
    //[MaxLength(50)]
    public String DotNet;


    /// <summary>
    /// 用户名
    /// </summary>
    //[MaxLength(30)]
    public String UserName;

    /// <summary>
    /// 异常内容
    /// </summary>
    //[MaxLength(5000)]
    public String Content;

    /// <summary>
    /// 硬件信息
    /// </summary>
    //[MaxLength(1000)]
    public String HardwareInfo;

    /// <summary>
    /// 设备分辨率，如1280*1024
    /// </summary>
    //[MaxLength(20)]
    public String Resolution;

    /// <summary>
    /// 设备的Mac地址
    /// </summary>
    //[MaxLength(50)]
    public String Mac;

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getSoftType() {
        return SoftType;
    }

    public void setSoftType(String softType) {
        SoftType = softType;
    }

    public String getSoft() {
        return Soft;
    }

    public void setSoft(String soft) {
        Soft = soft;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDotNet() {
        return DotNet;
    }

    public void setDotNet(String dotNet) {
        DotNet = dotNet;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getHardwareInfo() {
        return HardwareInfo;
    }

    public void setHardwareInfo(String hardwareInfo) {
        HardwareInfo = hardwareInfo;
    }

    public String getResolution() {
        return Resolution;
    }

    public void setResolution(String resolution) {
        Resolution = resolution;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }
}
