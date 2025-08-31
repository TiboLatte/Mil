
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.R
import com.tibolatte.milbadge.components.RotatableBadge
import com.tibolatte.milbadge.screens.HomeViewModel
import kotlinx.coroutines.launch
import kotlin.math.pow

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BadgeDetailScreen(
    badgeId: Int,
    homeViewModel: HomeViewModel,
    onClose: () -> Unit
) {
    val badges by homeViewModel.badges.collectAsState()
    val badge = badges.firstOrNull { it.id == badgeId } ?: return
    var showConfetti by remember { mutableStateOf(false) }

    var countInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Animatable pour rotation post-validation
    val extraRotationY = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RotatableBadge(
                badge = badge,
                sizeFraction = 0.55f,
                extraRotationY = extraRotationY.value
            )
            if (showConfetti) {
                LottieAnimation(
                    composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti)).value,
                    iterations = 1,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(badge.name, fontSize = 28.sp)
            Text("Rareté : ${badge.rarity.name}", fontSize = 20.sp, color = Color.Gray)

            badge.progress?.let { (current, total) ->
                Text("Progress : $current / $total", fontSize = 20.sp)
            }

            Text("Débloqué le : ${badge.unlockDate ?: "-"}", fontSize = 20.sp)

            if (!badge.isUnlocked) {
                Text(
                    "Objectif : ${badge.unlockConditionText ?: "À réaliser"}",
                    fontSize = 20.sp
                )

                if (badge.objectiveType == ObjectiveType.COUNT || badge.objectiveType == ObjectiveType.DURATION) {
                    OutlinedTextField(
                        value = countInput,
                        onValueChange = { input -> countInput = input.filter { it.isDigit() } },
                        label = { Text("Entrez la valeur", fontSize = 18.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Button(
                        onClick = {


                            scope.launch {
                                // Démarre la rotation rapide
                                // Affiche les confettis au moment du spin
                                showConfetti = true

                                extraRotationY.snapTo(1080f * 2) // rotation initiale
                                extraRotationY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = 4500, // tu peux ajuster
                                        easing = { fraction -> 1f - (1f - fraction).pow(4) } // quartic deceleration
                                    )
                                )

                                homeViewModel.completeObjective(
                                    badge.id,
                                    countInput.toIntOrNull() ?: 0
                                )
                                // Masque les confettis après la fin de l’animation
                                showConfetti = false
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Valider", fontSize = 20.sp)
                    }
                }
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Fermer")
        }
    }
}