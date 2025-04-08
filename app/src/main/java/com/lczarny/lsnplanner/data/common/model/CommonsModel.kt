package com.lczarny.lsnplanner.data.common.model

data class VarArgsId(val id: Long)

enum class ItemState {
    New,
    Existing,
    ToBeDeleted
}

enum class Importance(val raw: Int) {
    VeryHigh(3),
    High(2),
    Normal(1);

    companion object {
        fun from(find: Int): Importance = Importance.entries.find { it.raw == find } ?: Normal
    }
}