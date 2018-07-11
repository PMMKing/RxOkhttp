package com.yuan.library.base;


import android.text.TextUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by shucheng.qu on 2017/5/26.
 */

public class BaseParam implements Serializable {

    public transient String progressMessage = "";

    public HashMap toHashMap() {
        Field[] declaredFields = this.getClass().getFields();
        HashMap<String, Object> hashMap = new HashMap<>();
        if (declaredFields != null && declaredFields.length > 0) {
            for (Field field : declaredFields) {
                if (field != null) {
                    try {
                        field.setAccessible(true);
                        String value = field.get(this).toString();
                        if (!TextUtils.equals(field.getName(), "path")) {
                            hashMap.put(field.getName(), value);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return hashMap;
    }

}