package com.lczarny.lsnplanner.database.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable

@Immutable
data class VarArgsId(val id: Long)

enum class ItemState {
    New,
    Existing,
    ToBeDeleted
}

@Keep
enum class Importance(val raw: Int) {
    VeryHigh(3),
    High(2),
    Normal(1);

    companion object {
        fun from(find: Int): Importance = Importance.entries.find { it.raw == find } ?: Normal
    }
}