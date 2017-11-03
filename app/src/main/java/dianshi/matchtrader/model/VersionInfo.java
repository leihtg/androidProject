package dianshi.matchtrader.model;

/**
 * 用于更新
 *
 * @author 钱智慧[qzhforthelife@163.com]
 * @date 2014-11-12 上午8:40:43
 */
public class VersionInfo {
    private String Name;

    private String Remark;

    private String CreateTime;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        this.Remark = remark;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        this.CreateTime = createTime;
    }


}
