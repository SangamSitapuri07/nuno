package com.nuno.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.core.designsystem.components.ButtonStyle

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedCategory by remember { mutableIntStateOf(0) }
    val categories = listOf("General", "Audio", "Controls", "Notifications", "Privacy")

    var musicVolume by remember { mutableFloatStateOf(80f) }
    var sfxVolume by remember { mutableFloatStateOf(70f) }
    var voiceChat by remember { mutableStateOf(true) }
    var hapticFeedback by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("English") }
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }
    var voiceVolume by remember { mutableFloatStateOf(80f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left - Categories (#25 in reference)
            Column(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
                    .background(GameColors.BackgroundDark)
                    .padding(GameDimens.paddingMd)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Text("SETTINGS", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                categories.forEachIndexed { index, category ->
                    SettingCategory(
                        label = category,
                        isSelected = selectedCategory == index,
                        onClick = { selectedCategory = index }
                    )
                }
            }

            // Right - Settings content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (selectedCategory) {
                    0 -> {
                        SettingSlider("Game Sound", musicVolume) { musicVolume = it }
                        SettingSlider("Sound Effects", sfxVolume) { sfxVolume = it }
                        SettingSlider("Voice Volume", voiceVolume) { voiceVolume = it }
                        SettingToggle("Haptic Feedback", hapticFeedback) { hapticFeedback = it }
                        SettingDropdown("Language", language)
                    }

                    1 -> {
                        SettingSlider("Music", musicVolume) { musicVolume = it }
                        SettingSlider("Sound Effects", sfxVolume) { sfxVolume = it }
                        SettingToggle("Voice Chat", voiceChat) { voiceChat = it }
                    }

                    2 -> {
                        SettingToggle("Haptic Feedback", hapticFeedback) { hapticFeedback = it }
                    }

                    3 -> {
                        SettingToggle("Notifications", notifications) { notifications = it }
                    }

                    4 -> {
                        SettingToggle("Dark Mode", darkMode) { darkMode = it }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GameButton(
                    text = "LOGOUT",
                    onClick = onLogout,
                    style = ButtonStyle.DANGER,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SettingCategory(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) GameColors.Blue.copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color.Transparent,
                RoundedCornerShape(GameDimens.radiusSm)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isSelected) GameColors.Gold else GameColors.TextGray,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun SettingSlider(label: String, value: Float, onValueChange: (Float) -> Unit) {
    GamePanel {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = GameColors.TextWhite, fontSize = 13.sp, modifier = Modifier.width(120.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..100f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = GameColors.Gold,
                    activeTrackColor = GameColors.Blue,
                    inactiveTrackColor = GameColors.Surface
                )
            )
            Text(
                text = "${value.toInt()}%",
                color = GameColors.Gold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp)
            )
        }
    }
}

@Composable
private fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    GamePanel {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = GameColors.TextWhite, fontSize = 13.sp)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GameColors.Gold,
                    checkedTrackColor = GameColors.GoldDark.copy(alpha = 0.5f),
                    uncheckedThumbColor = GameColors.TextDark,
                    uncheckedTrackColor = GameColors.Surface
                )
            )
        }
    }
}

@Composable
private fun SettingDropdown(label: String, value: String) {
    GamePanel {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = GameColors.TextWhite, fontSize = 13.sp)
            Text(text = value, color = GameColors.Gold, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}