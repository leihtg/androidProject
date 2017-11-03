package dianshi.matchtrader.customInterface;

import android.view.View;
import android.widget.AdapterView;

/**
 * 接口监听器,禁止列表的行双击事件,以免触发两次单击
 */
public abstract class OnNoDoubleItemClickListener implements AdapterView.OnItemClickListener {

    public static final int DELAY = 1000;  //连击事件间隔
    private long lastClickTime = 0; //记录最后一次时间

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > DELAY) {  //判断时间差
            lastClickTime = currentTime;  //记录最后一次点击时间
            onNoDoubleClick(parent, view, position, id);
        }
    }

    //抽象一个无连击事件方法，用于实现内容
    public abstract void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id);
}