package com.step2hell.newsmth.utilities

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor

class RxBus private constructor() {

    protected val processor = PublishProcessor.create<Any>().toSerialized()
    protected val map = HashMap<String, CompositeDisposable>()

    inline fun <reified A : Any> register(a: A, d: Disposable) {
        val key = a::class.java.name
        if (map[key] == null) {
            map[key] = CompositeDisposable()
        }
        map[key]!!.add(d)
    }

    inline fun <reified A : Any> unregister(a: A) {
        val key = a::class.java.name
        if (map[key] != null) {
            map[key]!!.dispose()
            map.remove(key)
        }
    }

    inline fun <reified T> publish(t: T) = processor.onNext(t)

    inline fun <reified T> listen(eventType: Class<T>) = processor.ofType(eventType)


    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = RxBus()
    }

}