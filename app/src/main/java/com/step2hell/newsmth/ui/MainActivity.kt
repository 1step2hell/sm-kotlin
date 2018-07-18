package com.step2hell.newsmth.ui

import android.os.Bundle
import android.util.Log
import com.step2hell.newsmth.R
import com.step2hell.newsmth.data.remote.ApiServiceHelper
import com.step2hell.newsmth.data.remote.service.NewsmthService
import com.step2hell.newsmth.utilities.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello.setText("haha")

        ApiServiceHelper.INSTANCE
                .createService(NewsmthService.BASE_URL, NewsmthService::class.java)
                .getIndexHtml()
                .subscribeOn(Schedulers.newThread())
//                .map(Function {  }) // do map here
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer<ResponseBody> { responseBody -> Log.e("Bob", "ResponseBody:" + responseBody.string()) })

        val disposable = RxBus.getInstance()
                .listen(Integer::class.java)
                .observeOn(Schedulers.newThread())
                .subscribe(Consumer<Integer> { integer -> Log.e("Bob", "Thread:" + Thread.currentThread() + ", MainActivity listen:" + integer) })
        RxBus.getInstance().register(this, disposable)
        RxBus.getInstance().publish(2)

        val disposable1 = RxBus.getInstance()
                .listen(Haha::class.java)
                .observeOn(Schedulers.newThread())
                .subscribe(Consumer<Haha> { integer -> Log.e("Bob", "Thread:" + Thread.currentThread() + ", MainActivity listen:" + integer) })
        RxBus.getInstance().register(this, disposable1)
        RxBus.getInstance().publish(Haha())
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unregister(this)
    }

    class Haha constructor(){

    }
}
