package com.tibolatte.milbadge

import com.tibolatte.milbadge.data.BadgeRepositoryRoom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object BadgeUpdater {

    suspend fun checkAllBadges(
        badgeRepo: BadgeRepositoryRoom,
        stepsThisWeek: Int = 0,
        poolVisitsInRow: Int = 0,
        sleepHoursInRow: Int = 0,
        booksRead: Int = 0,
        recipesCooked: Int = 0,
        selfiesTaken: Int = 0,
        artworksCreated: Int = 0,
        episodesWatched: Int = 0,
        squatsDone: Int = 0,
        placesVisited: Int = 0
    ) {
        val badges = badgeRepo.getBadges().toMutableList()
        badges.forEach { badge ->
            if (!badge.isUnlocked) {
                val (current, total) = when (badge.name) {
                    "Marcheur assidu" -> stepsThisWeek to 6000
                    "Nageur courageux" -> poolVisitsInRow to 3
                    "Petit chef" -> recipesCooked to 5
                    "Marathon du sommeil" -> sleepHoursInRow to 8
                    "Lecteur assidu" -> booksRead to 3
                    "Selfie social" -> selfiesTaken to 3
                    "Créatif fou" -> artworksCreated to 5
                    "Marathon de séries" -> episodesWatched to 10
                    "Défi sportif" -> squatsDone to 50
                    "Voyageur urbain" -> placesVisited to 5
                    else -> 0 to 1
                }

                badge.progress = current to total

                if (current >= total) {
                    badge.isUnlocked = true
                    badge.unlockDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    badge.isFirstUnlocked = false
                }

                badgeRepo.updateBadge(badge)
            }
        }
    }
}