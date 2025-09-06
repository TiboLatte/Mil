
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.tibolatte.milbadge.BadgeType
import com.tibolatte.milbadge.ObjectiveType
import com.tibolatte.milbadge.R
import com.tibolatte.milbadge.components.AnimatedProgressRing
import com.tibolatte.milbadge.components.RotatableBadge
import com.tibolatte.milbadge.data.BadgeRepositoryRoom
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BadgeDetailScreen(
    badgeId: Int,
    badgeRepository: BadgeRepositoryRoom,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showConfetti by remember { mutableStateOf(false) }
    val extraRotationY = remember { Animatable(0f) }

    var countInput by remember { mutableStateOf("") }
    var yesNoChecked by remember { mutableStateOf(false) }

    // --- Flow réactif depuis Room ---
    val badges by badgeRepository.badgesFlow.collectAsState(initial = emptyList())
    val badge = badges.find { it.id == badgeId } ?: return

    if (badge == null) {
        Text("Chargement…", modifier = Modifier.padding(16.dp))
        return
    }

    badge.let { b ->
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
                val progressAnim = remember { Animatable(b.progress?.first?.toFloat() ?: 0f) }
                var showRing by remember { mutableStateOf(!b.isUnlocked && b.type == BadgeType.PROGRESSIVE) }

                LaunchedEffect(b.progress) {
                    if (!b.isUnlocked) {
                        val targetFraction = (b.progress?.first ?: 0) / (b.progress?.second ?: 1).toFloat()
                        progressAnim.animateTo(
                            targetFraction,
                            animationSpec = tween(1200)
                        )
                        // On ne cache le ring que si le badge est débloqué **après** l'animation
                        if (b.isUnlocked) {
                            showRing = false
                        }
                    }
                }
                Box(contentAlignment = Alignment.Center) {
                    if (showRing) {
                        AnimatedProgressRing(
                            badge = b,
                            size = 180f,
                            strokeWidth = 8f,
                            progressFraction = progressAnim.value
                        )
                    }
                    RotatableBadge(
                        badge = b,
                        sizeFraction = 0.55f,
                        extraRotationY = extraRotationY.value
                    )
                }

                if (showConfetti) {
                    LottieAnimation(
                        composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti)).value,
                        iterations = 1,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text("Rareté : ${b.rarity.name}", fontSize = 15.sp, color = Color.Gray)
                Text(b.name, fontSize = 28.sp)

                if (b.totalForDay > 0) {
                    Text(
                        text = "Aujourd’hui : ${b.currentValue}/${b.totalForDay}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (b.isUnlocked) {
                    Text("Débloqué le : ${b.unlockDate}", fontSize = 20.sp)
                    b.message?.let { Text(it, fontSize = 24.sp) }
                }




                if (!b.isUnlocked) {
                    Text(b.unlockConditionText ?: "À réaliser", fontSize = 20.sp)
                }

                val dailyMax = when (b.objectiveType) {
                    ObjectiveType.COUNT, ObjectiveType.DURATION -> b.totalForDay
                    ObjectiveType.YES_NO, ObjectiveType.CHECK, ObjectiveType.CUSTOM -> 1
                }

                val reachedDailyMax = b.currentValue >= dailyMax
                val reachedGlobalMax = (b.progress?.first ?: 0) >= (b.progress?.second ?: 1)
                val showInputButton = !b.isUnlocked && !reachedDailyMax && !reachedGlobalMax
                val inputValue = badgeRepository.getInputValue(b, countInput, yesNoChecked)

                if (showInputButton) {
                    when (b.objectiveType) {
                        ObjectiveType.COUNT, ObjectiveType.DURATION -> {
                            OutlinedTextField(
                                value = countInput,
                                onValueChange = { countInput = it.filter { c -> c.isDigit() } },
                                label = { Text("Entrez la valeur") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )
                        }
                        ObjectiveType.YES_NO -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = yesNoChecked, onCheckedChange = { yesNoChecked = it })
                                Text("Action effectuée ?")
                            }
                        }
                        ObjectiveType.CUSTOM -> Text("Action spéciale : valider depuis le jeu ou événement", fontSize = 18.sp)
                        ObjectiveType.CHECK -> Text("Cliquez sur Valider pour confirmer", fontSize = 18.sp)
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                showConfetti = true
                                extraRotationY.snapTo(1080f * 2)
                                extraRotationY.animateTo(0f, tween(4500))

                                when (b.type) {
                                    BadgeType.UNIQUE, BadgeType.EVENT, BadgeType.SECRET -> badgeRepository.setBadgeValue(b.id, 1)
                                    BadgeType.CUMULATIVE -> badgeRepository.incrementBadge(b.id, inputValue)
                                    BadgeType.PROGRESSIVE -> badgeRepository.incrementBadgeProgress(b.id, inputValue)
                                }

                                countInput = ""
                                yesNoChecked = false
                                showConfetti = false
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Valider", fontSize = 20.sp)
                    }
                } else {
                    Text("Objectif déjà rempli ✅", fontSize = 18.sp, color = Color.Gray)
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
}