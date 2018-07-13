package com.yuan.library;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;

/**
 * Created by shucheng.qu on 2017/10/13.
 */

public class NetWorkManage {

    private NetWorkManage() {
    }

    public static NetWorkManage singleInstance = new NetWorkManage();
    //等待栈
    private final LinkedList<NetWorkParam> waitSequence = new LinkedList<NetWorkParam>();
    //请求栈
    private final LinkedList<NetWorkParam> questSequence = new LinkedList<NetWorkParam>();

    public void addTask(NetWorkParam param) {
        boolean isRepeat = false;
        synchronized (this.waitSequence) {
            Iterator<NetWorkParam> listSequenceIterator = this.waitSequence.iterator();
            while (listSequenceIterator.hasNext()) {
                NetWorkParam tmp = listSequenceIterator.next();
                if (tmp.equals(param)) {
                    isRepeat = true;
                    break;
                }
            }

            synchronized (this.questSequence) {
                Iterator<NetWorkParam> questSequenceIterator = this.questSequence.iterator();
                while (questSequenceIterator.hasNext()) {
                    NetWorkParam tmp = questSequenceIterator.next();
                    if (tmp.equals(param)) {
                        isRepeat = true;
                        break;
                    }
                }
            }//拦截请求栈中重复请求
            //拦截重复请求
            if (isRepeat) {
                return;
            }

            switch (param.addType) {
                case QRequest.NET_ADD_ONORDER:
                    this.waitSequence.add(param);
                    break;
                case QRequest.NET_ADD_INSERT2HEAD:
                    this.waitSequence.add(0, param);
                    break;
                case QRequest.NET_ADD_CANCELPRE: {
                    Iterator<NetWorkParam> it = this.waitSequence.iterator();
                    while (it.hasNext()) {
                        NetWorkParam np = it.next();
                        if (param.key.getRequestType() == np.key.getRequestType() && np.cancelAble) {
                            it.remove();
                        }
                    }

                    synchronized (this.questSequence) {
                        for (NetWorkParam next : this.questSequence) {
                            next.cancel = next.cancelAble;
                        }
                    }

                    this.waitSequence.add(0, param);
                }
                break;
                case QRequest.NET_ADD_CANCELSAMET: {//
                    Iterator<NetWorkParam> it = this.waitSequence.iterator();
                    while (it.hasNext()) {
                        NetWorkParam np = it.next();
                        if (param.key == np.key && np.cancelAble) {
                            it.remove();
                        }
                    }
                    synchronized (this.questSequence) {
                        for (NetWorkParam next : this.questSequence) {
                            next.cancel = next.cancelAble && next.key == param.key;
                        }
                    }
                    this.waitSequence.add(param);

                }
                break;
                default:
                    break;
            }
        }
        checkTasks();
    }

