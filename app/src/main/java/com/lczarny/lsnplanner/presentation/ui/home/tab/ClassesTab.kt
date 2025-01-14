package com.lczarny.lsnplanner.presentation.ui.home.tab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import kotlin.collections.get

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassesTab(padding: PaddingValues, viewModel: HomeViewModel, classes: List<PlanClassModel>) {
    val classesPerWeekDay = mutableMapOf<Int, List<PlanClassModel>>().apply {
        for (i in 0..6) {
            this.put(i, mutableListOf())
        }
    }

    classes.filter { it.isCyclical }.forEach {
        classesPerWeekDay[it.weekDay]!! + it
    }

    val currentDay = 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(AppPadding.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppPadding.listItemPadding)
    ) {
        items(items = classesPerWeekDay[currentDay]!!) { planClass ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
                content = {
                    Column(
                        modifier = Modifier.padding(AppPadding.screenPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {

                    }
                }
            )
        }
    }
}