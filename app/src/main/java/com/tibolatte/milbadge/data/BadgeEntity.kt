package com.tibolatte.milbadge.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tibolatte.milbadge.BadgeType
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.PeriodUnit

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val rarity: String,
    val type: BadgeType,
    val isUnlocked: Boolean,
    val unlockDate: String?,
    val unlockConditionText: String?,
    val message: String?,
    val progressCurrent: Int,
    val progressTotal: Int,
    val objectiveType: ObjectiveType,
    val lastActionDate: Long? = null,
    val totalForDay: Int = 0,
    val currentValue: Int = 0,
    val periodUnit: PeriodUnit
)