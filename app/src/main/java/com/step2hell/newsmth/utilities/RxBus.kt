package com.step2hell.newsmth.utilities

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor

class RxBus private constructor() {

    private val processor = PublishProcessor.create<Any>().toSerialized()
    private val map = HashMap<String, CompositeDisposable>()

    fun <T : Any> register(t: T, d: Disposable) {
        val key = t::class.java.name
        if (map[key] == null) {
            map[key] = CompositeDisposable()
        }
        map[key]!!.add(d)
    }

    fun <T : Any> unregister(t: T) {
        val key = t::class.java.name
        if (map[key] != null) {
            map[key]!!.dispose()
            map.remove(key)
        }
    }

    fun <T> publish(t: T) = processor.onNext(t)

    fun <T> listen(eventType: Class<T>) = processor.ofType(eventType)


    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = RxBus()
    }

}