    /**
     * start net listSequence
     */
    private void checkTasks() {
        if (this.waitSequence.size() == 0) return;
        synchronized (this.waitSequence) {
            Iterator<NetWorkParam> it = this.waitSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam np = it.next();
                this.questSequence.add(np);
                it.remove();
                startNet(np);
            }
        }
    }


    private void startNet(final NetWorkParam netWorkParam) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String json = doRequest(netWorkParam);
                if (TextUtils.isEmpty(json) && !netWorkParam.cancel) {
                    e.onError(new Throwable("data 为空"));
                } else {
                    e.onNext(json);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        if (netWorkParam.netWorkListener != null) {
                            netWorkParam.netWorkListener.onNetStart(netWorkParam);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        if (netWorkParam.cancel) {
                            removeCurrentTask(netWorkParam);
                            if (netWorkParam.netWorkListener != null) {
                                netWorkParam.netWorkListener.onNetCancel(netWorkParam);
                            }
                            return false;
                        }
                        return true;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String result) {
                        try {
                            netWorkParam.result = JSON.parseObject(result, netWorkParam.key.getClazz());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (netWorkParam.netWorkListener != null) {
                            if (!netWorkParam.netWorkListener.onNetIntercept(netWorkParam)) {
                                netWorkParam.netWorkListener.onNetSuccess(netWorkParam);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        removeCurrentTask(netWorkParam);
                        if (netWorkParam.netWorkListener != null) {
                            netWorkParam.netWorkListener.onNetFailed(netWorkParam);
                        }
                    }

                    @Override
                    public void onComplete() {
                        removeCurrentTask(netWorkParam);
                        if (netWorkParam.netWorkListener != null) {
                            netWorkParam.netWorkListener.onNetEnd(netWorkParam);
                        }
                    }
                });

    }

    private String doRequest(NetWorkParam netWorkParam) {
        try {
            IServiceMap.RequestType requestType = netWorkParam.key.getRequestType();
            Request request = null;
            switch (requestType) {
                case GET:
                    request = requestGet(netWorkParam);
                    break;
                case POSTJSON:
                    request = requestPostGson(netWorkParam);
                    break;
                case POSTFORM:
                    request = requestPostForm(netWorkParam);
                    break;
                case POSTFILE:
                    request = requestPostFile(netWorkParam);
                    break;
            }
            if (request == null) return "";
            if (netWorkParam.cancel) return "";
            Call call = OkhttpClient.okClient.newCall(request);
            Response response = call.execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            HttpLogUtils.e("rxokhttp", e.toString());
            return "";
        }
    }


    /**
     * okHttp get同步请求
     *
     * @param
     */
    private Request requestGet(NetWorkParam param) {
        if (param == null || param.param == null || param.param.toHashMap() == null)
            return null;
        HashMap<String, String> paramsMap = param.param.toHashMap();
        StringBuilder tempParams = new StringBuilder();
        //处理参数
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                tempParams.append("&");
            }
            tempParams.append(String.format("%s=%s", key, paramsMap.get(key)));
            pos++;
        }
        String requestUrl = "";
        if (param.param.toHashMap().isEmpty()) {
            requestUrl = String.format("%s%s", param.hostUrl, param.descUrl);
        } else {
            requestUrl = String.format("%s%s?%s", param.hostUrl, param.descUrl, tempParams.toString());
        }
        //补全请求地址
        return new Request.Builder()
                .url(requestUrl)
                .headers(getHeaders(param))
                .build();
    }

    /**
     * post json 提交
     *
     * @param param
     * @return
     */
    private Request requestPostGson(NetWorkParam param) {
        if (param == null || param.param == null) return null;
        String json = JSON.toJSONString(param.param);
//        param.param.packetHead.sign = KeyUtils.getSign(json);
        json = JSON.toJSONString(param.param);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(param.hostUrl + param.descUrl)
                .post(requestBody)
                .headers(getHeaders(param))
                .build();
    }

    /**
     * post from提交
     *
     * @param
     * @return
     */
    private Request requestPostForm(NetWorkParam param) {
        if (param == null || param.param == null || param.param.toHashMap() == null || param.param.toHashMap().isEmpty())
            return null;
        HashMap<String, String> paramsMap = param.param.toHashMap();
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        //生成表单实体对象
        RequestBody formBody = builder.build();
        return new Request.Builder()
                .url(param.hostUrl + param.descUrl)
                .post(formBody)
                .headers(getHeaders(param))
                .build();
    }

    /**
     * post 文件提交
     *
     * @param param
     * @return
     */
    private Request requestPostFile(NetWorkParam param) {
        if (param == null || TextUtils.isEmpty(param.filePath)) return null;
        File file = new File(param.filePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(file.getName(), file.getName(), RequestBody.create(param.mediaType, file))
                .build();
        return new Request.Builder()
                .url(param.hostUrl + param.descUrl)
                .post(requestBody)
                .headers(getHeaders(param))
                .build();
    }

    private Headers getHeaders(NetWorkParam param) {
        HashMap<String, String> headers = param.key.getHeaders();
        Headers.Builder builder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return builder.build();
        for (String key : headers.keySet()) {
            builder.add(key, headers.get(key));
        }
        return builder.build();
    }


    /**
     * 请求取消或者请求完成移除请求栈中的请求
     *
     * @param param
     */
    private void removeCurrentTask(NetWorkParam param) {
        synchronized (this.questSequence) {
            Iterator<NetWorkParam> iterator = this.questSequence.iterator();
            while (iterator.hasNext()) {
                NetWorkParam next = iterator.next();
                if (next.equals(param)) {
                    iterator.remove();
                }
            }
        }
    }

    public void cancelRequestByListener(NetWorkListener listener) {
        synchronized (this.waitSequence) {
            Iterator<NetWorkParam> it = this.waitSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam param = it.next();
                if (param.netWorkListener == listener && param.cancelAble) {
                    it.remove();
                }
            }
        }

        synchronized (this.questSequence) {
            Iterator<NetWorkParam> it = this.questSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam param = it.next();
                if (param.netWorkListener == listener) {
                    param.cancel = param.cancelAble;
                }
            }
        }
    }

    public void cancelRequestByParam(NetWorkParam param) {
        synchronized (this.waitSequence) {
            Iterator<NetWorkParam> it = this.waitSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam temp = it.next();
                if (temp.equals(param) && temp.cancelAble) {
                    it.remove();
                }
            }
        }

        synchronized (this.questSequence) {
            Iterator<NetWorkParam> it = this.questSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam temp = it.next();
                if (temp.equals(param)) {
                    param.cancel = temp.cancelAble;
                }
            }
        }
    }

    public void cancelRequestByKey(IServiceMap serviceMap) {
        synchronized (this.waitSequence) {
            Iterator<NetWorkParam> it = this.waitSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam temp = it.next();
                if (temp.key == serviceMap && temp.cancelAble) {
                    it.remove();
                }
            }
        }
        synchronized (this.questSequence) {
            Iterator<NetWorkParam> it = this.questSequence.iterator();
            while (it.hasNext()) {
                NetWorkParam temp = it.next();
                if (temp.key == serviceMap) {
                    temp.cancel = temp.cancelAble;
                }
            }
        }
    }

}
