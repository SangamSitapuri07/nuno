package com.nuno.app.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.home.LeftSidebar
import com.nuno.app.features.settings.components.*
import com.nuno.app.features.settings.models.SettingsCategory

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val saveMessage by viewModel.saveMessage.collectAsState()
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(SettingsCategory.GENERAL) }

    LaunchedEffect(saveMessage) {
        saveMessage?.let {
            context.showToast(it)
            viewModel.clearMessage()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "settings",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SETTINGS",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }

            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                Column(
                    modifier = Modifier.width(180.dp).fillMaxHeight().padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SettingsCategory.entries.forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (selectedCategory) {
                        SettingsCategory.GENERAL -> GeneralSettings(settings, viewModel)
                        SettingsCategory.GRAPHICS -> GraphicsSettings()
                        SettingsCategory.AUDIO -> AudioSettings(settings, viewModel)
                        SettingsCategory.GAMEPLAY -> GameplaySettings()
                        SettingsCategory.CONTROLS -> ControlsSettings(settings, viewModel)
                        SettingsCategory.PRIVACY -> PrivacySettings()
                        SettingsCategory.NOTIFICATIONS -> NotificationsSettings(settings, viewModel)
                        SettingsCategory.HELP -> HelpSettings()
                        SettingsCategory.ABOUT -> AboutSettings()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(category: SettingsCategory, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) PrimaryPurple.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category.icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = category.label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun GeneralSettings(settings: SettingsUiState, viewModel: SettingsViewModel) {
    SettingSection(title = "Display") {
        SettingRow(icon = Icons.Default.DarkMode, label = "Dark Mode", description = "Enable dark theme") {
            SettingSwitch(checked = settings.darkMode, onCheckedChange = { viewModel.updateDarkMode(it) })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Language, label = "Language", description = "English") {
            SettingDropdown(value = "English", onClick = { })
        }
    }
}

@Composable
private fun GraphicsSettings() {
    SettingSection(title = "Visual") {
        SettingRow(icon = Icons.Default.Speed, label = "FPS", description = "60 FPS") {
            SettingDropdown(value = "60 FPS", onClick = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.HighQuality, label = "Quality", description = "High") {
            SettingDropdown(value = "High", onClick = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Animation, label = "Animations", description = "Enable card animations") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
    }
}

@Composable
private fun AudioSettings(settings: SettingsUiState, viewModel: SettingsViewModel) {
    SettingSection(title = "Volume") {
        SettingRow(icon = Icons.Default.MusicNote, label = "Music Volume") {
            SettingSlider(
                value = settings.musicVolume.toFloat(),
                onValueChange = { viewModel.updateMusicVolume(it.toInt()) }
            )
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.AutoMirrored.Filled.VolumeUp, label = "Sound Effects") {
            SettingSlider(
                value = settings.soundVolume.toFloat(),
                onValueChange = { viewModel.updateSoundVolume(it.toInt()) }
            )
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Mic, label = "Voice Chat") {
            SettingSlider(
                value = settings.voiceVolume.toFloat(),
                onValueChange = { viewModel.updateVoiceVolume(it.toInt()) }
            )
        }
    }
}

@Composable
private fun GameplaySettings() {
    SettingSection(title = "Gameplay") {
        SettingRow(icon = Icons.Default.Speed, label = "Card Speed", description = "Normal") {
            SettingDropdown(value = "Normal", onClick = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.PlayArrow, label = "Auto Ready", description = "Automatically ready in lobby") {
            SettingSwitch(checked = false, onCheckedChange = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Warning, label = "Confirm Wild Card", description = "Show confirmation") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
    }
}

@Composable
private fun ControlsSettings(settings: SettingsUiState, viewModel: SettingsViewModel) {
    SettingSection(title = "Voice") {
        SettingRow(icon = Icons.Default.Mic, label = "Push to Talk", description = "Hold to speak") {
            SettingSwitch(
                checked = settings.pushToTalk,
                onCheckedChange = { viewModel.updatePushToTalk(it) }
            )
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Vibration, label = "Vibration", description = "Enable haptic feedback") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
    }
}

@Composable
private fun PrivacySettings() {
    SettingSection(title = "Privacy") {
        SettingRow(icon = Icons.Default.Visibility, label = "Show Online Status") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Group, label = "Accept Friend Requests") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Block, label = "Blocked Players") {
            SettingDropdown(value = "Manage", onClick = { })
        }
    }
}

@Composable
private fun NotificationsSettings(settings: SettingsUiState, viewModel: SettingsViewModel) {
    SettingSection(title = "Notifications") {
        SettingRow(icon = Icons.Default.Notifications, label = "Enable Notifications") {
            SettingSwitch(
                checked = settings.notifications,
                onCheckedChange = { viewModel.updateNotifications(it) }
            )
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.CardGiftcard, label = "Rewards") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.PersonAdd, label = "Friend Requests") {
            SettingSwitch(checked = true, onCheckedChange = { })
        }
    }
}

@Composable
private fun HelpSettings() {
    SettingSection(title = "Support") {
        SettingRow(icon = Icons.AutoMirrored.Filled.Help, label = "How to Play") {
            SettingDropdown(value = "View", onClick = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.Email, label = "Contact Support") {
            SettingDropdown(value = "Email", onClick = { })
        }
        HorizontalDivider(color = SurfaceDark)
        SettingRow(icon = Icons.Default.BugReport, label = "Report Bug") {
            SettingDropdown(value = "Report", onClick = { })
        }
    }
}

@Composable
private fun AboutSettings() {
    SettingSection(title = "About") {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "NUNO", color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Black, letterSpacing = 4.sp)
            Text(text = "Version 1.0.0", color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Play. Compete. Win.", color = AccentCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "© 2026 NUNO Games", color = TextTertiary, fontSize = 10.sp)
        }
    }
}