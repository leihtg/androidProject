package dianshi.matchtrader.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dianshi.matchtrader.model.IDModel_in;
import com.dianshi.matchtrader.model.NewsModel_out;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import dianshi.matchtrader.R;
import dianshi.matchtrader.constant.APPFinal;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.toolbar.ToolBarActivity;
import dianshi.matchtrader.util.ScreenUtils;

/**
 * 公告详情页面
 */
public class NoticeDetailActivity extends ToolBarActivity {

    int newsId;
    NewsModel_out news;
    Context context;
    //分享
    LinearLayout layout_share;
    WebView webView;
    TextView titleTextView, dateTextView, timeTextView;
    /**
     * 友盟分享Controller
     **/
    private UMSocialService mController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        context = this;

        //得到上一个页面传过来的公告id
        newsId = (int) getIntent().getExtras().get("NewsId");

        //绑定控件id
        findViewById();

        //初始化WebView
        initWebview();

        //加载数据
        loadNews();

    }

    /**
     * 绑定控件id
     */
    private void findViewById() {
        webView = (WebView) findViewById(R.id.txt_noticeDetail_content);
        titleTextView = (TextView) findViewById(R.id.txt_noticeDetail_title);
        dateTextView = (TextView) findViewById(R.id.txt_noticeDetail_dayTime);
        timeTextView = (TextView) findViewById(R.id.txt_noticeDetail_hourTime);
    }

    /**
     * toolbar设置
     *
     * @param toolbar
     */
    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        TextView tv = (TextView) toolbar.findViewById(R.id.txt_toolbar_title);
        tv.setText(R.string.noticeNav);
    }


    /**
     * 初始化WebView
     */
    private void initWebview() {

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        //显示全屏
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);


        webView.setHorizontalScrollBarEnabled(false);//设置水平滚动条
        webView.setHorizontalFadingEdgeEnabled(false);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                webView.measure(w, h);


            }
        });

    }

    /**
     * 初始化分享按键
     */
    public void initShareBtn() {


        layout_share = (LinearLayout) findViewById(R.id.layout_notice_detail_shared);

        layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Handler mHandler = new Handler();

                mHandler.post(new Runnable() {

                    public void run() {

                        //初始化友盟分享平台
                        initPlatFrom();
                        //微信好友分享内容
                        shareWEIXIN();
                        //微信朋友圈分享内容
                        shareWEIXIN_CIRCLE();

                        //分享微信好友和微信朋友圈
                        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
                        //分享順序
                        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);

                        mController.openShare(NoticeDetailActivity.this, false);

                        mController.registerListener(mSnsPostListener);

                    }

                });


            }
        });
    }

    /**
     * 显示公告
     */
    private void loadNews() {
        IDModel_in model_in = new IDModel_in();
        model_in.setId(newsId);

        ConcurrentHashMap<String, String> dict = new ConcurrentHashMap<>();
        FuncCall<IDModel_in, NewsModel_out> funcCall = new FuncCall<>();
        funcCall.FuncResultHandler = loadSuccHandler;
        funcCall.FuncErrHandler = loadFailHandler;
        funcCall.Call("NewsDetail", model_in, NewsModel_out.class, dict);
    }

    private Handler loadSuccHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            news = (NewsModel_out) msg.obj;
            //显示内容
            setContent();
        }
    };
    private Handler loadFailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //显示提示对话框
            MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.tipDialog("获得新闻列表失败");
            finish();
        }
    };


    /**
     * 调整WebView里面内容的格式问题
     *
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";

        return "<html>" + head + "<body height =\"auto\" width=" + ScreenUtils.getScreenWidth(context) +
                " style=\"word-wrap:break-word;font-size: " + 18.0f + ";line-height=" + 10.0f + "\">" + bodyHTML + "</body></html>";
    }

    /**
     * 设置内容
     */
    private void setContent() {
        if (news != null) {
            //加载网页内容
            String content = news.getContent();
            webView.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "utf-8", null);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
                    int w = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    //重新测量
                    webView.measure(w, h);


                }
            });

            try {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dfDate = new SimpleDateFormat("yyyy年MM月dd日");
                DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
                Date d = df.parse(news.getSysCreateTime().replace("T", " "));
                dateTextView.setText(dfDate.format(d));
                timeTextView.setText(dfTime.format(d));
                titleTextView.setText(news.getTitle());


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 初始化友盟分享平台
     */
    public void initPlatFrom() {

        mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

        // 添加微信好友分享sdk
        UMWXHandler wxHandler = new UMWXHandler(NoticeDetailActivity.this, APPFinal.WEXIN_APP_ID, APPFinal.WEXIN_APP_SERCET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈分享sdk
        UMWXHandler wxCircleHandler = new UMWXHandler(NoticeDetailActivity.this, APPFinal.WEXIN_APP_ID, APPFinal.WEXIN_APP_SERCET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


    }


    /**
     * 分享回调监听
     */
    SocializeListeners.SnsPostListener mSnsPostListener = new SocializeListeners.SnsPostListener() {

        @Override
        public void onStart() {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int stCode,
                               SocializeEntity entity) {
            if (stCode == 200) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(context,
                        "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * 设置微信好友分享内容
     */

    public void shareWEIXIN() {


        WeiXinShareContent weixinContent = new WeiXinShareContent();

        weixinContent.setShareContent(news.getTitle());//设置分享内容
        weixinContent.setTitle(news.getTitle()); //设置分享标题
        weixinContent.setTargetUrl(GlobalSingleton.CreateInstance().HomePage); //设置分享内容跳转URL

        //设置分享图片
        UMImage img = new UMImage(context, R.mipmap.ic_launcher);
        weixinContent.setShareImage(img);


        mController.setShareMedia(weixinContent);


    }


    /**
     * 设置微信朋友圈分享内容
     */
    public void shareWEIXIN_CIRCLE() {
        //CircleShareContent是和分享到微信好友不同的地方
        CircleShareContent circleMedia = new CircleShareContent();

        circleMedia.setShareContent(news.getTitle());//设置分享内容
        circleMedia.setTitle(news.getTitle());//设置分享标题
        circleMedia.setTargetUrl(GlobalSingleton.CreateInstance().HomePage); //设置分享内容跳转URL

        //设置分享图片
        UMImage img = new UMImage(context, R.mipmap.ic_launcher);
        circleMedia.setShareImage(img);


        mController.setShareMedia(circleMedia);

    }


}
