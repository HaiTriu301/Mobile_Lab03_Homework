package com.example.lab03_homework.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.lab03_homework.api.PixabayApiService
import com.example.lab03_homework.data.PixabayPagingSource
import com.example.lab03_homework.db.AppDatabase
import com.example.lab03_homework.ml.ImageAnalyzer
import com.example.lab03_homework.model.ProcessedImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val api = PixabayApiService.create()
    private val analyzer = ImageAnalyzer(application)
    private val dao = AppDatabase.getDatabase(application).imageAnalysisDao()

    // Thay bằng API Key của bạn lấy từ Pixabay[cite: 1]
    private val API_KEY = "55713483-54cfbb97b2d2ce52ba0985d5a"

    val searchQuery = MutableStateFlow("")

    // Khởi tạo luồng dữ liệu Paging
    private val imagesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PixabayPagingSource(api, "nature", API_KEY, analyzer, dao) }
    ).flow.cachedIn(viewModelScope)

    // Lọc hình ảnh theo AI tags (Smart Search)[cite: 1]
    val filteredImages: Flow<PagingData<ProcessedImage>> = imagesFlow.combine(searchQuery) { pagingData, query ->
        if (query.isBlank()) {
            pagingData
        } else {
            pagingData.filter { processedImage ->
                processedImage.aiTags.any { it.contains(query, ignoreCase = true) }
            }
        }
    }
}