package dianshi.matchtrader.model;

import com.dianshi.matchtrader.model.ModelOutBase;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class OperateLogListModel_out extends ModelOutBase {


    private double AllMoney;
    private int AllCount;
    private int AllPage;
    private int page;
    private int pageSize;

    private List<OperateLogModel> EntityCollection;


    public double getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public List<OperateLogModel> getEntityCollection() {
        return EntityCollection;
    }

    public void setEntityCollection(List<OperateLogModel> entityCollection) {
        EntityCollection = entityCollection;
    }
}
