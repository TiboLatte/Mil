package com.tibolatte.milbadge.navigation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tibolatte.milbadge.ui.theme.TexteMarron

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Paramètres (placeholder)", style = MaterialTheme.typography.headlineSmall, color = TexteMarron)
    }
}