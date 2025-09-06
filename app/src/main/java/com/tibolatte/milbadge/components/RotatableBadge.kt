package com.tibolatte.milbadge.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.tibolatte.milbadge.Badge
import kotlinx.coroutines.launch

@Composable
fun RotatableBadge(
    badge: Badge,
    sizeFraction: Float = 0.55f,
    maxRotation: Float = 80f,
    extraRotationY: Float = 0f // rotation supplémentaire externe
) {
    val density = LocalDensity.current.density
    val scope = rememberCoroutineScope()

    val rotationX = remember { Animatable(0f) }
    val rotationY = remember { Animatable(0f) }
    val sensitivity = 0.5f
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth(sizeFraction)
            .aspectRatio(1f)
            .onGloballyPositioned { boxSize = it.size }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            rotationY.snapTo(
                                (rotationY.value + dragAmount.x * sensitivity)
                                    .coerceIn(-maxRotation, maxRotation)
                            )
                            rotationX.snapTo(
                                (rotationX.value - dragAmount.y * sensitivity)
                                    .coerceIn(-maxRotation, maxRotation)
                            )
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val animSpec = tween<Float>(
                                durationMillis = 400,
                                easing = { OvershootInterpolator(2f).getInterpolation(it) }
                            )
                            launch { rotationX.animateTo(0f, animationSpec = animSpec) }
                            launch { rotationY.animateTo(0f, animationSpec = animSpec) }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            val animSpec = tween<Float>(
                                durationMillis = 400,
                                easing = { OvershootInterpolator(2f).getInterpolation(it) }
                            )
                            launch { rotationX.animateTo(0f, animationSpec = animSpec) }
                            launch { rotationY.animateTo(0f, animationSpec = animSpec) }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val width = boxSize.width.toFloat().coerceAtLeast(1f)
        val height = boxSize.height.toFloat().coerceAtLeast(1f)
        val maxOffset = 30f

        // Ombre projetée
        val shadowOffsetX = -rotationY.value / maxRotation * maxOffset
        val shadowOffsetY = rotationX.value / maxRotation * maxOffset
        val shadowScaleX = 1f - kotlin.math.abs(rotationY.value) / maxRotation * 0.5f
        val shadowScaleY = 1f - kotlin.math.abs(rotationX.value) / maxRotation * 0.5f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = shadowOffsetX
                    translationY = shadowOffsetY
                    scaleX = shadowScaleX
                    scaleY = shadowScaleY
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(width / 2f, height / 2f),
                        radius = (width.coerceAtLeast(height) * 0.5f).coerceAtLeast(1f)
                    ),
                    shape = CircleShape
                )
        )

        // Badge avec rotation + rotation externe
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    rotationX = rotationX.value,
                    rotationY = rotationY.value + extraRotationY,
                    cameraDistance = 8 * density
                ),
            contentAlignment = Alignment.Center
        ) {
            BadgeIcon(
                badge = badge,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val highlightOffsetX = -rotationY.value / maxRotation * width * 0.25f
                        val highlightOffsetY = -rotationX.value / maxRotation * height * 0.25f
                        translationX = highlightOffsetX
                        translationY = highlightOffsetY
                        scaleX = 1f
                        scaleY = 1f
                    }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            center = Offset(width / 2f, height / 2f),
                            radius = (width * 0.6f).coerceAtLeast(1f)
                        ),
                        shape = CircleShape
                    )
            )

            // Reflets secondaires
            listOf(0.2f, 0.4f, 0.6f).forEach { factor ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = rotationY.value / maxRotation * 8f * factor
                            translationY = rotationX.value / maxRotation * 8f * factor
                            scaleX = 0.6f * factor
                            scaleY = 0.15f * factor
                        }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.White.copy(alpha = 0.05f * factor), Color.Transparent),
                                center = Offset(width / 2f, height / 2f),
                                radius = (width * 0.4f * factor).coerceAtLeast(1f)
                            ),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}