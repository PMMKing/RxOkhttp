package com.yuan.library.base;


import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by shucheng.qu on 2017/5/26.
 */

public class BaseParam implements Serializable {

    public transient String progressMessage = "";

    public HashMap toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(this));
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet){
            hashMap.put(key, jsonObject.getString(key));
        }
//        Field[] declaredFields = this.getClass().getFields();
//
//        if (declaredFields != null && declaredFields.length > 0) {
//            for (Field field : declaredFields) {
//                if (field != null) {
//                    try {
//                        field.setAccessible(true);
//                        String value = field.get(this).toString();
//                        if (!TextUtils.equals(field.getName(), "path")) {
//                            hashMap.put(field.getName(), value);
//                        }
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
        return hashMap;
    }

}