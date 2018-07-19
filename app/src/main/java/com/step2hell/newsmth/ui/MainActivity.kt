package com.step2hell.newsmth.ui

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.step2hell.newsmth.R
import com.step2hell.newsmth.data.remote.ApiServiceHelper
import com.step2hell.newsmth.data.remote.dto.AdRsp
import com.step2hell.newsmth.data.remote.service.NewsmthService
import com.step2hell.newsmth.utilities.HtmlUtils
import com.step2hell.newsmth.utilities.RxBus
import com.step2hell.newsmth.widgets.AdDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello.text = "haha"

        ApiServiceHelper.INSTANCE
                .createService(NewsmthService.BASE_URL, NewsmthService::class.java)
                .getIndexHtml()
                .subscribeOn(Schedulers.newThread())
                .map { body -> Gson().fromJson(HtmlUtils.getSubSample(body.string(), "preimg=\\[(.*?)\\]"), AdRsp::class.java) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adRsp ->
                    Log.e("Bob", "AdRsp:" + adRsp.toString());
                    val dialog = AdDialog(this)
                    dialog.show()
                    dialog.setAd(adRsp)
                }

        val disposable = RxBus.getInstance()
                .listen(Integer::class.java)
                .observeOn(Schedulers.newThread())
                .subscribe(Consumer<Integer> { i -> Log.e("Bob", "Thread:" + Thread.currentThread() + ", MainActivity listen:" + i) })
        RxBus.getInstance().register(this, disposable)
        RxBus.getInstance().publish(2)

        val disposable1 = RxBus.getInstance()
                .listen(Haha::class.java)
                .observeOn(Schedulers.newThread())
                .subscribe(Consumer<Haha> { haha -> Log.e("Bob", "Thread:" + Thread.currentThread() + ", MainActivity listen:" + haha.toString()) })
        RxBus.getInstance().register(this, disposable1)
        RxBus.getInstance().publish(Haha())

    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(this)
    }

    class Haha constructor() {

    }
}
