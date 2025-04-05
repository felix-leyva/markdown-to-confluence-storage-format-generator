package com.confluence.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollablePreviewText(text: String) {
    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.Companion.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Companion.Start
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.Companion.padding(8.dp),
                maxLines = 500
            )
        }
    }
}