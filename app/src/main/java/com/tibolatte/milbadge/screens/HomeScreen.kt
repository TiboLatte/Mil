package com.tibolatte.milbadge.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tibolatte.milbadge.components.BadgeCell
import com.tibolatte.milbadge.components.BadgePreviewAnimation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    var showUnlocked by remember { mutableStateOf<Boolean?>(null) }
    var previewBadgeId by remember { mutableStateOf<Int?>(null) }
    val badges by viewModel.badges.collectAsState()
    val previewBadge = previewBadgeId?.let { id -> badges.firstOrNull { it.id == id } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Header avec compteur
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mes badges", style = MaterialTheme.typography.headlineLarge)
            Box(
                modifier = Modifier.background(Color.Yellow, RoundedCornerShape(12.dp)).padding(12.dp, 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("${badges.count { it.isUnlocked }} / ${badges.size}")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Filtres
        val buttons = listOf("Débloqués" to true, "Non débloqués" to false, "Tous" to null)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            buttons.forEach { (label, value) ->
                Button(onClick = { showUnlocked = value }) { Text(label) }
            }
        }



        Spacer(Modifier.height(16.dp))

        // Liste des badges
        val filteredBadges = badges.filter { showUnlocked?.let { it1 -> it.isUnlocked == it1 } ?: true }
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f)) {
            items(filteredBadges, key = { it.id }) { badge ->
                BadgeCell(
                    badge = badge,
                    onClick = { navController.navigate("badgeDetail/${badge.id}") },
                    onLongPressStart = { previewBadgeId = badge.id },
                    onLongPressEnd = { previewBadgeId = null }
                )
            }
        }
    }

    // Overlay preview
    previewBadge?.let { BadgePreviewAnimation(it) }
}

