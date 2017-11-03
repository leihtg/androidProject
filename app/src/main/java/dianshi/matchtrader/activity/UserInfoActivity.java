package dianshi.matchtrader.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.UserInfoAdapter;
import dianshi.matchtrader.model.UserInfoModel;
import dianshi.matchtrader.toolbar.ToolBarActivity;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class UserInfoActivity extends ToolBarActivity {

    Context context;
    UserInfoAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        context = this;
        //初始化侧边栏
        initListView(true);
        //检测用户金额
        GlobalSingleton.CreateInstance().ServerPushHandler.regMoneyHandler(moneyChangeHandler);

    }


    /**
     * 初始化listview
     */
    public void initListView(boolean isFirst) {

        GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
        ArrayList<UserInfoModel> array = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_userInfo);
        String[] names = getResources().getStringArray(R.array.userInfo_list);
        String[] values = {
                globalSingleton.UserInfoPool.getUserName(),
                globalSingleton.TrueName,
                globalSingleton.UserInfoPool.getType(),
                globalSingleton.UserInfoPool.getPhoneNum(),
                MathUtil.toBigDecimal(globalSingleton.UserInfoPool.getMoney() == null ? 0 : globalSingleton.UserInfoPool.getMoney(), 2).toString(),
                MathUtil.toBigDecimal(globalSingleton.UserInfoPool.getFrozenMoney() == null ? 0 : globalSingleton.UserInfoPool.getFrozenMoney(), 2).toString()
        };

        for (int i = 0; i < names.length; i++) {
            UserInfoModel model = new UserInfoModel();
            model.setName(names[i]);
            model.setValue(values[i]);
            array.add(model);
        }

        if (isFirst) {
            adapter = new UserInfoAdapter(context, array);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }


    }

    /**
     * 用户信息发生变化
     */
    private Handler moneyChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                initListView(false);
            }
        }
    };

    @Override
    public void setTitle(TextView tv) {
        tv.setText(R.string.userInfo_userinfo);
        super.setTitle(tv);
    }
}
