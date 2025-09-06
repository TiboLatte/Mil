package com.tibolatte.milbadge.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.tibolatte.milbadge.Badge
import kotlinx.coroutines.launch

@Composable
fun ProgressRing(badge: Badge, size: Float = 100f, strokeWidth: Float = 8f) {
    val progressFraction = (badge.progress?.first ?: 0) / (badge.progress?.second ?: 1).toFloat()

    Box(modifier = Modifier.size(size.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Cercle de fond gris
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.dp.toPx(), cap = StrokeCap.Round)
            )

            // Cercle de progression vert
            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = 360f * progressFraction,
                useCenter = false,
                style = Stroke(width = strokeWidth.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}