package com.lczarny.lsnplanner.presentation.ui.classdetails.tab

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsViewModel

@Composable
fun ClassTestsTab(viewModel: ClassDetailsViewModel) {
    val exams by viewModel.exams.collectAsStateWithLifecycle()

    Text("TBD")
}