package com.yuan.rxokhttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.yuan.library.*
import com.yuan.library.base.BaseParam
import junit.framework.Test
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), NetWorkListener {
    override fun onNetSuccess(param: NetWorkParam?) {
        Log.d("netrequest", "成功" + param?.key.toString())
    }

    override fun onNetIntercept(param: NetWorkParam?): Boolean {
        return false
    }

    override fun onNetCancel(p0: NetWorkParam?) {
        Log.d("netrequest", "取消" + p0?.key.toString())
    }

    override fun onNetStart(p0: NetWorkParam?) {
        Log.d("netrequest", "开始" + p0?.key.toString())
    }

    override fun onNetEnd(p0: NetWorkParam?) {
        Log.d("netrequest", "结束" + p0?.key.toString())
    }

    override fun onNetFailed(p0: NetWorkParam?) {
        Log.d("netrequest", "失败" + p0?.key.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HttpLogUtils.debug = true
        tv.setOnClickListener {
            for (i in 0..1) {
                thread {
                    val request1 = QRequest.startRequest(this, ServiceMap.QQ, QQParam())
                    val request2 = QRequest.startRequest(this, ServiceMap.TIM, TIMParam())
                    NetWorkManage.singleInstance.cancelRequestByParam(request2)
                }
            }
        }
        QRequest.startRequest(this, LoginMap(), TIMParam())
    }
}
