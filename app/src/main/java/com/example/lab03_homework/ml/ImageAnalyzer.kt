package com.example.lab03_homework.ml

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class ImageAnalyzer(private val context: Context) {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    private val imageLoader = ImageLoader(context)

    suspend fun analyzeImage(imageUrl: String): List<String> = coroutineScope {
        try {
            // Tải ảnh bằng Coil[cite: 1]
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = imageLoader.execute(request)
            val bitmap = result.drawable?.toBitmap() ?: return@coroutineScope emptyList()

            // Phân tích bằng ML Kit[cite: 1]
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await() // Cần kotlinx-coroutines-play-services

            // Trả về top 3 nhãn[cite: 1]
            labels.take(3).map { it.text }
        } catch (e: Exception) {
            emptyList()
        }
    }
}