package com.confluence.ui

sealed interface ConversionStatus {
    data class Success(val message: String) : ConversionStatus
    data class Error(val message: String) : ConversionStatus
}