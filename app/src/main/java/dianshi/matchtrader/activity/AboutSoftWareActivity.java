package dianshi.matchtrader.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dianshi.matchtrader.Utils.MathUtil;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.ArrayList;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.UserInfoAdapter;
import dianshi.matchtrader.customInterface.OnNoDoubleItemClickListener;
import dianshi.matchtrader.model.UserInfoModel;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.PackageUtils;
import dianshi.matchtrader.util.PhoneUtils;
import dianshi.matchtrader.util.UpdateManager;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class AboutSoftWareActivity extends ToolBarActivity {

    Context context;
    UserInfoAdapter adapter;
    ListView listView;
    ArrayList<UserInfoModel> array;
    boolean isLastestVersion;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_software);

        context = this;
        //初始化列表
        initListView(true);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            UpdateManager.getUpdateManager().checkAppUpdate(this, handler);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                isLastestVersion = false;
                array.get(1).setValue("");
            } else {
                isLastestVersion = true;
                array.get(1).setValue("已经是最新版本");
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            super.handleMessage(msg);
        }
    };


    /**
     * 初始化listview
     */
    public void initListView(boolean isFirst) {

        array = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_softWare);
        String[] names = getResources().getStringArray(R.array.aboutSoftWare_list);
        String[] values = {
                PackageUtils.getVersionName(context),
                "",
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

        listView.setOnItemClickListener(new OnNoDoubleItemClickListener() {
            @Override
            public void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        if (!isLastestVersion) {
                            CheckUpdate();
                        }

                        break;
                }
            }
        });


    }

    /**
     * 检测权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            UpdateManager.getUpdateManager().checkAppUpdate(activity, true);
        }
    }

    private void CheckUpdate() {
        verifyStoragePermissions(this);
    }


    @Override
    public void setTitle(TextView tv) {
        tv.setText(R.string.aboutSoftWare_aboutSoftWare);
        super.setTitle(tv);
    }
}
