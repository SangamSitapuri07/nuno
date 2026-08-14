package com.nuno.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.screens.home.PremiumGameTableBackground

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

    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()

        Row(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            // Sidebar
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
                    .background(Brush.verticalGradient(listOf(Color(0xFF121535).copy(0.98f), Color(0xFF0A0C22))))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(0.08f), RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(10.dp))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("SETTINGS", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    categories.forEachIndexed { index, category ->
                        PremiumSettingCategory(label = category, isSelected = selectedCategory == index) { selectedCategory = index }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("PREFERENCES", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)

                when (selectedCategory) {
                    0 -> {
                        SettingSliderPremium("Game Sound", musicVolume) { musicVolume = it }
                        SettingSliderPremium("Sound Effects", sfxVolume) { sfxVolume = it }
                        SettingSliderPremium("Voice Volume", voiceVolume) { voiceVolume = it }
                        SettingTogglePremium("Haptic Feedback", hapticFeedback) { hapticFeedback = it }
                        SettingDropdownPremium("Language", language)
                    }
                    1 -> {
                        SettingSliderPremium("Music", musicVolume) { musicVolume = it }
                        SettingSliderPremium("Sound Effects", sfxVolume) { sfxVolume = it }
                        SettingTogglePremium("Voice Chat", voiceChat) { voiceChat = it }
                    }
                    2 -> {
                        SettingTogglePremium("Haptic Feedback", hapticFeedback) { hapticFeedback = it }
                    }
                    3 -> {
                        SettingTogglePremium("Push Notifications", notifications) { notifications = it }
                    }
                    4 -> {
                        SettingTogglePremium("Dark Mode", darkMode) { darkMode = it }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF3B5C).copy(0.08f), RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFFFF3B5C).copy(0.2f), RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("DANGER ZONE", color = GameColors.Red, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        GameButton(text = "LOGOUT", onClick = onLogout, style = ButtonStyle.DANGER, modifier = Modifier.fillMaxWidth().height(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumSettingCategory(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.2f), Color(0xFF7B5CFF).copy(0.12f)))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, if (isSelected) GameColors.Blue.copy(0.3f) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium)
    }
}

@Composable
private fun SettingSliderPremium(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131636), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .background(GameColors.Gold.copy(0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("${value.toInt()}%", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = GameColors.Gold,
                    activeTrackColor = GameColors.Blue,
                    inactiveTrackColor = Color(0xFF1E2249)
                )
            )
        }
    }
}

@Composable
private fun SettingTogglePremium(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131636), RoundedCornerShape(14.dp))
            .border(1.dp, if (checked) GameColors.Cyan.copy(0.25f) else Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GameColors.Cyan,
                    uncheckedThumbColor = Color(0xFF5A6488),
                    uncheckedTrackColor = Color(0xFF1A1F4A)
                )
            )
        }
    }
}

@Composable
private fun SettingDropdownPremium(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131636), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.White, fontSize = 13.sp)
            Box(
                modifier = Modifier
                    .background(Color.White.copy(0.06f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(value, color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
