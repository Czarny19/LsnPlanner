package com.lczarny.lsnplanner.presentation.ui.home.tab.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.model.mapper.ClassViewType
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel

@Composable
fun ClassesTab(padding: PaddingValues, viewModel: HomeViewModel, pagerState: PagerState) {
    val classesDisplayType by viewModel.classesDisplayType.collectAsStateWithLifecycle()
    val classesLoading by viewModel.classesLoading.collectAsStateWithLifecycle()

    if (classesLoading) {
        FullScreenLoading()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ClassesTabTopNav(viewModel, pagerState)

        when (classesDisplayType) {
            ClassViewType.List -> ClassesTabList(viewModel, pagerState)
            ClassViewType.Timeline -> ClassesTabTimeline()
        }
    }
}