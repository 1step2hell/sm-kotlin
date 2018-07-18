package com.step2hell.newsmth.utilities

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class RxBus private constructor() {

    private val processor = PublishProcessor.create<Any>().toSerialized()
    private val map = HashMap<String, CompositeDisposable>()

    fun register(a: Any, d: Disposable) {
        val key = a::class.java.name
        if (map[key] == null) {
            map[key] = CompositeDisposable()
        }
        map[key]!!.add(d)
    }

    fun unregister(a: Any) {
        val key = a::class.java.name
        if (map[key] != null) {
            map[key]!!.dispose()
            map.remove(key)
        }
    }

    fun <T> publish(t: T) {
        processor.onNext(t)
    }

    fun <T> listen(eventType: Class<T>): Flowable<T> {
        return processor.ofType(eventType)
    }


    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = RxBus()
    }

}