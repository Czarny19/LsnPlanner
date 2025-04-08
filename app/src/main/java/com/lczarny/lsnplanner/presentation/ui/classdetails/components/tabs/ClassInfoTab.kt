package com.lczarny.lsnplanner.presentation.ui.classdetails.components.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.ClassType
import com.lczarny.lsnplanner.data.common.model.planClassTypes
import com.lczarny.lsnplanner.presentation.components.ClassIcon
import com.lczarny.lsnplanner.presentation.components.ColorPicker
import com.lczarny.lsnplanner.presentation.components.DisplayField
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.mapper.planClassTypeLabelMap
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsViewModel

@Composable
fun ClassInfoTab(viewModel: ClassDetailsViewModel, newClass: Boolean) {
    val context = LocalContext.current

    val classInfo by viewModel.info.collectAsStateWithLifecycle()
    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    var nameTouched by remember { mutableStateOf(false) }

    lessonPlan?.let { lessonPlanData ->
        val planClassTypeLabelMap by lazy { lessonPlanData.type.planClassTypeLabelMap(context) }

        classInfo?.let { classInfoData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.SCREEN_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                if (newClass) {
                    OutlinedInputField(
                        label = stringResource(R.string.class_name),
                        onValueChange = { name ->
                            nameTouched = true
                            viewModel.updateName(name)
                        },
                        maxLines = 3,
                        maxLength = 100,
                        error = if (nameTouched && classInfoData.name.isEmpty()) InputError.FieldRequired else null
                    )

                    OutlinedDropDown(
                        label = stringResource(R.string.class_type),
                        initialValue = DropDownItem(classInfoData.type, planClassTypeLabelMap.getValue(classInfoData.type)),
                        onValueChange = { type -> viewModel.updateClassType(type.value as ClassType) },
                        items = lessonPlanData.type.planClassTypes().map { DropDownItem(it, planClassTypeLabelMap.getValue(it)) }
                    )
                } else {
                    DisplayField(
                        modifier = Modifier.padding(bottom = AppPadding.INPUT_BOTTOM_PADDING),
                        label = stringResource(R.string.class_type),
                        text = planClassTypeLabelMap.getValue(classInfoData.type),
                        icon = { ClassIcon(stringResource(R.string.class_type)) }
                    )
                }

                ColorPicker(
                    initialColorHex = classInfoData.color,
                    label = stringResource(R.string.class_color),
                    onColorSelect = { color -> viewModel.updateClassColor(color) }
                )

                OutlinedInputField(
                    label = stringResource(R.string.class_note),
                    initialValue = classInfoData.note ?: "",
                    onValueChange = { name -> viewModel.updateNote(name) },
                    minLines = 3,
                    maxLines = 10,
                    maxLength = 500,
                )

                Spacer(modifier = Modifier.weight(1.0f))

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppPadding.MD_PADDING),
                    enabled = saveEnabled,
                    text = stringResource(R.string.plan_save),
                    onClick = { viewModel.saveClass() }
                )
            }
        }
    }
}