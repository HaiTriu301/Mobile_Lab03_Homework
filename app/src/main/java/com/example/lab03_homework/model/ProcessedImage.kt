package com.example.lab03_homework.model

import com.google.gson.annotations.SerializedName

data class ProcessedImage(
    val image: ImageItem,
    val aiTags: List<String>
)