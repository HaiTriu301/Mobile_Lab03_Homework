package com.example.lab03_homework.model

import com.google.gson.annotations.SerializedName

data class PixabayResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageItem>
)