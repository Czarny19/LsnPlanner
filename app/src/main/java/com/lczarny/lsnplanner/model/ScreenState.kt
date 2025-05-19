package com.lczarny.lsnplanner.model

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

enum class SignInScreenState {
    Loading,
    SignIn,
    Done
}