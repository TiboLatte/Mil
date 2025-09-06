package com.tibolatte.milbadge.data


import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import com.tibolatte.milbadge.Badge
import com.tibolatte.milbadge.BadgeRepository
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.PeriodUnit
import com.tibolatte.milbadge.toBadge
import com.tibolatte.milbadge.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Database(entities = [BadgeEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun badgeDao(): BadgeDao
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    fun getAllFlow(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges")
    suspend fun getAll(): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(badges: List<BadgeEntity>)

    @Update
    suspend fun update(badge: BadgeEntity)

    @Query("SELECT * FROM badges WHERE id = :id")
    suspend fun getBadgeById(id: Int): BadgeEntity?
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

    // --- Flow réactif pour l'UI ---
    val badgesFlow: Flow<List<Badge>> = badgeDao.getAllFlow()
        .map { list -> list.map { it.toBadge() } }

    // --- List ponctuelle ---
    suspend fun getAllBadges(): List<Badge> = badgeDao.getAll().map { it.toBadge() }
    suspend fun getBadges(): List<Badge> = getAllBadges()

    suspend fun insertBadges(badges: List<Badge>) {
        badgeDao.insertAll(badges.map { it.toEntity() })
    }

    suspend fun updateBadge(badge: Badge) {
        badgeDao.update(badge.toEntity())
    }

    // --- Méthodes de progression ---
    suspend fun incrementBadge(badgeId: Int, amount: Int = 1) {
        val badge = getBadges().firstOrNull { it.id == badgeId } ?: return
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
        val now = System.currentTimeMillis()

        // Calcule le début de la période courante
        fun startOfPeriod(time: Long, unit: PeriodUnit): Long {
            val cal = java.util.Calendar.getInstance().apply { timeInMillis = time }
            when (unit) {
                PeriodUnit.DAY -> {
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                }
                PeriodUnit.WEEK -> {
                    cal.set(java.util.Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                }
                PeriodUnit.MONTH -> {
                    cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                }
            }
            return cal.timeInMillis
        }

        val currentPeriodStart = startOfPeriod(now, badge.periodUnit)
        val lastPeriodStart = badge.lastActionDate?.let { startOfPeriod(it, badge.periodUnit) }

        if (lastPeriodStart == null || currentPeriodStart > lastPeriodStart) {
            if (badge.currentValue < badge.totalForDay) {
                // on a raté l'objectif => reset de la progression
                badge.progress = 0 to (badge.progress?.second ?: 1)
            }
            badge.currentValue = 0
        }

        // Récupère la valeur saisie selon l’objectif
        val valueToAdd = when (badge.objectiveType) {
            ObjectiveType.COUNT, ObjectiveType.DURATION -> amount
            ObjectiveType.YES_NO, ObjectiveType.CHECK, ObjectiveType.CUSTOM -> if (amount > 0) 1 else 0
        }

        // Incrémente la valeur pour la période
        badge.currentValue = (badge.currentValue + valueToAdd).coerceAtMost(badge.totalForDay)

        // Si objectif atteint pour la période, incrémente progression globale
        if (badge.currentValue >= badge.totalForDay) {
            val current = (badge.progress?.first ?: 0) + 1
            val total = badge.progress?.second ?: 1
            badge.progress = current.coerceAtMost(total) to total
        }

        badge.lastActionDate = now

        // Déblocage si progression complète
        if (!badge.isUnlocked && (badge.progress?.first ?: 0) >= (badge.progress?.second ?: 1)) {
            badge.isUnlocked = true
            badge.unlockDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            badge.isFirstUnlocked = false
        }

        updateBadge(badge)
    }

    suspend fun setBadgeValue(badgeId: Int, value: Int) {
        val badge = getBadges().firstOrNull { it.id == badgeId } ?: return
        val total = badge.progress?.second ?: 1
        badge.progress = value.coerceAtMost(total) to total

        if (!badge.isUnlocked && value >= total) {
            badge.isUnlocked = true
            badge.unlockDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            badge.isFirstUnlocked = false
        }

        updateBadge(badge)
    }

    // --- Retourne la valeur de l’input selon type d’objectif ---
    fun getInputValue(badge: Badge, countInput: String, yesNoChecked: Boolean): Int {
        return when (badge.objectiveType) {
            ObjectiveType.COUNT, ObjectiveType.DURATION -> countInput.toIntOrNull() ?: 0
            ObjectiveType.YES_NO -> if (yesNoChecked) 1 else 0
            ObjectiveType.CUSTOM -> 1
            ObjectiveType.CHECK -> 1
        }
    }

    fun prepopulateIfEmpty() = runBlocking {
        if (badgeDao.getAll().isEmpty()) {
            insertBadges(BadgeRepository.badges)
        }
    }
}