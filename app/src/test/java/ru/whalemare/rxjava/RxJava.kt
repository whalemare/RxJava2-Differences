package ru.whalemare.rxjava

import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Observable
import rx.functions.Action0
import rx.functions.Action1

/**
 * @since 2017
 * @author Anton Vlasov - whalemare
 */
@RunWith(JUnit4::class)
open class RxJava {

    @Test
    fun testNPE() {
        try {
            Observable.just(null)
                    .subscribe({
                        print(it)
                    })
        } catch (e: NullPointerException) {
            // this block not call
            Assert.assertFalse(true)
        }
    }

    @Test
    @Suppress("RedundantSamConstructor")
    fun testSubscriberFun() {
        Observable.range(0, 10)
                .subscribe(
                        Action1 { print("\nonNext $it") },
                        Action1 { print("\nonError $it") },
                        Action0 { print("\nonCompleted") }
                )
    }

    @Suppress("RedundantSamConstructor")
    fun simulateRestApi() {
        getMyRestApiData()
                .subscribe(
                        Action1 { print("\nonNext $it") },
                        Action1 { print("\nonError $it") },
                        Action0 { print("\nonCompleted") }
                )
    }

    fun getMyRestApiData() = Observable.just("")


}