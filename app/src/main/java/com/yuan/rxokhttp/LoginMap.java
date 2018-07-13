package com.yuan.rxokhttp;

import android.view.View;

import com.yuan.library.IServiceMap;
import com.yuan.library.base.BaseResult;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by shucheng.qu on 2018/7/11
 */
public class LoginMap implements IServiceMap {

    @Override
    public String getHostUrl() {
        return "http://route.showapi.com/";
    }

    @Override
    public String getDescUrl() {
        return "863-1";
    }

    @Override
    public Class<? extends BaseResult> getClazz() {
        return BaseResult.class;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.POSTFORM;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return null;
    }
}
