package com.confluence.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.confluence.ui.ConversionStatus

@Composable
fun StatusMessage(
    conversionStatus: ConversionStatus,
) {
    val (message, isError) = when (conversionStatus) {
        is ConversionStatus.Success -> conversionStatus.message to false
        is ConversionStatus.Error -> conversionStatus.message to true
    }

    Surface(
        color = if (isError) MaterialTheme.colors.error
        else MaterialTheme.colors.primary,
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        Text(
            text = message,
            modifier = Modifier.Companion.padding(16.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}