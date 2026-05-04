package com.example.lab03_homework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageAnalysisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(result: AnalysisResult)

    @Query("SELECT tags FROM analyses WHERE imageId = :id")
    suspend fun getAnalysis(id: Int): String?
}