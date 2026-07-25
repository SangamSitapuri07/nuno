package com.nuno.app.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun SettingRow(
    icon: ImageVector,
    label: String,
    description: String? = null,
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(SurfaceDark, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = AccentCyan, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            if (description != null) {
                Text(text = description, color = TextSecondary, fontSize = 11.sp)
            }
        }

        trailingContent()
    }
}

@Composable
fun SettingSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AccentCyan,
            checkedTrackColor = PrimaryBlue,
            uncheckedThumbColor = TextTertiary,
            uncheckedTrackColor = SurfaceLight
        )
    )
}

@Composable
fun SettingSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(180.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = AccentCyan,
                activeTrackColor = PrimaryBlue,
                inactiveTrackColor = SurfaceDark
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "${value.toInt()}",
            color = AccentGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(30.dp)
        )
    }
}

@Composable
fun SettingDropdown(
    value: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.KeyboardArrowRight, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title.uppercase(),
            color = AccentCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(bottom = 6.dp, start = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}