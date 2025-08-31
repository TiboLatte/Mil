package com.tibolatte.milbadge.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tibolatte.milbadge.Badge

@Composable
fun BadgeRow(badge: Badge, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), onClick = onClick, elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(badge.name)
            Text(badge.rarity.name)
        }
    }
}
