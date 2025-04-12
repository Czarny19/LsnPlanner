package com.lczarny.lsnplanner.presentation.model

enum class BasicScreenState {
    Loading,
    Ready
}

enum class DetailsScreenState {
    Loading,
    Create,
    Edit,
    Saving,
    Finished
}

enum class StartScreenState {
    Loading,
    FirstLaunch,
    UserNameSaved,
    StartApp
}