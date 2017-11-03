package dianshi.matchtrader.activity;

import android.os.Bundle;
import android.widget.TextView;

import dianshi.matchtrader.R;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.view.RingView;

/**
 * Created by Administrator on 2016/5/8 0008.
 */
public class MoneyInActivity extends ToolBarActivity {

    RingView ringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_in);

        //资产百分比统计图

    }

    @Override
    public void setTitle(TextView tv) {
        super.setTitle(tv);
        tv.setText(R.string.moneyIn_moneyIn);
    }
}
