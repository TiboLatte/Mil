package com.tibolatte.milbadge.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge

@Composable
fun AnimatedProgressRing(
    badge: Badge,
    size: Float = 100f,
    strokeWidth: Float = 8f,
    progressFraction: Float // <-- fraction animée ici
) {
    Box(modifier = Modifier.size(size.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFFE18AAA),
                startAngle = -90f,
                sweepAngle = 360f * progressFraction, // <-- fraction animée
                useCenter = false,
                style = Stroke(width = strokeWidth.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}