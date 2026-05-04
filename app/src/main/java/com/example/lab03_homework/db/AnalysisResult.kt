package com.example.lab03_homework.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analyses")
data class AnalysisResult(
    @PrimaryKey val imageId: Int,
    val tags: String
)