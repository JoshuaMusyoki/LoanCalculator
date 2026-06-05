package com.coop_test.loan_calculator.domain.model

sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>
    object Loading : Resource<Nothing>
}