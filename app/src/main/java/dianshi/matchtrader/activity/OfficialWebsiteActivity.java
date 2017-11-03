package dianshi.matchtrader.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dianshi.matchtrader.server.GlobalSingleton;

import dianshi.matchtrader.R;
import dianshi.matchtrader.dialog.WaitingDialog;
import dianshi.matchtrader.toolbar.ToolBarActivity;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class OfficialWebsiteActivity extends ToolBarActivity {


    WaitingDialog waitingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_website);

        //初始化WebView
        initWebView();
    }

    /**
     * 初始化WebView
     */
    public void initWebView() {

        WebView webview = (WebView) findViewById(R.id.webview_official_website);
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //加载需要显示的网页
        webview.loadUrl(GlobalSingleton.HomePage);


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (waitingDialog == null) {
                    waitingDialog = new WaitingDialog(OfficialWebsiteActivity.this);
                    waitingDialog.setCancelable(true);
                    waitingDialog.show();


                } else if (!waitingDialog.isShowing()) {
                    waitingDialog.show();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (waitingDialog != null) {
                    waitingDialog.dismiss();
                }
            }


        });

    }

    @Override
    public void setTitle(TextView tv) {
        tv.setText("官网");
        super.setTitle(tv);
    }

    @Override
    public void onBackPressed() {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
        super.onBackPressed();
    }
}
