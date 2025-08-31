package com.tibolatte.milbadge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val RomanticTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 34.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Cursive,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )
)
