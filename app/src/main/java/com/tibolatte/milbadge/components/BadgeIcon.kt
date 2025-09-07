package com.tibolatte.milbadge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeType
import com.tibolatte.milbadge.ui.theme.Dore
import com.tibolatte.milbadge.ui.theme.GrisCadenas
@Composable
fun BadgeIcon(
    badge: Badge,
    sizeUnlocked: Dp = 52.dp,
    sizeLocked: Dp = 38.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .background(Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Pour les badges EVOLVE, on ajoute le suffixe du niveau actuel
        val badgeName = if (badge.type == BadgeType.EVOLVE) {
            "badge_${badge.id}_${badge.evolveLevel}" // +1 si evolveLevel commence à 0
        } else {
            "badge_${badge.id}"
        }

        val resourceId = context.resources.getIdentifier(
            badgeName,
            "drawable",
            context.packageName
        )

        if (resourceId != 0) {
            val colorFilter = if (!badge.isUnlocked)
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            else null

            androidx.compose.foundation.Image(
                painter = painterResource(resourceId),
                contentDescription = badge.name,
                colorFilter = colorFilter,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val icon = if (badge.isUnlocked) Icons.Rounded.Star else Icons.Rounded.Lock
            val tint = if (badge.isUnlocked) Dore else GrisCadenas
            val iconSize = if (badge.isUnlocked) sizeUnlocked else sizeLocked

            Icon(
                icon,
                contentDescription = badge.name,
                tint = tint,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

