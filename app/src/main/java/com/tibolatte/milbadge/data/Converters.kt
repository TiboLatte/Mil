package com.tibolatte.milbadge.data


import androidx.room.TypeConverter
import com.tibolatte.milbadge.BadgeType
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.PeriodUnit


class Converters {
    @TypeConverter fun fromBadgeType(value: BadgeType): String = value.name
    @TypeConverter fun toBadgeType(value: String): BadgeType = BadgeType.valueOf(value)
    @TypeConverter fun fromObjectiveType(value: ObjectiveType): String = value.name
    @TypeConverter fun toObjectiveType(value: String): ObjectiveType = ObjectiveType.valueOf(value)
    @TypeConverter fun fromPeriodUnit(value: PeriodUnit): String = value.name
    @TypeConverter fun toPeriodUnit(value: String): PeriodUnit = PeriodUnit.valueOf(value)

    @TypeConverter fun fromEvolveThresholdsList(list: List<Int>?): String? = list?.joinToString(",")
    @TypeConverter
    fun toEvolveThresholdsList(data: String?): List<Int> {
        return data?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }
}