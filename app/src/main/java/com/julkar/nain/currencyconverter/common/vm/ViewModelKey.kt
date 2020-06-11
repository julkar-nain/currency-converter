package com.julkar.nain.currencyconverter.common.vm

import androidx.lifecycle.ViewModel
import dagger.MapKey
import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy
import javax.xml.transform.OutputKeys.METHOD
import kotlin.reflect.KClass

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Suppress("DEPRECATED_JAVA_ANNOTATION")
@Documented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)