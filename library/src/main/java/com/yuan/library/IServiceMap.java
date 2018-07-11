package com.yuan.library;




import com.yuan.library.base.BaseResult;

import java.io.Serializable;

/**
 * Created by shucheng.qu on 2017/10/13.
 */

public interface IServiceMap extends Serializable {


    String getHostUrl();

    String getDescUrl();

    String getTypeCode();

    Class<? extends BaseResult> getClazz();

    RequestType getRequestType();

    public enum RequestType {//请求方式
        GET, POSTJSON, POSTFORM, POSTFILE
    }

}
