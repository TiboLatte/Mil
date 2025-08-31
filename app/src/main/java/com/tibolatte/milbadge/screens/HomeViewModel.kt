package com.tibolatte.milbadge.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _badges = MutableStateFlow<List<Badge>>(BadgeRepository.badges)
    val badges: StateFlow<List<Badge>> = _badges

    fun completeObjective(badgeId: Int, value: Int) {
        val updatedBadges = _badges.value.map { badge ->
            if (badge.id == badgeId) {
                val originalBadge = BadgeRepository.badges.firstOrNull { it.id == badgeId }
                val total = originalBadge?.progress?.second ?: (badge.progress?.second ?: 1)
                val current = value.coerceAtMost(total)

                badge.copy(
                    progress = current to total,
                    isUnlocked = current >= total,
                    unlockDate = if (current >= total) SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) else badge.unlockDate,
                    message = if (current >= total) originalBadge?.message else null
                )
            } else badge
        }
        _badges.value = updatedBadges
    }
}

