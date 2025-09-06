package com.tibolatte.milbadge

import com.tibolatte.milbadge.data.BadgeEntity
enum class Rarity { COMMON, MEDIUM, RARE, EPIC, LEGENDARY }

enum class BadgeType {
    UNIQUE,
    CUMULATIVE,
    PROGRESSIVE,
    EVENT,
    SECRET
}

enum class ObjectiveType {
    COUNT,      // nombre à atteindre, ex: pas, points
    DURATION,   // durée à atteindre
    YES_NO,     // action binaire
    CUSTOM,     // action spéciale
    CHECK       // nouveau : juste valider via bouton
}
enum class PeriodUnit { DAY, WEEK, MONTH }

data class Badge(
    var id: Int,
    var name: String,
    var rarity: Rarity,
    var type: BadgeType = BadgeType.UNIQUE,
    var isUnlocked: Boolean,
    var unlockDate: String? = null,
    var unlockConditionText: String? = null,
    var message: String? = null,
    var isFirstUnlocked: Boolean = false,
    var progress: Pair<Int, Int>? = null,       // currentDays / totalDays
    var objectiveType: ObjectiveType = ObjectiveType.COUNT,
    var lastActionDate: Long? = null,          // timestamp pour badges progressifs
    var totalForDay: Int = 0,                  // objectif journalier (ex: 6000 pas)
    var currentValue: Int = 0   ,               // valeur du jour
    val periodUnit: PeriodUnit = PeriodUnit.DAY

    )
fun BadgeEntity.toBadge(): Badge = Badge(
    id = id,
    name = name,
    rarity = com.tibolatte.milbadge.Rarity.valueOf(rarity),
    type = type,
    isUnlocked = isUnlocked,
    unlockDate = unlockDate,
    unlockConditionText = unlockConditionText,
    message = message,
    progress = progressCurrent to progressTotal,
    objectiveType = objectiveType,
    lastActionDate = lastActionDate,
    totalForDay = totalForDay,
    currentValue = currentValue,
    periodUnit = periodUnit
)

fun Badge.toEntity(): BadgeEntity = BadgeEntity(
    id = id,
    name = name,
    rarity = rarity.name,
    type = type,
    isUnlocked = isUnlocked,
    unlockDate = unlockDate,
    unlockConditionText = unlockConditionText,
    message = message,
    progressCurrent = progress?.first ?: 0,
    progressTotal = progress?.second ?: 1,
    objectiveType = objectiveType,
    lastActionDate = lastActionDate,
    totalForDay = totalForDay,
    currentValue = currentValue,
    periodUnit = periodUnit
)