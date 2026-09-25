package com.example.milehigh.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milehigh.R
import com.example.milehigh.data.ObjectInteraction
import com.example.milehigh.data.SceneScenario
import com.example.milehigh.ui.CampaignViewModel
import com.example.milehigh.ui.theme.*

@Composable
fun CampaignScreen(
    viewModel: CampaignViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val campaign = uiState.campaignData

    if (campaign == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonCyan)
        }
        return
    }

    val selectedScenario = campaign.scenarios.getOrNull(uiState.selectedScenarioIndex)
        ?: campaign.scenarios.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("campaign_screen_list"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_void_nexus),
                    contentDescription = "Onalym Nexus banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    VoidDark.copy(alpha = 0.85f),
                                    VoidDark
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NeonCyan.copy(alpha = 0.2f),
                            border = BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(listOf(NeonCyan, ArcaneViolet))
                            )
                        ) {
                            Text(
                                text = "PARITY ${campaign.metadata.systemParity} · ${campaign.metadata.lighting.uppercase()}",
                                color = NeonCyan,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = VoidSurfaceVariant
                        ) {
                            Text(
                                text = campaign.metadata.environment.take(24) + "...",
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "THE VERSE: INTO THE VOID",
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        // Scenario Chapter Navigation Chips
        item {
            Column(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)) {
                Text(
                    text = "CAMPAIGN TIMELINE",
                    style = MaterialTheme.typography.labelMedium,
                    color = NeonCyan,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("scenario_selector_row")
                ) {
                    itemsIndexed(campaign.scenarios) { index, sc ->
                        val isSelected = index == uiState.selectedScenarioIndex
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) VoidSurfaceVariant else VoidSurface,
                            border = if (isSelected) BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(listOf(NeonCyan, ArcaneViolet))
                            ) else null,
                            modifier = Modifier
                                .clickable { viewModel.selectScenario(index) }
                                .testTag("scenario_chip_$index")
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = sc.act,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) NeonCyan else TextMuted
                                )
                                Text(
                                    text = sc.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Scenario Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurface),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedScenario.title.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            color = RadiantGold
                        )
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Scenario icon",
                            tint = RadiantGold
                        )
                    }
                    Text(
                        text = selectedScenario.act,
                        style = MaterialTheme.typography.labelSmall,
                        color = ArcaneViolet
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedScenario.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
        }

        // Interactive Objects & Vectors
        item {
            Text(
                text = "INTERACTIVE 3D OBJECTS & VECTORS (${selectedScenario.interactiveObjects.size})",
                style = MaterialTheme.typography.labelMedium,
                color = NeonCyan,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(selectedScenario.interactiveObjects) { obj ->
            InteractiveObjectCard(
                obj = obj,
                scenarioId = selectedScenario.scenarioId,
                onUpdate = { newFloat, newX, newY, newZ ->
                    viewModel.updateInteractiveObject(
                        scenarioId = selectedScenario.scenarioId,
                        objectId = obj.objectId,
                        newFloat = newFloat,
                        newX = newX,
                        newY = newY,
                        newZ = newZ
                    )
                }
            )
        }

        // Narrative Dialogue
        if (selectedScenario.dialogue.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "BATTLEREM NARRATIVE DIALOGUE",
                    style = MaterialTheme.typography.labelMedium,
                    color = ArcaneViolet,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            items(selectedScenario.dialogue) { dia ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = VoidSurfaceVariant),
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(listOf(ArcanePurple, VoidBorder))
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ArcaneViolet.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubble,
                                    contentDescription = "Speaker",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dia.speaker,
                                style = MaterialTheme.typography.titleSmall,
                                color = NeonCyan
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "TRIGGER: ${dia.trigger}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${dia.text}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveObjectCard(
    obj: ObjectInteraction,
    scenarioId: String,
    onUpdate: (Float, Float, Float, Float) -> Unit
) {
    var floatVal by remember(obj.objectId, obj.floatValue) { mutableFloatStateOf(obj.floatValue) }
    var xVal by remember(obj.objectId, obj.x) { mutableFloatStateOf(obj.x) }
    var yVal by remember(obj.objectId, obj.y) { mutableFloatStateOf(obj.y) }
    var zVal by remember(obj.objectId, obj.z) { mutableFloatStateOf(obj.z) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = VoidSurface),
        border = BorderStroke(
            1.dp,
            Brush.horizontalGradient(listOf(VoidBorder, VoidSurfaceVariant))
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = obj.objectId,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "ACTION: ${obj.action}",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (obj.isVector) ArcanePurple.copy(alpha = 0.3f) else NeonCyan.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (obj.isVector) "VECTOR 3D" else "SCALAR",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (obj.isVector) ArcaneViolet else NeonCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (obj.isVector) {
                // Vector 3D Adjusters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VectorAxisAdjuster(label = "X", value = xVal, onDelta = { delta ->
                        xVal += delta
                        onUpdate(floatVal, xVal, yVal, zVal)
                    }, modifier = Modifier.weight(1f))
                    VectorAxisAdjuster(label = "Y", value = yVal, onDelta = { delta ->
                        yVal += delta
                        onUpdate(floatVal, xVal, yVal, zVal)
                    }, modifier = Modifier.weight(1f))
                    VectorAxisAdjuster(label = "Z", value = zVal, onDelta = { delta ->
                        zVal += delta
                        onUpdate(floatVal, xVal, yVal, zVal)
                    }, modifier = Modifier.weight(1f))
                }
            } else {
                // Scalar Float Adjuster
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Value: ${String.format("%.1f", floatVal)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RadiantGold,
                        fontFamily = FontFamily.Monospace
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                floatVal -= 10f
                                onUpdate(floatVal, xVal, yVal, zVal)
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VoidSurfaceVariant)
                        ) {
                            Text("-10", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        }
                        Button(
                            onClick = {
                                floatVal += 10f
                                onUpdate(floatVal, xVal, yVal, zVal)
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VoidSurfaceVariant)
                        ) {
                            Text("+10", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VectorAxisAdjuster(
    label: String,
    value: Float,
    onDelta: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = VoidSurfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$label: ${String.format("%.1f", value)}",
                style = MaterialTheme.typography.labelSmall,
                color = RadiantGold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = VoidSurface,
                    modifier = Modifier
                        .clickable { onDelta(-5f) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("-", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = VoidSurface,
                    modifier = Modifier
                        .clickable { onDelta(+5f) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("+", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
