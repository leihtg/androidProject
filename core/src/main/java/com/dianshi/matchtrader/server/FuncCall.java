package com.dianshi.matchtrader.server;

import android.os.Handler;
import android.os.Message;

import com.dianshi.framework.ReceiveData;
import com.dianshi.matchtrader.Utils.CommonUtil;
import com.dianshi.matchtrader.Utils.Util;
import com.dianshi.matchtrader.model.ErrorModel_out;
import com.dianshi.matchtrader.model.ModelInBase;
import com.dianshi.matchtrader.model.ModelOutBase;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 函数访问
 */
public class FuncCall<IN extends ModelInBase, OUT extends ModelOutBase> {
    public Handler FuncResultHandler;
    public Handler FuncErrHandler;
    private GlobalSingleton globalSingleton = GlobalSingleton.CreateInstance();
    private String timestamp;
    private Class<OUT> tClass;


    /**
     * 访问函数
     *
     * @param controller
     * @param model_in
     * @param tClass
     * @param errorMsgDictionary
     * @return
     */
    public String Call(String controller, IN model_in, Class<OUT> tClass, ConcurrentHashMap<String, String> errorMsgDictionary) {
        String result = "";
        this.tClass = tClass;
        timestamp = UUID.randomUUID().toString();
        String hashKey = globalSingleton.SignStr;
        if (globalSingleton.CustomerId == 0) {
            hashKey = globalSingleton.PasswordMD5;
        }


        model_in.setSys_Controller(controller);
        model_in.setSys_Timestamp(timestamp.replace("T"," "));
        model_in.setSys_UserName(globalSingleton.UserName);
        model_in.setSys_CustomerId(globalSingleton.CustomerId);
        model_in.setSys_Key(hashKey);

        ConcurrentHashMap<String, String> paramMap = new ConcurrentHashMap<String, String>();
        paramMap = SplitParam(model_in, hashKey);

        String postData = Util.DictionaryToUrl(paramMap);



        try {
            globalSingleton.getTCPSingleton().FuncSend(postData, timestamp, ReceiveHandler);
        } catch (Exception e) {
            result = "error";
        }

        return result;
    }


    /**
     * 拼接字符串，以便发送给服务端
     *
     * @param model_in
     * @param hashKey
     * @return
     */
    private ConcurrentHashMap<String, String> SplitParam(IN model_in, String hashKey) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        //java的反射
        // Class userCla = (Class) bean.getClass();
        StringBuilder myBuilder = new StringBuilder();

        /*
        获取全部字段,包括私有和受保护的Field
        这样做的原因是getFields()只能获取public类型,包括父类的
        getDeclaredFields() 能获取public和nonPublic,但是不包括父类的
        但是我们大多都有一个基类要去继承,我们需要去获取父类的东西
        但是object类中的属性就不要获取了,添加到加密串中会导致验证失败
        同时可以看出请求数据的Model不要有多余的非空值的字段,否则会导致加密串验证失败
         */

        List<Field> fieldList = new ArrayList<Field>();
        Class tmpClass = model_in.getClass();
        while (tmpClass !=null && !tmpClass.getName().toLowerCase().equals("java.lang.object") ) {
            fieldList.addAll(Arrays.asList(tmpClass .getDeclaredFields()));
            tmpClass = tmpClass .getSuperclass();
        }


        for (int i = 0; i < fieldList.size(); i++) {
            try {
                Field f = fieldList.get(i);
                f.setAccessible(true);
                Object val = f.get(model_in);
                String name = f.getName();

                if(val == null || val.toString().equals(""))
                {
                    continue;
                }


                //将值非空的键名值对对放入map中
                if (name.compareTo("Sys_Key") != 0) {

                    try {
                        if(!name.equals("serialVersionUID")){
                            map.put(f.getName(), val.toString());
                        }
                    }catch (Exception ex){

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        将map进行排列，这一步尤其重要，因为map的排列存储是无序的
        在不同的android系统上通过反射取出的值也是不同的，这样造成的结果是拼接出的字符串不一致
        和服务端不匹配，会无法正常访问接口
         */
        Map<String, String> map1 = CommonUtil.sortMapByKey(map);

        for (String key : map1.keySet()) {

            myBuilder.append(map1.get(key));
            myBuilder.append("#");
        }


        myBuilder.append(hashKey);
        String hashedStr = Util.MD5(myBuilder.toString());
        map.put("Sys_Key", hashedStr);


        return map;
    }

    /**
     * 信息接收handler
     */
    private Handler ReceiveHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            boolean isError = false;
            ReceiveData data = (ReceiveData) message.obj;
            try {

                ErrorModel_out errorModel_out = new Gson().fromJson(data.Data, ErrorModel_out.class);

                    /*
                     * 信息接收失败
                     */
                if (FuncErrHandler != null && errorModel_out != null && errorModel_out.getErrorMsg() != null) {

                    Message errMsg = new Message();
                    errMsg.obj = errorModel_out;

                    FuncErrHandler.sendMessage(errMsg);
                    isError = true;
                } else {
                    isError = false;
                }
            } catch (Exception e) {
                isError = false;
            }
            if (!isError) {
                try {


                    /*
                     * 信息接收成功
                     */

                    OUT out = new Gson().fromJson(data.Data, tClass);

                    String outTimeStamp = out.getTimestamp();
                    if (FuncResultHandler != null) {
                        Message sucMsg = new Message();
                        sucMsg.obj = out;
                        FuncResultHandler.sendMessage(sucMsg);
                    }
                } catch (Exception ex) {

                    ErrorModel_out errorModel_out = new ErrorModel_out();
                    errorModel_out.setErrorCode("001");
                    errorModel_out.setErrorMsg("解析结果出错!");
                    Message errMsg = new Message();
                    errMsg.obj = errorModel_out;
                    FuncErrHandler.sendMessage(errMsg);
                    ex.printStackTrace();
                }
            }
        }
    };
}
