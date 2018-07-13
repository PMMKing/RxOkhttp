package com.yuan.library;

import android.content.Intent;

import com.yuan.library.base.BaseParam;
import com.yuan.library.base.BaseResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;

/**
 * Created by shucheng.qu on 2017/10/13.
 */

public class NetWorkParam implements Serializable {

    public IServiceMap key;
    public String hostUrl = "";
    public String descUrl = "";
    public BaseParam param;
    public BaseResult result = new BaseResult();
    public String filePath;
    public MediaType mediaType = MediaType.parse("image/png");
    /**
     * 本地用的参数
     */
    public boolean block = false;
    public boolean blockauto = false;
    public boolean cancelAble = true;
    public boolean cancel = false;
    public int addType = QRequest.NET_ADD_ONORDER;

    public String progressMessage = "";
    public Serializable ext;
    public NetWorkListener netWorkListener;


    NetWorkParam(IServiceMap key, BaseParam param) {
        this.key = key;
        this.param = param != null ? param : new BaseParam();
        hostUrl = key.getHostUrl();
        descUrl = key.getDescUrl();
    }

    /**
     * 请求结果拦截
     *
     * @param netWorkParam
     * @return
     */
    public boolean intercept(NetWorkParam netWorkParam) {
//        if (netWorkParam.result.code == 9002) {//重新登录
//            Intent intent = new Intent();
//            intent.setClass(MainApplication.getContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            MainApplication.getContext().startActivity(intent);
//            return true;
//        }
        return false;
    }

    public NetWorkParam startRequest() {
        NetWorkManage.singleInstance.addTask(this);
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.key == null ? 0 : this.key.hashCode());
        result = prime * result + (this.param == null ? 0 : this.param.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NetWorkParam other = (NetWorkParam) obj;
        if (this.key != other.key) {
            return false;
        }
        if (this.param == null) {
            if (other.param != null) {
                return false;
            }
        } else if (!this.param.equals(other.param)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("NetworkParam [key=%s, param=%s]", this.key, this.param);
    }

}
