package com.example.milehigh.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milehigh.core.SentinelCheckItem
import com.example.milehigh.ui.CampaignViewModel
import com.example.milehigh.ui.theme.*

@Composable
fun SentinelScreen(
    viewModel: CampaignViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val audit = uiState.auditReport
    val campaign = uiState.campaignData

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("sentinel_screen_list"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Banner
        item {
            val allPassed = audit?.isAllPassed == true
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurface),
                border = BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        if (allPassed) listOf(EmeraldMatrix, NeonCyan) else listOf(VoidfireCrimson, RadiantGold)
                    )
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (allPassed) EmeraldMatrix.copy(alpha = 0.2f) else VoidfireCrimson.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (allPassed) Icons.Default.VerifiedUser else Icons.Default.GppBad,
                                    contentDescription = null,
                                    tint = if (allPassed) EmeraldMatrix else VoidfireCrimson,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "SENTINEL PROTOCOL",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (allPassed) "ALL PARITY GATES SECURED" else "INTEGRITY DEGRADATION DETECTED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (allPassed) EmeraldMatrix else VoidfireCrimson
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Sentinel enforces strict mathematical boundaries on multiversal data: void saturation must remain within [0.0, 1.0], system parity fixed at 9, and interactive vectors validated.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.runSentinelAudit() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("run_sentinel_audit_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = VoidBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXECUTE FULL SENTINEL AUDIT",
                            color = VoidBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Checks List
        item {
            Text(
                text = "SECURITY VALIDATION GATES (${audit?.checks?.size ?: 0})",
                style = MaterialTheme.typography.labelMedium,
                color = NeonCyan
            )
        }

        if (audit != null) {
            items(audit.checks) { check ->
                SentinelCheckRow(check = check)
            }
        }

        // Raw Architecture Metadata Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VoidSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HORIZON DATA REGISTRATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArcaneViolet
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (campaign != null) {
                        MetadataRow(label = "Scene Identifier", value = campaign.sceneId)
                        MetadataRow(label = "Lighting Mode", value = campaign.metadata.lighting)
                        MetadataRow(label = "Multiversal Nexus", value = campaign.metadata.environment)
                        MetadataRow(label = "System Parity", value = "${campaign.metadata.systemParity}-bit (Conservation of Nine)")
                        MetadataRow(label = "Void Saturation", value = "${campaign.metadata.voidSaturationLevel}")
                        MetadataRow(label = "Registered Scenarios", value = "${campaign.scenarios.size} Acts/Chapters")
                        MetadataRow(label = "Active Dossiers", value = "${campaign.characters.size} Champions")
                    }
                }
            }
        }
    }
}

@Composable
fun SentinelCheckRow(check: SentinelCheckItem) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = VoidSurface),
        border = BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                if (check.isPassed) listOf(VoidBorder, VoidSurfaceVariant) else listOf(VoidfireCrimson, VoidBorder)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (check.isPassed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (check.isPassed) EmeraldMatrix else VoidfireCrimson,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = check.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = check.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (check.isPassed) TextSecondary else VoidfireCrimson
                )
            }
        }
    }
}

@Composable
fun MetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = RadiantGold,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp
        )
    }
}
