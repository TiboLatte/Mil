package com.tibolatte.milbadge
object BadgeRepository {
    val badges: List<Badge> = listOf(
        Badge(
            id = 0,
            name = "Déclaration d'Amour",
            rarity = Rarity.RARE,
            type = BadgeType.UNIQUE,
            isUnlocked = false,
            unlockConditionText = "Rends fou ton amoureux avec un baiser",
            unlockDate = null,
            message = "Je t'aime tellement...",
            objectiveType = ObjectiveType.CHECK
        ),
        Badge(
            id = 1,
            name = "Marcheuse assidue",
            rarity = Rarity.RARE,
            type = BadgeType.PROGRESSIVE,
            isUnlocked = false,
            unlockConditionText = "Marcher 6000 pas par jour pendant 1 semaine",
            progress = 0 to 7,               // jours consécutifs
            totalForDay = 6000,              // objectif journalier
            currentValue = 0,                // compteur du jour
            objectiveType = ObjectiveType.COUNT,
            periodUnit = PeriodUnit.DAY

        ),
        Badge(
            id = 2,
            name = "Nageuse courageuse",
            rarity = Rarity.MEDIUM,
            type = BadgeType.EVOLVE,
            isUnlocked = false,
            evolveThresholds = listOf(5,10,15), // paliers d’évolution
            unlockConditionText = "Faire {X} longueurs en une seule fois",
            progress = 0 to 3,
            totalForDay = 1,
            currentValue = 0,
            objectiveType = ObjectiveType.CHECK,
        ),
        Badge(
            id = 3,
            name = "Expédition 33",
            rarity = Rarity.LEGENDARY,
            type = BadgeType.UNIQUE,
            isUnlocked = false,
            unlockConditionText = "Finir Clair Obscur: Expedition 33",
            unlockDate = null,
            message = "Enfin il est terminé! Pour ceux qui viendront après.",
            objectiveType = ObjectiveType.CHECK

        ),
        Badge(
            id = 4,
            name = "Avoir un petit chat",
            rarity = Rarity.LEGENDARY,
            type = BadgeType.UNIQUE,
            isUnlocked = false,
            unlockConditionText = "Enfin récupérer un petit chat!",
            unlockDate = null,
            message = "Une boule de poils à câliner",
            objectiveType = ObjectiveType.CHECK

        ),
        Badge(
            id = 5,
            name = "Ciao ESGI!",
            rarity = Rarity.LEGENDARY,
            type = BadgeType.UNIQUE,
            isUnlocked = false,
            unlockConditionText = "Finir l'ESGI",
            unlockDate = null,
            message = "Félicitations mademoiselle, les études, c'est terminé!",
            objectiveType = ObjectiveType.CHECK

        ),
        Badge(
            id = 6,
            name = "Le grand saut",
            rarity = Rarity.LEGENDARY,
            type = BadgeType.UNIQUE,
            isUnlocked = false,
            unlockConditionText = "Faire un saut en parachute",
            unlockDate = null,
            message = "Ca fait haut là quand même... plus jamais de vertige!",
            objectiveType = ObjectiveType.CHECK

        ),
        Badge(
            id = 7,
            name = "Test Minute",
            rarity = Rarity.COMMON,
            type = BadgeType.EVOLVE,
            isUnlocked = false,
            unlockConditionText = "Atteindre 3 minutes consécutives",
            progress = 0 to 3,
            totalForDay = 1,
            currentValue = 0,
            objectiveType = ObjectiveType.CHECK,
            evolveThresholds = listOf(1,2,3) // paliers d’évolution,

        )

    ) + List(19) { j ->
        Badge(101 + j, "Badge ${j + 12}", Rarity.COMMON, BadgeType.UNIQUE, false)
    }
}