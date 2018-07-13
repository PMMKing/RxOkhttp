package com.yuan.rxokhttp;

import com.yuan.library.IServiceMap;
import com.yuan.library.base.BaseResult;

import java.util.HashMap;

/**
 * Created by shucheng.qu on 2018/7/11
 */
public enum ServiceMap implements IServiceMap {
    QQ("863-1", BaseResult.class, RequestType.POSTFORM),
    TIM("863-1", BaseResult.class, RequestType.GET);

    private final String hostUrl;
    private final String descUrl;
    private final Class<? extends BaseResult> classz;
    private final RequestType requestType;

    ServiceMap(String descUrl, Class<? extends BaseResult> classz) {
        this(descUrl, classz, RequestType.POSTJSON);
    }

    ServiceMap(String descUrl, Class<? extends BaseResult> classz, RequestType requestType) {
        this("http://route.showapi.com/", descUrl, classz, requestType);
    }

    ServiceMap(String hostUrl, String descUrl, Class<? extends BaseResult> classz, RequestType requestType) {
        this.hostUrl = hostUrl;
        this.descUrl = descUrl;
        this.classz = classz;
        this.requestType = requestType;
    }

    @Override
    public String getHostUrl() {
        return hostUrl;
    }

    @Override
    public String getDescUrl() {
        return descUrl;
    }

    @Override
    public Class<? extends BaseResult> getClazz() {
        return classz;
    }

    @Override
    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", "1233234567890");
        hashMap.put("llp", "jkehwfukejwfbkjewurkw");
        return hashMap;
    }

}
