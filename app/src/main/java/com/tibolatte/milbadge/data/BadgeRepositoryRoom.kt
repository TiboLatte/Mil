package com.tibolatte.milbadge.data


import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeRepository
import com.tibolatte.milbadge.toBadge
import com.tibolatte.milbadge.toEntity
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Database(entities = [BadgeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun badgeDao(): BadgeDao
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    suspend fun getAllBadges(): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Update
    suspend fun updateBadge(badge: BadgeEntity)
}
class BadgeRepositoryRoom(private val context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "badges.db"
    )
        .fallbackToDestructiveMigration(true)
        .build()

    private val badgeDao = db.badgeDao()

    // --- CRUD ---
    suspend fun getBadges(): List<Badge> = badgeDao.getAllBadges().map { it.toBadge() }

    suspend fun insertBadge(badge: Badge) {
        badgeDao.insertBadge(badge.toEntity())
    }

    suspend fun updateBadge(badge: Badge) {
        badgeDao.updateBadge(badge.toEntity())
    }

    // --- Incrémente un badge (pour capteurs ou pas) ---
    suspend fun incrementBadge(badgeId: Int, amount: Int = 1) {
        val badges = getBadges().toMutableList()
        val badge = badges.firstOrNull { it.id == badgeId } ?: return
        val current = (badge.progress?.first ?: 0) + amount
        val total = badge.progress?.second ?: 1
        badge.progress = current.coerceAtMost(total) to total

        if (!badge.isUnlocked && current >= total) {
            badge.isUnlocked = true
            badge.unlockDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            badge.isFirstUnlocked = false
        }

        updateBadge(badge)
    }

    suspend fun incrementBadgeProgress(badgeId: Int, amount: Int) {
        val badge = getBadges().firstOrNull { it.id == badgeId } ?: return
        val current = badge.progress?.first ?: 0
        val total = badge.progress?.second ?: 1
        val newProgress = (current + amount).coerceAtMost(total)

        badge.progress = newProgress to total

        if (!badge.isUnlocked && newProgress >= total) {
            badge.isUnlocked = true
            badge.unlockDate =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            badge.isFirstUnlocked = false
        }

        updateBadge(badge)
    }

    // --- Définit une valeur spécifique pour un badge ---
    suspend fun setBadgeValue(badgeId: Int, value: Int) {
        val badges = getBadges().toMutableList()
        val badge = badges.firstOrNull { it.id == badgeId } ?: return
        val total = badge.progress?.second ?: 1
        badge.progress = value.coerceAtMost(total) to total

        if (!badge.isUnlocked && value >= total) {
            badge.isUnlocked = true
            badge.unlockDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            badge.isFirstUnlocked = false
        }

        updateBadge(badge)
    }

    // --- Pré-remplissage si la table est vide ---
    fun prepopulateIfEmpty() = runBlocking {
        if (badgeDao.getAllBadges().isEmpty()) {
            BadgeRepository.badges.forEach { insertBadge(it) }
        }
    }
}
