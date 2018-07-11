package com.yuan.library;

/**
 * Created by shucheng.qu on 2017/5/26.
 */

public interface NetWorkListener {

    void onNetStart(NetWorkParam param);

    void onMsgSearchComplete(NetWorkParam param);

    void onNetCancel(NetWorkParam param);

    /**
     * 数据签名验证失败
     * @param param
     */
    void onNetFinish(NetWorkParam param);

    void onNetError(NetWorkParam param);

    void onNetFailed(NetWorkParam param);

    void onNetEnd(NetWorkParam param);

}
