package dianshi.matchtrader.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.dianshi.matchtrader.model.ProductModel_out;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.List;

import dianshi.matchtrader.R;
import dianshi.matchtrader.adapter.ProductAutoComplateAdapter;
import dianshi.matchtrader.database.SQLiteHelper;
import dianshi.matchtrader.dialog.MyAlertDialog;
import dianshi.matchtrader.toolbar.BaseActivity;
import dianshi.matchtrader.util.KeyBoardUtils;

/**
 * Created by Administrator on 2016/5/16 0016.
 */
public class AddChooseActivity extends BaseActivity {

    Context context;
    AutoCompleteTextView productAutoComplateTextView;
    List<ProductModel_out> array;

    int product_id;

    //SQLite数据库
    SQLiteDatabase db;
    //SQLite数据库操作类
    SQLiteHelper sqlHelper = new SQLiteHelper(AddChooseActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_choose);

        context = this;

        //初始化popupWindow的控件参数
        initView();

        //藏品的自动补全列表
        initAutoComplate();

    }


    /**
     * 藏品的自动补全列表
     */
    public void initAutoComplate() {
        productAutoComplateTextView = (AutoCompleteTextView) findViewById(R.id.edt_priceList_ppw_stockCode);
        productAutoComplateTextView.setFocusable(true);
        array = GlobalSingleton.CreateInstance().ProductPool.getProduct();
        ProductAutoComplateAdapter adapter = new ProductAutoComplateAdapter(AddChooseActivity.this, array);
        productAutoComplateTextView.setAdapter(adapter);
        productAutoComplateTextView.setThreshold(0);//设置用户至少输入几个字符才会提示
        productAutoComplateTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //填写到编辑框中
                ProductModel_out product = (ProductModel_out) parent.getItemAtPosition(position);
                productAutoComplateTextView.setText(product.getName() + "【" + product.getCode() + "】");

                product_id = product.getId();

                if (KeyBoardUtils.isOpen(context)) {
                    KeyBoardUtils.closeKeyboard(productAutoComplateTextView, context);

                }

            }
        });

        productAutoComplateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAutoComplateTextView.setText(" ");
            }
        });

        //设置编辑框为输入内容的时候提示字体
        productAutoComplateTextView.setHint(GlobalSingleton.CreateInstance().ProductTypeName + context.getResources().getString(R.string.chooseList_stockCode));


    }


    /**
     * 初始化控件参数
     */
    public void initView() {


        Button btn_cancel = (Button) findViewById(R.id.btn_priceList_ppw_cancel);
        btn_cancel.setOnClickListener(onClickListener);

        Button btn_add = (Button) findViewById(R.id.btn_priceList_ppw_add);
        btn_add.setOnClickListener(onClickListener);


        //数据库可执行对象
        db = sqlHelper.getWritableDatabase();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_priceList_ppw_cancel:
                    //关闭
                    finish();
                    break;
                case R.id.btn_priceList_ppw_add:
                    //判断是否选择了数据
                    if (productAutoComplateTextView.getText().toString().equals("")) {
                        //显示提示对话框
                        AlertMsg("未选择数据");
                        return;

                    }
                    //判断输入的数据是否是展示的藏品
                    if (!productAutoComplateTextView.getText().toString().contains("【")) {
                        AlertMsg("请输入有效的" + GlobalSingleton.CreateInstance().ProductTypeName + "代码或名称");
                        return;
                    }

                    //判断是否插入过
                    if (sqlHelper.queryById(db, product_id)) {
                        AlertMsg("已添加过该条数据");
                        return;
                    } else {
                        //将此数据插入数据库的表中
                        sqlHelper.insert(db, product_id);
                        finish();
                    }


                    break;
            }
        }
    };

    private void AlertMsg(String msg) {
        MyAlertDialog dialog = new MyAlertDialog(context);
        dialog.tipDialog(msg);
    }
}
