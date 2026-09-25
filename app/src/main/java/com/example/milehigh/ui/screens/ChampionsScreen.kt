package com.example.milehigh.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milehigh.data.CharacterProfile
import com.example.milehigh.ui.CampaignViewModel
import com.example.milehigh.ui.theme.*

@Composable
fun ChampionsScreen(
    viewModel: CampaignViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val allianceState by viewModel.allianceState.collectAsState()
    val characters = uiState.campaignData?.characters ?: emptyList()

    val selectedChar = characters.find { it.id == uiState.selectedCharacterId }
        ?: characters.firstOrNull()

    var showScript by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("champions_screen_list"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Champion Selector Carousel
        item {
            Text(
                text = "CHAMPIONS & AVATARS ROSTER",
                style = MaterialTheme.typography.labelMedium,
                color = NeonCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.testTag("champions_carousel")
            ) {
                items(characters) { char ->
                    val isSelected = char.id == selectedChar?.id
                    val isIngrisRestored = char.name.contains("Delilah") && allianceState.ingrisRestored

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) VoidSurfaceVariant else VoidSurface,
                        border = if (isSelected) BorderStroke(
                            1.dp,
                            Brush.horizontalGradient(listOf(NeonCyan, ArcaneViolet))
                        ) else null,
                        modifier = Modifier
                            .clickable {
                                viewModel.selectCharacter(char.id)
                                showScript = false
                            }
                            .testTag("champion_card_${char.id}")
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .widthIn(min = 120.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isIngrisRestored -> EmeraldMatrix.copy(alpha = 0.3f)
                                            char.name.contains("Micah") -> RadiantGold.copy(alpha = 0.3f)
                                            char.name.contains("Omega") -> NeonCyan.copy(alpha = 0.3f)
                                            char.name.contains("Cyrus") -> VoidfireCrimson.copy(alpha = 0.3f)
                                            char.name.contains("Aeron") -> ArcaneViolet.copy(alpha = 0.3f)
                                            else -> ArcanePurple.copy(alpha = 0.3f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getIconForRole(char.role),
                                    contentDescription = char.name,
                                    tint = if (isIngrisRestored) EmeraldMatrix else NeonCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isIngrisRestored) "Ingris (Restored)" else char.name.split(" ").first(),
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isIngrisRestored) EmeraldMatrix else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = char.role.split("/").first().trim(),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        if (selectedChar != null) {
            val isDelilah = selectedChar.name.contains("Delilah")
            val isRestored = isDelilah && allianceState.ingrisRestored

            // Active Dossier Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = VoidSurface),
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            if (isRestored) listOf(EmeraldMatrix, NeonCyan) else listOf(ArcanePurple, VoidBorder)
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isRestored) "Ingris the Untainted" else selectedChar.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (isRestored) EmeraldMatrix else RadiantGold
                                )
                                Text(
                                    text = if (isRestored) "Restored Ally / Astral Weaver" else selectedChar.role,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ArcaneViolet
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = VoidSurfaceVariant
                            ) {
                                Text(
                                    text = "VOID AFFINITY: ${(selectedChar.voidAffinity * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selectedChar.voidAffinity > 0.5f) VoidfireCrimson else NeonCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (selectedChar.quote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = VoidDark.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "\"${selectedChar.quote}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(10.dp),
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Bars
                        Text(
                            text = "TACTICAL CAPABILITIES",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        StatBar(label = "Base Defense", value = selectedChar.defense, max = 700, color = NeonCyan)
                        Spacer(modifier = Modifier.height(6.dp))
                        StatBar(label = "Kinetic Strength", value = selectedChar.strength, max = 700, color = RadiantGold)

                        Spacer(modifier = Modifier.height(14.dp))

                        // Traits Chips
                        Text(
                            text = "TRAITS & PROTOCOLS",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            selectedChar.traits.forEach { trait ->
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = VoidSurfaceVariant
                                ) {
                                    Text(
                                        text = trait.replace("_", " "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextPrimary,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Ability Execution Chamber
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = VoidSurfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ACTIVE ABILITY CHAMBER",
                                style = MaterialTheme.typography.labelMedium,
                                color = RadiantGold
                            )
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Ability Chamber",
                                tint = RadiantGold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        when {
                            selectedChar.name.contains("Micah") -> {
                                Text(
                                    text = "Reactive Hardening: Absorbs kinetic attacks, charges Flow Meter (+15) and extrudes gauntlet (${String.format("%.1f", allianceState.gauntletExtrusion)}x).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.triggerMicahKineticRedirection() },
                                    colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("micah_redirect_button")
                                ) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = VoidBlack)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("REDIRECT KINETIC FORCE", color = VoidBlack, fontWeight = FontWeight.Bold)
                                }
                            }

                            selectedChar.name.contains("Omega") -> {
                                Text(
                                    text = "Gemini Logic: Reconciles corrupted shard memory pointers and restores system stability (${(allianceState.stabilityIndex * 100).toInt()}%).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.triggerOmegaGeminiLogic() },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("omega_gemini_button")
                                ) {
                                    Icon(Icons.Default.Memory, contentDescription = null, tint = VoidBlack)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("RUN GEMINI LOGIC RECONCILIATION", color = VoidBlack, fontWeight = FontWeight.Bold)
                                }
                            }

                            selectedChar.name.contains("Aeron") -> {
                                Text(
                                    text = "Airborne Ginga: Flap thrusters, maintain altitude, and unleash Void Lightning Stomp once Flow Meter is primed.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.triggerAeronAirborneStomp() },
                                    colors = ButtonDefaults.buttonColors(containerColor = ArcaneViolet),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("aeron_stomp_button")
                                ) {
                                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = VoidBlack)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("EXECUTE VOID LIGHTNING STOMP", color = VoidBlack, fontWeight = FontWeight.Bold)
                                }
                            }

                            selectedChar.name.contains("Anastasia") -> {
                                val hasHarmony = allianceState.activeStatuses.contains("Dream_Harmony")
                                Text(
                                    text = "Psychic Weaver: Enters Dream Harmony state and unlocks Blinding Truth required for boss purification.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.triggerAnastasiaDreamTrance() },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (hasHarmony) ArcanePurple else NeonCyan),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("anastasia_dream_button")
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = VoidBlack)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        if (hasHarmony) "DISMISS REVERIE FORM" else "WEAVE DREAM HARMONY & TRUTH",
                                        color = VoidBlack,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            selectedChar.name.contains("Delilah") -> {
                                val hasTruth = allianceState.activeStatuses.contains("Blinding_Truth")
                                val hasHarmony = allianceState.activeStatuses.contains("Dream_Harmony")
                                Text(
                                    text = if (isRestored) {
                                        "Ingris has been successfully purified! Voidfire extinguished."
                                    } else {
                                        "Avatar of Decay: Casts Voidfire. Status needed: 'Blinding Truth' [${if (hasTruth) "ACTIVE" else "MISSING"}] & 'Dream Harmony' [${if (hasHarmony) "ACTIVE" else "MISSING"}]."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isRestored) EmeraldMatrix else TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.triggerDelilahPurification() },
                                    enabled = !isRestored,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (hasTruth && hasHarmony) EmeraldMatrix else VoidfireCrimson
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("delilah_purify_button")
                                ) {
                                    Icon(
                                        if (isRestored) Icons.Default.CheckCircle else Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        if (isRestored) "INGRIS FULLY RESTORED" else "PURIFY DELILAH (CAST TRUTH)",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            else -> {
                                Text(
                                    text = "Antagonist / Void Anchor: Imposes Void Shockwave across Onalym Nexus, overwriting core logic.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.setVoidSaturation(allianceState.voidSaturation + 0.15f) },
                                    colors = ButtonDefaults.buttonColors(containerColor = VoidfireCrimson),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("cyrus_shockwave_button")
                                ) {
                                    Icon(Icons.Default.Dangerous, contentDescription = null, tint = TextPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("UNLEASH VOID SHOCKWAVE", color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Expandable Behavior Script Inspector
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = VoidSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showScript = !showScript },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = "Code",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AUTONOMOUS BEHAVIOR SCRIPT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                            }
                            Icon(
                                imageVector = if (showScript) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle Script",
                                tint = NeonCyan
                            )
                        }

                        AnimatedVisibility(visible = showScript) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = VoidDark,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = selectedChar.behaviorScript,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = NeonCyan,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBar(label: String, value: Int, max: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text(
                text = "$value / $max",
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (value.toFloat() / max.toFloat()).coerceIn(0f, 1f) },
            color = color,
            trackColor = VoidBorder,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

fun getIconForRole(role: String): ImageVector {
    return when {
        role.contains("Tank", ignoreCase = true) -> Icons.Default.Shield
        role.contains("AI", ignoreCase = true) || role.contains("Guide", ignoreCase = true) -> Icons.Default.SmartToy
        role.contains("Antagonist", ignoreCase = true) -> Icons.Default.Warning
        role.contains("Aerial", ignoreCase = true) || role.contains("Sentinel", ignoreCase = true) -> Icons.Default.Flight
        role.contains("Healer", ignoreCase = true) || role.contains("Psychic", ignoreCase = true) -> Icons.Default.Psychology
        role.contains("Boss", ignoreCase = true) || role.contains("Decay", ignoreCase = true) -> Icons.Default.LocalFireDepartment
        else -> Icons.Default.Person
    }
}

