package com.lczarny.lsnplanner.data.common.model

data class AppSettings(
    val initialized: Boolean = false,
    val homeClassesViewType: String? = null,
    val tutorials: Tutorials = Tutorials()
)

data class Tutorials(
    val noteListSwipeDone: Boolean = false,
    val noteImportanceDone: Boolean = false
)
