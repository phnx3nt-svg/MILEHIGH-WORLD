package com.example.milehigh.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milehigh.ui.CampaignViewModel
import com.example.milehigh.ui.theme.*

@Composable
fun AllianceScreen(
    viewModel: CampaignViewModel,
    modifier: Modifier = Modifier
) {
    val allianceState by viewModel.allianceState.collectAsState()

    var testSeedInput by remember { mutableStateOf("144") }
    val parityResult = remember(testSeedInput) {
        val num = testSeedInput.toIntOrNull() ?: 0
        if (num == 0) 9 else {
            val r = num % 9
            if (r == 0) 9 else r
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("alliance_screen_list"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Alliance Header
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurface),
                border = BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(NeonCyan, ArcaneViolet))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ONALYM CORE ALLIANCE",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = "ACT IV RAID · TSIDKENU ENGAGEMENT",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCyan
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (allianceState.isTsidkenuPrimed) RadiantGold.copy(alpha = 0.2f) else VoidSurfaceVariant
                        ) {
                            Text(
                                text = if (allianceState.isTsidkenuPrimed) "STRIKE READY" else "SYNCHRONIZING",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (allianceState.isTsidkenuPrimed) RadiantGold else TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Circular Sync Gauge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val animatedSync by animateFloatAsState(
                            targetValue = allianceState.synchronization / 100f,
                            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                            label = "syncAnimation"
                        )

                        Canvas(modifier = Modifier.size(160.dp)) {
                            val strokeWidth = 14.dp.toPx()
                            val radius = (size.minDimension - strokeWidth) / 2
                            val center = Offset(size.width / 2, size.height / 2)

                            // Background circle
                            drawCircle(
                                color = VoidBorder,
                                radius = radius,
                                center = center,
                                style = Stroke(width = strokeWidth)
                            )

                            // Foreground arc
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(NeonCyan, ArcaneViolet, RadiantGold, NeonCyan)
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * animatedSync,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2, radius * 2)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${allianceState.synchronization}%",
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (allianceState.synchronization >= 100) RadiantGold else NeonCyan
                            )
                            Text(
                                text = "SYNCHRONIZATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }

                    // Quick Sync adjustment buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.adjustSync(-15) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            modifier = Modifier.testTag("sync_minus_button")
                        ) {
                            Text("-15%")
                        }
                        Button(
                            onClick = { viewModel.adjustSync(+15) },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            modifier = Modifier.testTag("sync_plus_button")
                        ) {
                            Text("+15% SYNC", color = VoidBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Primary Tactical Strike & Split Peel Actions
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "TACTICAL DIRECTIVES",
                        style = MaterialTheme.typography.labelMedium,
                        color = RadiantGold
                    )

                    // TSIDKENU Strike Button
                    Button(
                        onClick = { viewModel.fireTsidkenuStrike(0.0f, 1000.0f, 0.0f) },
                        enabled = allianceState.synchronization >= 100,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RadiantGold,
                            disabledContainerColor = VoidBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("tsidkenu_strike_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = if (allianceState.synchronization >= 100) VoidBlack else TextMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXECUTE TSIDKENU STRIKE (0, 1000, 0)",
                            color = if (allianceState.synchronization >= 100) VoidBlack else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Split Peel Toggle
                    OutlinedButton(
                        onClick = { viewModel.toggleSplitPeel() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (allianceState.isSplitPeelActive) EmeraldMatrix else NeonCyan
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("split_peel_button")
                    ) {
                        Icon(
                            imageVector = if (allianceState.isSplitPeelActive) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.AltRoute,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (allianceState.isSplitPeelActive) "SPLIT PEEL: ACTIVE (DISENGAGE)" else "ENGAGE SPLIT PEEL PROTOCOL",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Void Saturation Control
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "VOID SATURATION LEVEL",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                        Text(
                            text = String.format("%.2f", allianceState.voidSaturation),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (allianceState.voidSaturation > 0.5f) VoidfireCrimson else NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = allianceState.voidSaturation,
                        onValueChange = { viewModel.setVoidSaturation(it) },
                        valueRange = 0.0f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = if (allianceState.voidSaturation > 0.5f) VoidfireCrimson else NeonCyan,
                            activeTrackColor = if (allianceState.voidSaturation > 0.5f) VoidfireCrimson else NeonCyan,
                            inactiveTrackColor = VoidBorder
                        ),
                        modifier = Modifier.testTag("void_saturation_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0.0 (Pure Reality)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Text("1.0 (Total Collapse)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
            }
        }

        // Vortex Mathematics / Parity 9 Calculator
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Vortex Math",
                            tint = ArcaneViolet
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "INFINITERATION ENGINE · CONSERVATION OF NINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = ArcaneViolet
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vortex Parity ensures all dimensional variables balance to 9-bit equilibrium. Digital root mod 9 transforms multiversal noise into harmonic anchors.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = testSeedInput,
                            onValueChange = { testSeedInput = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Input Seed / Frequency") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ArcaneViolet,
                                unfocusedBorderColor = VoidBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("parity_seed_input")
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = VoidDark,
                            border = BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(listOf(ArcaneViolet, RadiantGold))
                            ),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "PARITY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 9.sp
                                )
                                Text(
                                    text = "$parityResult",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = RadiantGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tactical Report Log
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "TACTICAL TELEMETRY LOG",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = VoidDark,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "> ${allianceState.lastActionReport}",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}
