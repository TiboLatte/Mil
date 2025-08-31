package com.tibolatte.milbadge

object BadgeRepository {

    val badges: List<Badge> = listOf(
        Badge(
            id = 0,
            name = "Étoile de l’Amour",
            rarity = Rarity.RARE,
            isUnlocked = true,
            unlockDate = "27/08/2025",
            message = "Mon amour, tu brilles ✨"
        ),
        Badge(
            id = 1,
            name = "Marcheuse assidue",
            rarity = Rarity.RARE,
            isUnlocked = false,
            unlockConditionText = "Marcher 6000 pas par jour pendant 1 semaine",
            message = "Et ben dis-donc, le badge originel!",
            progress = Pair(0,6000)
        ),
        Badge(
            id = 2,
            name = "Nageur courageux",
            rarity = Rarity.MEDIUM,
            isUnlocked = false,
            unlockConditionText = "Aller à la piscine 3 jours d’affilée",
        ),
        Badge(
            id = 3,
            name = "Petit chef",
            rarity = Rarity.MEDIUM,
            isUnlocked = false,
            unlockConditionText = "Cuisiner 5 recettes différentes",
        ),
        Badge(
            id = 4,
            name = "Marathon du sommeil",
            rarity = Rarity.RARE,
            isUnlocked = false,
            unlockConditionText = "Dormir 8 heures minimum pendant 5 nuits consécutives",
        ),
        Badge(
            id = 5,
            name = "Lecteur assidu",
            rarity = Rarity.MEDIUM,
            isUnlocked = false,
            unlockConditionText = "Lire 3 livres en un mois",
        ),
        Badge(
            id = 6,
            name = "Selfie social",
            rarity = Rarity.COMMON,
            isUnlocked = false,
            unlockConditionText = "Prendre une photo avec des amis 3 fois dans la semaine",
        ),
        Badge(
            id = 7,
            name = "Créatif fou",
            rarity = Rarity.RARE,
            isUnlocked = false,
            unlockConditionText = "Dessiner ou peindre 5 œuvres différentes",
        ),
        Badge(
            id = 8,
            name = "Marathon de séries",
            rarity = Rarity.COMMON,
            isUnlocked = false,
            unlockConditionText = "Regarder 10 épisodes d’une série",
        ),
        Badge(
            id = 9,
            name = "Défi sportif",
            rarity = Rarity.MEDIUM,
            isUnlocked = false,
            unlockConditionText = "Faire 50 squats en une seule séance",
        ),
        Badge(
            id = 10,
            name = "Voyageur urbain",
            rarity = Rarity.COMMON,
            isUnlocked = false,
            unlockConditionText = "Visiter 5 lieux différents dans ta ville",
        )
    ) + List(19) { j -> Badge(101 + j, "Badge ${j + 12}", Rarity.COMMON, false) }

}
