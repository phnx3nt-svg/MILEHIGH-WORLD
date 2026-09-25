package com.example.milehigh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milehigh.ui.CampaignViewModel
import com.example.milehigh.ui.screens.AllianceScreen
import com.example.milehigh.ui.screens.CampaignScreen
import com.example.milehigh.ui.screens.ChampionsScreen
import com.example.milehigh.ui.screens.SentinelScreen
import com.example.milehigh.ui.theme.*

enum class AppTab(val label: String, val icon: ImageVector, val tag: String) {
    CAMPAIGN("Campaign", Icons.Default.Explore, "nav_campaign"),
    CHAMPIONS("Champions", Icons.Default.Groups, "nav_champions"),
    ALLIANCE("Alliance", Icons.Default.Shield, "nav_alliance"),
    SENTINEL("Sentinel", Icons.Default.Security, "nav_sentinel")
}

class MainActivity : ComponentActivity() {

    private val viewModel: CampaignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MilehighTheme {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: CampaignViewModel) {
    var selectedTab by remember { mutableStateOf(AppTab.CAMPAIGN) }
    val uiState by viewModel.uiState.collectAsState()
    val allianceState by viewModel.allianceState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.notificationMessage) {
        val msg = uiState.notificationMessage
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissNotification()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidDark),
        containerColor = VoidDark,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MILEHIGH WORLD",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "INTO THE VOID · MULTIVERSE",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCyan,
                                fontSize = 10.sp
                            )
                        }

                        // Live status pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = VoidSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(listOf(NeonCyan, ArcaneViolet))
                            ),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PARITY 9",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NeonCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = " · ",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    text = "${allianceState.synchronization}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RadiantGold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VoidDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = VoidSurface,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VoidBlack,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        containerColor = VoidSurfaceVariant,
                        contentColor = TextPrimary,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = NeonCyan
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.CAMPAIGN -> CampaignScreen(viewModel = viewModel)
                AppTab.CHAMPIONS -> ChampionsScreen(viewModel = viewModel)
                AppTab.ALLIANCE -> AllianceScreen(viewModel = viewModel)
                AppTab.SENTINEL -> SentinelScreen(viewModel = viewModel)
            }
        }
    }
}
