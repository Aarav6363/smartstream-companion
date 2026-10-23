package com.aaravpatel.smartstreamcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SmartStreamDashboard()
            }
        }
    }
}

@Composable
fun SmartStreamDashboard() {
    var selectedAmbience by remember { mutableStateOf("Forest Rain") }

    // Fast, visible background color transitions
    val primaryBgColor by animateColorAsState(
        targetValue = when (selectedAmbience) {
            "Forest Rain" -> Color(0xFF0D3B2E)
            "Ocean Waves" -> Color(0xFF0F3854)
            "Deep Space Lofi" -> Color(0xFF3B1354)
            else -> Color(0xFF0F172A)
        },
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "bgColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(primaryBgColor, Color(0xFF05070A))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF38BDF8).copy(alpha = 0.2f))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "FIRE TV AMBIENT COMPANION",
                    color = Color(0xFF38BDF8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "SmartStream Companion",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Dynamic Video Soundscapes & Living Room Dashboard",
                fontSize = 15.sp,
                color = Color.LightGray.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Active Stream Card with Live Equalizer Animation
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(
                        1.5.dp,
                        Color(0xFF38BDF8).copy(alpha = 0.6f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Animated Equalizer Bars
                    EqualizerBar(durationMillis = 400)
                    Spacer(modifier = Modifier.width(4.dp))
                    EqualizerBar(durationMillis = 700)
                    Spacer(modifier = Modifier.width(4.dp))
                    EqualizerBar(durationMillis = 500)

                    Spacer(modifier = Modifier.width(16.dp))

                    AnimatedContent(
                        targetState = selectedAmbience,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + slideInVertically { it / 2 } togetherWith
                                    fadeOut(animationSpec = tween(300))
                        },
                        label = "modeText"
                    ) { targetMode ->
                        Text(
                            text = "NOW PLAYING: $targetMode",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Ambience Selection Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AmbienceCard(
                    title = "🌲 Forest Rain",
                    subtitle = "Nature Audio • 4K Video",
                    isSelected = selectedAmbience == "Forest Rain",
                    onClick = { selectedAmbience = "Forest Rain" }
                )

                AmbienceCard(
                    title = "🌊 Ocean Waves",
                    subtitle = "Coastal Ambience",
                    isSelected = selectedAmbience == "Ocean Waves",
                    onClick = { selectedAmbience = "Ocean Waves" }
                )

                AmbienceCard(
                    title = "🌌 Deep Space Lofi",
                    subtitle = "Relaxation Mix",
                    isSelected = selectedAmbience == "Deep Space Lofi",
                    onClick = { selectedAmbience = "Deep Space Lofi" }
                )
            }
        }
    }
}

@Composable
fun EqualizerBar(durationMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq")
    val heightScale by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "height"
    )

    Box(
        modifier = Modifier
            .width(4.dp)
            .height(20.dp * heightScale)
            .clip(CircleShape)
            .background(Color(0xFF22C55E))
    )
}

@Composable
fun AmbienceCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF0284C7) else Color(0xFF1E293B).copy(alpha = 0.9f),
            contentColor = Color.White
        ),
        modifier = Modifier
            .width(200.dp)
            .height(85.dp)
            .scale(scale)
            .border(
                width = if (isSelected) 2.5.dp else 0.dp,
                color = if (isSelected) Color(0xFF38BDF8) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}