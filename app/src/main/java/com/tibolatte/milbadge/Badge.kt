package com.tibolatte.milbadge

import com.tibolatte.milbadge.data.BadgeEntity

enum class Rarity { COMMON, MEDIUM, RARE, EPIC, LEGENDARY }

enum class ObjectiveType {
    COUNT,      // un nombre à atteindre, ex: pas, points
    DURATION,   // faire une action pendant une durée
    YES_NO,     // simple oui/non, ex: as-tu fait l’action ?
    CUSTOM      // tout autre type spécifique
}

data class Badge(
    var id: Int,
    var name: String,
    var rarity: Rarity,
    var isUnlocked: Boolean,
    var unlockDate: String? = null,
    var unlockConditionText: String? = null,
    var message: String? = null,
    var isFirstUnlocked: Boolean = false,
    var progress: Pair<Int, Int>? = null, // current / total
    var objectiveType: ObjectiveType = ObjectiveType.COUNT // Ajout du type d'objectif
)

fun BadgeEntity.toBadge(): Badge = Badge(
    id = id,
    name = name,
    rarity = Rarity.valueOf(rarity),
    isUnlocked = isUnlocked,
    unlockDate = unlockDate,
    unlockConditionText = unlockConditionText,
    message = message,
    progress = progressCurrent to progressTotal,
    isFirstUnlocked = false,
    objectiveType = ObjectiveType.COUNT // par défaut COUNT, tu peux adapter si BadgeEntity stocke le type
)

fun Badge.toEntity(): BadgeEntity = BadgeEntity(
    id = id,
    name = name,
    rarity = rarity.name,
    isUnlocked = isUnlocked,
    unlockDate = unlockDate,
    unlockConditionText = unlockConditionText,
    message = message,
    progressCurrent = progress?.first ?: 0,
    progressTotal = progress?.second ?: 1
    // ici tu peux ajouter un champ objectiveType si tu veux le stocker dans la DB
)
