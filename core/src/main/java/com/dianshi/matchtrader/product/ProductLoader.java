package com.dianshi.matchtrader.product;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dianshi.matchtrader.model.ListPostionModel_out;
import com.dianshi.matchtrader.model.ListProductCategoryModel_out;
import com.dianshi.matchtrader.model.ListProductModel_out;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.server.FuncCall;
import com.dianshi.matchtrader.server.GlobalSingleton;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品加载
 * 加载商品分类和所有商品列表
 * 加载用户的持仓商品列表
 */
public class ProductLoader
{
    /**总加载handler*/
    public Handler loadSuccHandler;
    /**
     * 持仓加载handler
     */
    public Handler loadPositionHandler;
    private boolean isLoadCategory = false;
    private boolean isLoadPositionProduct = false;
    private boolean isLoadProduct = false;
    private boolean isError = false;
    private String errorMsg = "";

    /**
     * 加载所有的商品信息
     */
    public void load()
    {
        loadCategory();
        loadProduct();
        loadPositionProduct();
    }

    /**
     * 加载分类
     */
    public void loadCategory(){
        ConcurrentHashMap<String,String> errorDict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase,ListProductCategoryModel_out> funcCall= new FuncCall<>();
        funcCall.FuncResultHandler = loadCateSuccHandler;
        funcCall.FuncErrHandler = loadCateFailHandler;

        funcCall.Call("ProductCategoryLoader",new ModelInBase(),ListProductCategoryModel_out.class,errorDict);
        if(errorDict != null && errorDict.keys().hasMoreElements()){
            isLoadCategory = true;
            isError = true;
            errorMsg = "产品分类加载错误";
            checkLoad();
        }
    }

    /**
     * 加载商品
     */
    public void loadProduct(){

        ConcurrentHashMap<String,String> errorDict = new ConcurrentHashMap<>();
        FuncCall<ModelInBase,ListProductModel_out> funcCall= new FuncCall<>();
        funcCall.FuncResultHandler = loadProductSuccHandler;
        funcCall.FuncErrHandler = loadProductFailHandler;

        funcCall.Call("ProductLoader",new ModelInBase(),ListProductModel_out.class,errorDict);
        if(errorDict != null && errorDict.keys().hasMoreElements()){
            isLoadProduct = true;
            isError = true;
            errorMsg = "产品加载错误";
            checkLoad();
        }
    }

    /**
     * 加载持仓商品列表
     */
    public void loadPositionProduct(){


        ConcurrentHashMap<String,String> errorDict  = new ConcurrentHashMap<>();
        FuncCall<ModelInBase,ListPostionModel_out> funcCall = new FuncCall<>();
        funcCall.FuncResultHandler = loadPositionSuccHandler;
        funcCall.FuncErrHandler = loadPositionFailHandler;
        funcCall.Call("Position",new ModelInBase(),ListPostionModel_out.class,errorDict);
        if(errorDict!= null && errorDict.keys().hasMoreElements()){
            isLoadPositionProduct = true;
            isError = true;
            errorMsg = "持仓产品加载错误";
            checkLoad();
        }
    }

    /**
     * 加载商品失败
     *
     */
    private Handler loadProductFailHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            isLoadProduct = true;
            isError = true;
            errorMsg = "加载产品失败";
            checkLoad();
        }
    };
    /**
     * 加载商品成功
     */
    private Handler loadProductSuccHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            isLoadProduct = true;
            ListProductModel_out model = (ListProductModel_out)msg.obj;

            //因为GlobalSingleton.CreateInstance().ProductPool在GlobalSingleton中没有实例化所以会报nullPointException
            GlobalSingleton.CreateInstance().ProductPool.AddProduct(model.getItemCollection());
            checkLoad();
        }
    };

    /**
     * 加载持仓商品成功
     */
    private Handler loadPositionSuccHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            isLoadPositionProduct = true;
            ListPostionModel_out model = (ListPostionModel_out)msg.obj;
            GlobalSingleton.CreateInstance().ProductPool.AddPositionProduct(model.getItemCollection());
            checkLoad();
        }
    };
    /**
     * 加载持仓商品失败
     */
    private Handler loadPositionFailHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            isLoadPositionProduct = true;
            isError = true;
            errorMsg = "持仓产品获得而失败";
            checkLoad();

        }
    };

    /**
     * 加载商品分类成功
     */
    private Handler loadCateSuccHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            isLoadCategory = true;
            ListProductCategoryModel_out model = (ListProductCategoryModel_out)msg.obj;
            GlobalSingleton.CreateInstance().ProductPool.AddCategory(model.getItemCollection());
            checkLoad();
        }
    };
    /**
     * 加载商品分类失败
     */
    private Handler loadCateFailHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            isLoadCategory = true;
            isError = true;
            errorMsg = "产品获得而失败";
            checkLoad();

        }
    };


    /**
     * 商品和商品分类加载成功或失败都发送给同一个handler---loadSuccHandler
     */
    private void checkLoad(){


        /**
         * 给持仓加载Handler发送消息
         */
        if (loadPositionHandler != null){
            Message msg = new Message();
            msg.obj = errorMsg;
            msg.arg1 = isError ? -1:1;
            loadPositionHandler.sendMessage(msg);
        }


        if(isLoadCategory && isLoadProduct && isLoadPositionProduct){//商品分类,商品持仓,商品信息全部加载完毕,再发送消息
            if(loadSuccHandler != null){
                Message msg = new Message();
                msg.obj = errorMsg;
                msg.arg1 = isError ? -1:1;
                loadSuccHandler.sendMessage(msg);
            }
        }
    }
}
