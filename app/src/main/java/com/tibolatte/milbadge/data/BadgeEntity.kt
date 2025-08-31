package com.tibolatte.milbadge.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val rarity: String,
    val isUnlocked: Boolean,
    val unlockDate: String?,
    val unlockConditionText: String?,
    val message: String?,
    val progressCurrent: Int,
    val progressTotal: Int
)