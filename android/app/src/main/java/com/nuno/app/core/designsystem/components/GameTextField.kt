package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    height: Dp = 48.dp,
    singleLine: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = GameColors.TextDark,
                fontSize = 13.sp
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth().height(height),
        singleLine = singleLine,
        shape = RoundedCornerShape(GameDimens.radiusMd),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = GameColors.TextWhite,
            unfocusedTextColor = GameColors.TextWhite,
            cursorColor = GameColors.Gold,
            focusedBorderColor = GameColors.Gold,
            unfocusedBorderColor = GameColors.BorderPurple,
            focusedLabelColor = GameColors.Gold,
            unfocusedLabelColor = GameColors.TextGray,
            focusedLeadingIconColor = GameColors.Gold,
            unfocusedLeadingIconColor = GameColors.TextGray,
            focusedTrailingIconColor = GameColors.Gold,
            unfocusedTrailingIconColor = GameColors.TextGray,
            focusedPlaceholderColor = GameColors.TextDark,
            unfocusedPlaceholderColor = GameColors.TextDark
        ),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = GameColors.TextWhite,
            fontSize = 13.sp
        )
    )
}