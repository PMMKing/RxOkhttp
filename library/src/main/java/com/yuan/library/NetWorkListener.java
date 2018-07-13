package com.yuan.library;

/**
 * Created by shucheng.qu on 2017/5/26.
 */

public interface NetWorkListener {

    void onNetStart(NetWorkParam param);

    void onNetSuccess(NetWorkParam param);

    void onNetCancel(NetWorkParam param);

    void onNetFailed(NetWorkParam param);

    void onNetEnd(NetWorkParam param);

    boolean onNetIntercept(NetWorkParam param);

}
