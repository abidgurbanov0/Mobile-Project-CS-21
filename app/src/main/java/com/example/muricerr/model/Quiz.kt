package com.example.muricerr.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.muricerr.database.PlaylistEntity

@Entity(
    tableName = "quizzes",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val playlistId: Long
)
