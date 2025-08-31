package com.tibolatte.milbadge.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tibolatte.milbadge.R


@Composable
fun ConfettiLottie(
    modifier: Modifier = Modifier,
    play: Boolean
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = play,
        iterations = 3 // 1 seule fois
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
        alignment = Alignment.Center
    )
}