package dianshi.matchtrader.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import dianshi.matchtrader.R;
import dianshi.matchtrader.toolbar.BaseActivity;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class DisclaimOfLiabilityActivity extends BaseActivity {


    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaim_of_liability);


        //初始化WebView
        initWebview();

    }

    /**
     * 初始化WebView
     */
    public void initWebview() {

        webView = (WebView) findViewById(R.id.webview_disclaim_liability);

        // 设置支持JavaScript等
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setSupportZoom(true);
        webView.setHapticFeedbackEnabled(false);

        webView.loadUrl("file:///android_asset/claim.html");


    }


}
