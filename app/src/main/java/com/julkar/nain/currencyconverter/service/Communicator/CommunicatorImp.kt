package com.julkar.nain.currencyconverter.service.Communicator

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Julkar Nain
 * @since 14 Jun 2020
 */
class CommunicatorImp : Communicator {

    private val publisher = PublishSubject.create<Any>()

    override fun emit(event: Any) {
        publisher.onNext(event)
    }

    override fun <T> observe(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
    override fun <T> observeList(eventType: Class<List<T>>): Observable<List<T>> =
        publisher.ofType(eventType)
}