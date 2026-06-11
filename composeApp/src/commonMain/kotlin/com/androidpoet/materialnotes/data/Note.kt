package com.androidpoet.materialnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var date: String = "",
    var backround: Int = 0,
    var content: String = "",
)
