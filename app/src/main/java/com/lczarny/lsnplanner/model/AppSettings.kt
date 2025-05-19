package com.lczarny.lsnplanner.model

data class AppSettings(
    val homeClassesViewType: String? = null,
    val tutorials: Tutorials = Tutorials()
)

data class Tutorials(
    val noteListSwipeDone: Boolean = false,
    val noteImportanceDone: Boolean = false
)

enum class ThemeType {
    Light,
    Dark,
    System
}