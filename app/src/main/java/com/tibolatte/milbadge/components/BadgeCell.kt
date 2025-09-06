package com.tibolatte.milbadge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge

@Composable
fun BadgeCell(
    badge: Badge,
    onClick: () -> Unit,
    onLongPressStart: () -> Unit = {},
    onLongPressEnd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(0.2f)   // 20% de la largeur parent
            .aspectRatio(1f)       // carré parfait
            .clip(CircleShape)
            .background(if (badge.isUnlocked) Transparent else Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onPress = { tryAwaitRelease(); onLongPressEnd() },
                    onLongPress = { onLongPressStart() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        BadgeIcon(
            badge = badge,
            modifier = Modifier.fillMaxSize() // prend tout l’espace du Box
        )
    }
}
