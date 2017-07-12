package ru.whalemare.rxjava2

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.Callable

/**
 * @since 2017
 * @author Anton Vlasov - whalemare
 */
@RunWith(JUnit4::class)
open class RxJava2 {

    @Test
    fun testNPE() {
        try {
            Observable.just(null)
            Single.just(null)
            Flowable.just(null)
        } catch (e: Exception) {
            Assert.assertTrue(e is NullPointerException)
        }
    }

    @Test
    fun testNPE2() {
        val list = mutableListOf("One", "Two", "Three", null, "This is not printed")
        var throwedExceptions = false
        Observable.fromIterable<String>(list)
                .subscribe({ string ->
                    println(string)
                }, { error ->
                    throwedExceptions = true
                    println(error)
                })

        Assert.assertTrue(throwedExceptions)
    }

    @Test
    fun testReduce() {
        Observable.range(0, 20)
                .reduce { new, old ->
                    new + old
                }
                .subscribe({
                    println(it)
                })
    }

    @Test
    @Suppress("RedundantSamConstructor")
    fun testSubscriberFun() {
        // callbacks on Observer
        Observable.range(0, 10)
                .subscribe(
                        Consumer { print("\nonNext $it") },
                        Consumer { print("\nonError $it") },
                        Action { print("\nonComplete") },
                        Consumer { print("\nonSubscribe $it") }
                )

        // callbacks on Subscriber
        Flowable.range(0, 10)
                .subscribe(
                        Consumer { print("\nonNext $it") },
                        Consumer { print("\nonError $it") },
                        Action { print("\nonComplete") }
//                        Consumer { print("\nonSubscribe ${it.request(9)}") }
                )
    }

    @Test
    fun testSingle() {
        val string = "string"
        Single.create<String> {
            it.onSuccess(string)
        }.subscribe({
            print("\nonSuccess $it")
        }, {
            print("\nonError $it")
        })

        val emptyString = " "
        Single.create<String> {
            if (string.isNullOrBlank()) {
                it.onError(NullPointerException())
            } else {
                it.onSuccess(emptyString)
            }
        }.subscribe({
            print("\nonSuccess $it")
            Assert.assertTrue(false)
        }, {
            print("\nonError $it")
            assert(it is NullPointerException)
        })
    }

    @Test
    fun testCallable() {
        // can execute
        Single.fromCallable(Callable {
            return@Callable "string"
        }).subscribe({
            print("\nonSuccess $it")
        }, {
            print("\nonError $it")
            Assert.assertTrue(false)
        })
    }

    fun sampleSchedulers() {
        // .map() следует по возможности определять до вызова метода переключения потоков на главный
        Observable.range(0, 10)
                .map {
                    Thread.sleep(100)
                    print(Thread.currentThread())
                    print("map ${Thread.currentThread()}")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    print("onNext ${Thread.currentThread()}")
                })
    }

    @Suppress("RedundantSamConstructor")
    fun simulateRestApi() {
        getMyRestApiData()
                .subscribe({ data ->
                    println("onNext: $data")
                }, { error ->
                    print("Oh fck, error: $error")
                })
    }

    fun getMyRestApiData() = Single.just("")

}
