package com.yuan.library;

import com.yuan.library.base.BaseParam;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

/**
 * Created by shucheng.qu on 2017/5/26.
 */

public class QRequest {

    public static NetWorkParam startRequest(NetWorkListener netWorkListener, @NonNull IServiceMap serviceMap, final BaseParam param, RequestFeature... features) {
        return startRequest(netWorkListener, serviceMap, param, null, features);
    }

    public static NetWorkParam startRequest(NetWorkListener netWorkListener, @NonNull IServiceMap serviceMap, final BaseParam param, Serializable ext, RequestFeature... features) {
        NetWorkParam netWorkParam = getNetWorkParam(netWorkListener, serviceMap, param, ext, features);
        netWorkParam.startRequest();
        return netWorkParam;
    }

    public static NetWorkParam getNetWorkParam(NetWorkListener netWorkListener, @NonNull IServiceMap serviceMap, BaseParam baseParam, Serializable ext, RequestFeature... features) {
        NetWorkParam netWorkParam = new NetWorkParam(serviceMap, baseParam);
        netWorkParam.ext = ext;
        netWorkParam.netWorkListener = netWorkListener;
        netWorkParam.progressMessage = baseParam.progressMessage;
        if (features != null && features.length > 0) {
            for (RequestFeature f : features) {
                switch (f) {
                    case ADD_CANCELPRE:
                    case ADD_CANCELSAMET:
                    case ADD_INSERT2HEAD:
                    case ADD_ONORDER:
                        netWorkParam.addType = f.getCode();
                        break;
                    case BLOCK:
                        netWorkParam.block = true;
                        break;
                    case BLOCKAUTO:
                        netWorkParam.blockauto = true;
                        break;
                    case DISCANCELABLE:
                        netWorkParam.cancelAble = false;
                        break;
                    default:
                        break;
                }
            }
        }

        return netWorkParam;
    }

    public final static int NET_ADD_ONORDER = 0; // 顺序添加
    public final static int NET_ADD_INSERT2HEAD = NET_ADD_ONORDER + 1; // 添加到开头
    public final static int NET_ADD_CANCELPRE = NET_ADD_INSERT2HEAD + 1; // 清空之前的再添加
    public final static int NET_ADD_CANCELSAMET = NET_ADD_CANCELPRE + 1; // 取消之前相同的请求再添加


    public enum RequestFeature {
        BLOCK,
        BLOCKAUTO,//
        DISCANCELABLE, //
        ADD_ONORDER {
            public int getCode() {
                return NET_ADD_ONORDER;
            }
        }, //
        ADD_INSERT2HEAD {
            public int getCode() {
                return NET_ADD_INSERT2HEAD;
            }
        }, //
        ADD_CANCELPRE {
            public int getCode() {
                return NET_ADD_CANCELPRE;
            }
        }, //
        ADD_CANCELSAMET {
            public int getCode() {
                return NET_ADD_CANCELSAMET;
            }
        }, //
        ;

        public int getCode() {
            return -1;
        }
    }
}
