package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun GameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = GameColors.TextDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color.Black.copy(0.4f)),
        singleLine = singleLine,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = GameColors.SurfaceCard,
            unfocusedContainerColor = GameColors.Surface,
            disabledContainerColor = GameColors.Surface.copy(0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White.copy(0.9f),
            disabledTextColor = GameColors.TextDark,
            cursorColor = GameColors.Gold,
            focusedBorderColor = GameColors.Gold.copy(0.8f),
            unfocusedBorderColor = Color.White.copy(0.08f),
            disabledBorderColor = Color.White.copy(0.04f),
            focusedLabelColor = GameColors.Gold,
            unfocusedLabelColor = GameColors.TextGray,
            focusedLeadingIconColor = GameColors.Gold,
            unfocusedLeadingIconColor = GameColors.TextGray,
            focusedTrailingIconColor = GameColors.Gold,
            unfocusedTrailingIconColor = GameColors.TextGray,
            focusedPlaceholderColor = GameColors.TextDark,
            unfocusedPlaceholderColor = GameColors.TextDark
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    )
}

@Composable
fun PremiumSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    GameTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        height = 44.dp,
        leadingIcon = leadingIcon
    )
}
