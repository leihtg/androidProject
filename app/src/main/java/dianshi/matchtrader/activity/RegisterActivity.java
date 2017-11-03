package dianshi.matchtrader.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dianshi.matchtrader.server.GlobalSingleton;

import dianshi.matchtrader.R;
import dianshi.matchtrader.toolbar.ToolBarActivity;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class RegisterActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化WebView
        initWebView();

    }

    /**
     * 初始化WebView
     */
    public void initWebView() {

        WebView webview = (WebView) findViewById(R.id.webview_register);

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl(GlobalSingleton.RefOpenUrl);
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
        tv.setText("用户注册");
        super.setTitle(tv);
    }
}
