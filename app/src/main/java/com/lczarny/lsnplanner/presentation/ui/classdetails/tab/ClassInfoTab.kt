package com.lczarny.lsnplanner.presentation.ui.classdetails.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import com.lczarny.lsnplanner.database.model.ClassType
import com.lczarny.lsnplanner.database.model.planClassTypes
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.ColorPicker
import com.lczarny.lsnplanner.presentation.components.DisplayField
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.InputError
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.model.mapper.toLabel
import com.lczarny.lsnplanner.model.mapper.toPlanClassTypeIcon
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsViewModel

@Composable
fun ClassInfoTab(viewModel: ClassDetailsViewModel, newClass: Boolean) {
    val context = LocalContext.current

    val classInfo by viewModel.info.collectAsStateWithLifecycle()
    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()

    var nameTouched by remember { mutableStateOf(false) }

    lessonPlan?.let { lessonPlanData ->
        val planClassTypes by lazy { lessonPlanData.type.planClassTypes() }

        classInfo?.let { classInfoData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppPadding.MD_PADDING)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
            ) {
                if (newClass) {
                    OutlinedInputField(
                        label = stringResource(R.string.class_name),
                        onValueChange = { name ->
                            nameTouched = true
                            viewModel.updateName(name)
                        },
                        maxLines = 2,
                        maxLength = 40,
                        error = if (nameTouched && classInfoData.name.isEmpty()) InputError.FieldRequired else null
                    )

                    OutlinedDropDown(
                        label = stringResource(R.string.class_type),
                        initialValue = classInfoData.type.let { DropDownItem(it, it.toLabel(context)) },
                        onValueChange = { type -> viewModel.updateClassType(type.value as ClassType) },
                        items = planClassTypes.map { DropDownItem(it, it.toLabel(context)) }
                    )
                } else {
                    DisplayField(
                        label = stringResource(R.string.class_name),
                        text = classInfoData.name,
                        icon = { Icon(AppIcons.CLASS, contentDescription = stringResource(R.string.class_name)) }
                    )

                    classInfoData.type.let {
                        val label = it.toLabel(context)
                        DisplayField(
                            label = stringResource(R.string.class_type),
                            text = label,
                            icon = { Icon(it.toPlanClassTypeIcon(), contentDescription = label) }
                        )
                    }
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
            }
        }
    }
}