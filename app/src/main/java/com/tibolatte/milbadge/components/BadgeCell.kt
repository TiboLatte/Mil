package com.tibolatte.milbadge.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeType


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
            .fillMaxWidth(0.2f)
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onPress = { tryAwaitRelease(); onLongPressEnd() },
                    onLongPress = { onLongPressStart() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Si le badge n'est pas débloqué, afficher le ring
        if (!badge.isUnlocked && badge.type == BadgeType.PROGRESSIVE) {
            ProgressRing(
                badge = badge,
                size = 64f,      // ajuster selon la taille de l'icône
                strokeWidth = 6f // épaisseur du ring
            )
        }

        // Icône du badge
        BadgeIcon(
            badge = badge,
            modifier = Modifier.fillMaxSize(0.8f) // laisse un peu d'espace pour le ring
        )
    }
}