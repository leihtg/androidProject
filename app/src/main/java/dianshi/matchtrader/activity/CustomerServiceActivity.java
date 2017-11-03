package dianshi.matchtrader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianshi.matchtrader.server.GlobalSingleton;

import dianshi.matchtrader.R;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.RegexpValidatorUtils;
import dianshi.matchtrader.util.StringUtils;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class CustomerServiceActivity extends ToolBarActivity {

    Button btn;
    ImageView img_customerService;
    View layout_customerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);


        //初始化视图
        initView();
    }

    @Override
    public void setTitle(TextView tv) {
        tv.setText(R.string.customerService_serverOnLine);
        super.setTitle(tv);
    }

    /**
     * 初始化视图
     */
    public void initView() {

        btn = (Button) findViewById(R.id.btn_customer_service_telephone);
        img_customerService = (ImageView) findViewById(R.id.img_customerService);
        layout_customerService = findViewById(R.id.layout_customerService);
        TextView tvCompany = (TextView) findViewById(R.id.customerService_company);
        TextView tvWebSite = (TextView) findViewById(R.id.customerService_companyWesite);


        //法兰典显示客服介绍图片
        if (GlobalSingleton.CompayName.contains("法兰典")) {
            img_customerService.setVisibility(View.VISIBLE);
            layout_customerService.setVisibility(View.GONE);
        } else {
            img_customerService.setVisibility(View.GONE);
            layout_customerService.setVisibility(View.VISIBLE);


            tvCompany.setText(GlobalSingleton.CompayName);

            //判断客户号码是否符合要求
            if (GlobalSingleton.Phone == null || GlobalSingleton.Phone.equals("") || !StringUtils.isOnlyNumber(GlobalSingleton.Phone.replace("-", ""))) {
                btn.setText("敬请期待~");

            } else {
                btn.setText(GlobalSingleton.Phone);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (GlobalSingleton.Phone == null || GlobalSingleton.Phone.equals("") || !StringUtils.isOnlyNumber(GlobalSingleton.Phone.replace("-", ""))) {
                            return;
                        }
                        //拨打电话
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + GlobalSingleton.Phone);
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
            }

            //判断公司网址是否符合要求
            if (GlobalSingleton.HomePage == null || GlobalSingleton.HomePage.equals("") || !RegexpValidatorUtils.isNetUrl(GlobalSingleton.HomePage)) {
                tvWebSite.setVisibility(View.GONE);
            } else {
                tvWebSite.setText(GlobalSingleton.HomePage);

            }

        }


    }


}
