package dianshi.matchtrader.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dianshi.matchtrader.server.GlobalSingleton;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.toolbar.ToolBarActivity;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class SchoolActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        //初始化WebView
        initWebView();

    }

    /**
     * 初始化WebView
     */
    public void initWebView() {

        WebView webview = (WebView) findViewById(R.id.webview_school);

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //加载需要显示的网页
        webview.loadUrl(GlobalSingleton.SchoolWebSite);
        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    @Override
    public void setTitle(TextView tv) {
        tv.setText("学堂");
        super.setTitle(tv);
    }
}
