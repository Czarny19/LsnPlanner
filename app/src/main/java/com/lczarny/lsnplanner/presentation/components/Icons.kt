package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
fun WelcomeIcon(modifier: Modifier = Modifier) = Icon(
    Icons.Outlined.WavingHand,
    modifier = modifier.size(AppSizes.XL_ICON),
    contentDescription = stringResource(R.string.first_launch_welcome),
    tint = MaterialTheme.colorScheme.primary,
)

@Composable
fun SaveIcon() = Icon(Icons.Filled.Save, contentDescription = stringResource(R.string.save))

@Composable
fun BackIcon() = Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))

@Composable
fun OptionsMenuIcon() = Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.options))

@Composable
fun DropDownIcon() = Icon(Icons.Outlined.ArrowDropDown, contentDescription = stringResource(R.string.drop_down_arrow))

@Composable
fun InfoIcon(modifier: Modifier = Modifier) = Icon(
    Icons.Outlined.Info,
    contentDescription = stringResource(R.string.information),
    modifier = modifier.size(AppSizes.LG_ICON),
)

@Composable
fun SelecteDateIcon() = Icon(Icons.Outlined.DateRange, stringResource(R.string.select_date))

@Composable
fun InputDropDownIcon(modifier: Modifier = Modifier, expanded: Boolean) = Icon(
    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
    contentDescription = stringResource(R.string.show_options),
    modifier = modifier
)

@Composable
fun AddFirstItemIcon() = Icon(
    Icons.Outlined.Add,
    contentDescription = stringResource(R.string.add_first_item),
    modifier = Modifier.size(AppSizes.XL_ICON),
    tint = MaterialTheme.colorScheme.primary
)

@Composable
fun PlanTypeIcon(modifier: Modifier = Modifier) =
    Icon(Icons.Filled.School, modifier = modifier, contentDescription = stringResource(R.string.plan_type))

@Composable
fun PlanCreateDateIcon(modifier: Modifier = Modifier) =
    Icon(Icons.Filled.EditCalendar, modifier = modifier, contentDescription = stringResource(R.string.plan_type))


@Composable
fun PlanActiveIcon(modifier: Modifier = Modifier, active: Boolean) = Icon(
    if (active) Icons.Filled.Check else Icons.Filled.Close,
    modifier = modifier,
    contentDescription = stringResource(if (active) R.string.plan_is_active else R.string.plan_is_not_active)
)

@Composable
fun PlanSelectedIcon(active: Boolean) = Icon(
    Icons.Filled.CheckCircle,
    contentDescription = stringResource(R.string.plan_active),
    tint = if (active) MaterialTheme.colorScheme.tertiary else Color.Transparent
)

@Composable
fun PlanSetActiveIcon() = Icon(Icons.Outlined.CheckCircle, contentDescription = stringResource(R.string.plan_make_active))

@Composable
fun PlanEditIcon() = Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.plan_edit))

@Composable
fun PlanDeleteIcon() = Icon(Icons.Outlined.Delete, contentDescription = stringResource(R.string.plan_delete))

@Composable
fun PlanAddIcon() = Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.plan_add))