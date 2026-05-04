package com.example.lab03_homework.model

import com.google.gson.annotations.SerializedName

data class ImageItem(
    val id: Int,
    val webformatURL: String,
    val tags: String
)