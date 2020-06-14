package com.julkar.nain.currencyconverter.service.Communicator

import io.reactivex.Observable

/**
 * @author Julkar Nain
 * @since 14 Jun 2020
 */
interface Communicator {
    fun emit(event: Any)
    fun <T> observe(eventType: Class<T>): Observable<T>
    fun <T> observeList(eventType: Class<List<T>>): Observable<List<T>>
}