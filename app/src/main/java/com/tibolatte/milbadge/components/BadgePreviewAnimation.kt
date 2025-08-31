package com.tibolatte.milbadge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge

@Composable
fun BadgePreviewAnimation(badge: Badge) {
    Box(
        Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(24.dp)
        ) {
            Text(badge.name, style = MaterialTheme.typography.headlineMedium)
            badge.progress?.let { (current, total) ->
                Text("$current / $total", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(12.dp))
            Text(badge.message ?: "Mon amour, tu brilles ✨", style = MaterialTheme.typography.bodyMedium)
        }
    }
}