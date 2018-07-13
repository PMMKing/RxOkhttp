package com.yuan.library;

import com.yuan.library.base.BaseResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by shucheng.qu on 2017/10/13.
 */

public interface IServiceMap extends Serializable {

    String getHostUrl();

    String getDescUrl();

    Class<? extends BaseResult> getClazz();

    RequestType getRequestType();

    HashMap<String, String> getHeaders();

    enum RequestType {//请求方式
        GET, POSTJSON, POSTFORM, POSTFILE
    }

}
