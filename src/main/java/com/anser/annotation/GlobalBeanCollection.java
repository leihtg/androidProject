package com.anser.annotation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;

import com.anser.contant.MsgType;
import com.anser.contant.ReceiveData;
import com.anser.enums.ActionType;
import com.anser.enums.ScopeType;
import com.anser.model.FileQueryModel_in;
import com.anser.model.base.ModelInBase;
import com.anser.model.base.ModelOutBase;
import com.google.gson.Gson;

/**
 * Bean收集器
 *
 * @author lht
 * @time 2018年1月19日 下午8:55:52
 */
public class GlobalBeanCollection {
    private static GlobalBeanCollection singleton = new GlobalBeanCollection();
    // 业务所在的包
    private static final String basePackage = "com.anser.business";

    public static GlobalBeanCollection getInstance() {
        return singleton;
    }

    public static void main(String[] args) {

    }

    private GlobalBeanCollection() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            String path = basePackage.replace(".", File.separator);
            Enumeration<URL> res = classLoader.getResources(path);
            while (res.hasMoreElements()) {
                URL url = res.nextElement();
                String type = url.getProtocol();
                if ("file".equals(type)) {
                    loadClassFile(classLoader, path, URLDecoder.decode(url.getPath(), "UTF-8"));
                } else if ("jar".equals(type)) {
                    // to do
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    HashMap<Class<?>, ScopeType> scopMap = new HashMap<>();
    HashMap<Method, Object> cachedSinton = new HashMap<>();
    HashMap<ActionType, Method> busiMap = new HashMap<>();

    public ModelOutBase invokeBusi(ActionType mt, ReceiveData rd) {
        if (null == mt) {
            throw new RuntimeException("ActionType can not null");
        }
        Method method = busiMap.get(mt);
        if (null == method) {
            throw new RuntimeException("no method for ActionType:" + mt);
        }
        Object obj = cachedSinton.get(method);
        if (null == obj) {
            Class<?> clzz = method.getDeclaringClass();
            ScopeType st = scopMap.get(clzz);
            try {
                if (null != st) {
                    obj = clzz.newInstance();
                    if (st == ScopeType.singleton) {
                        cachedSinton.put(method, obj);// 缓存起来
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("必须提供一个默认的构造函数");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        try {
            return (ModelOutBase) method.invoke(obj, rd);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            ModelInBase modelInBase = new Gson().fromJson(rd.data, ModelInBase.class);
            ModelOutBase modelOutBase = new ModelOutBase();
            rd.msgType = MsgType.ERROR;
            rd.data = e.getMessage();
            String uuid = modelInBase.getUuid();

            modelOutBase.setUuid(uuid);
            modelOutBase.setData(rd);
            return modelOutBase;
        }
    }

    private void loadClassFile(ClassLoader loader, String basePath, String path) {
        try {
            File file = new File(path);

            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    loadClassFile(loader, basePath, f.getPath());
                }
            } else {
                String p = file.getPath();
                if (!p.endsWith(".class")) {
                    return;
                }
                int index = p.indexOf(basePath);
                if (index == -1) {
                    return;
                }
                path = p.substring(index);
                path = path.replaceAll("\\\\", ".");
                path = path.substring(0, path.length() - 6);

                Class<?> clazz = loader.loadClass(path);
                Scope sc = clazz.getAnnotation(Scope.class);

                if (null == sc || sc.value() == ScopeType.singleton) {
                    // 默认都是单例的
                    scopMap.put(clazz, ScopeType.singleton);
                } else {
                    scopMap.put(clazz, ScopeType.prototype);
                }
                System.out.println("load " + clazz);
                Method[] ms = clazz.getMethods();
                for (Method m : ms) {
                    BusinessType ba = m.getAnnotation(BusinessType.class);
                    if (null == ba) {
                        continue;
                    }
                    ActionType mt = ba.value();
                    Method method = busiMap.get(mt);
                    if (null != method) {
                        throw new RuntimeException("can not bind same ActionType:[" + mt + "] in method:[" + m
                                + "] and method:[" + method + "]");
                    }
                    if (!ModelOutBase.class.isAssignableFrom(m.getReturnType())) {
                        throw new RuntimeException("method:" + m + ",must return dataType{" + ModelOutBase.class + "]");
                    }
                    busiMap.put(mt, m);
                    System.out.println("load method: " + m);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
