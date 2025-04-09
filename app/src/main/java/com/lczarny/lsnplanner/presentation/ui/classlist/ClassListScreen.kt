package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.model.ListScreenState
import com.lczarny.lsnplanner.presentation.ui.classlist.components.ClassList

enum class ClassListScreenSnackbar {
    Deleted
}

@Composable
fun ClassListScreen(navController: NavController, viewModel: ClassListViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            ListScreenState.Loading -> FullScreenLoading(stringResource(R.string.please_wait))
            ListScreenState.List -> ClassList(navController, viewModel)
        }
    }
}