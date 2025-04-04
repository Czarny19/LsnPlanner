package com.lczarny.lsnplanner.presentation.model

enum class DetailsScreenState {
    Loading,
    Create,
    Edit,
    Saving,
    Finished
}

enum class ListScreenState {
    Loading,
    List
}

enum class StartScreenState {
    Loading,
    FirstLaunch,
    UserNameSaved,
    StartApp
}