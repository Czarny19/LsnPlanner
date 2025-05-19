package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.lczarny.lsnplanner.R

@Composable
@Stable
fun ListItemTitle(modifier: Modifier = Modifier, text: String) {
    Text(text, modifier = modifier, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Composable
@Stable
fun ErrorSupportingText(error: InputError) {
    Text(
        text = when (error) {
            InputError.FieldRequired -> stringResource(R.string.error_field_required)
            is InputError.NumValueOutOfRange -> stringResource(R.string.error_number_not_in_range, error.minValue, error.maxValue)
            is InputError.CustomErrorMsg -> error.msg
        },
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
@Stable
fun LengthSupportingText(hint: String = "", value: String, maxLength: Int) {
    Text(
        text = if (hint.isNotEmpty()) "$hint | ${value.length} / $maxLength" else "${value.length} / $maxLength",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
    )
}