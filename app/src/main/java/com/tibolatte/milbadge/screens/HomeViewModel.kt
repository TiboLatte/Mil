package com.tibolatte.milbadge.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeType
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.PeriodUnit
import com.tibolatte.milbadge.data.BadgeRepositoryRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class HomeViewModel(
    application: Application,
    private val repository: BadgeRepositoryRoom
) : AndroidViewModel(application) {

    private val _badges = MutableStateFlow<List<Badge>>(emptyList())
    val badges: StateFlow<List<Badge>> = _badges

    init {
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
            _badges.value = repository.getAllBadges()
        }
    }fun recalculateBadgeProgress(badge: Badge) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            fun startOfPeriod(time: Long, unit: PeriodUnit): Long {
                val cal = java.util.Calendar.getInstance().apply { timeInMillis = time }
                when (unit) {
                    PeriodUnit.DAY -> cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    PeriodUnit.WEEK -> cal.set(java.util.Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                    PeriodUnit.MONTH -> cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
                }
                cal.set(java.util.Calendar.MINUTE, 0)
                cal.set(java.util.Calendar.SECOND, 0)
                cal.set(java.util.Calendar.MILLISECOND, 0)
                return cal.timeInMillis
            }

            val currentPeriodStart = startOfPeriod(now, badge.periodUnit)
            val lastPeriodStart = badge.lastActionDate?.let { startOfPeriod(it, badge.periodUnit) }

            if (lastPeriodStart == null || currentPeriodStart > lastPeriodStart) {
                badge.currentValue = 0
            }

            repository.updateBadge(badge)
        }
    }
    fun completeObjective(badgeId: Int, value: Int = 1) {
        viewModelScope.launch {
            val updatedBadges = _badges.value.map { badge ->
                if (badge.id == badgeId) {
                    val now = System.currentTimeMillis()
                    val updatedBadge = when (badge.objectiveType) {
                        ObjectiveType.COUNT, ObjectiveType.DURATION -> {
                            val current = ((badge.progress?.first ?: 0) + value)
                                .coerceAtMost(badge.progress?.second ?: 1)
                            val isUnlocked = current >= (badge.progress?.second ?: 1)
                            badge.copy(
                                progress = current to (badge.progress?.second ?: 1),
                                isUnlocked = isUnlocked,
                                unlockDate = if (isUnlocked) SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(Date()) else badge.unlockDate,
                                lastActionDate = if (badge.type == BadgeType.PROGRESSIVE) now else badge.lastActionDate
                            )
                        }

                        ObjectiveType.YES_NO, ObjectiveType.CUSTOM, ObjectiveType.CHECK -> {
                            val isUnlocked = value > 0
                            badge.copy(
                                progress = 1 to 1,
                                isUnlocked = isUnlocked,
                                unlockDate = if (isUnlocked) SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(Date()) else badge.unlockDate,
                                lastActionDate = now
                            )
                        }
                    }

                    repository.updateBadge(updatedBadge)
                    updatedBadge
                } else badge
            }
            _badges.value = updatedBadges
        }
    }
    fun refreshBadges() {
        viewModelScope.launch {
            _badges.value = repository.getAllBadges()
        }
    }
}