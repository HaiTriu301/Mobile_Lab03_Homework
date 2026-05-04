package com.example.lab03_homework.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lab03_homework.api.PixabayApiService
import com.example.lab03_homework.db.AnalysisResult
import com.example.lab03_homework.db.ImageAnalysisDao
import com.example.lab03_homework.ml.ImageAnalyzer
import com.example.lab03_homework.model.ProcessedImage

class PixabayPagingSource(
    private val api: PixabayApiService,
    private val query: String,
    private val apiKey: String,
    private val analyzer: ImageAnalyzer,
    private val dao: ImageAnalysisDao
) : PagingSource<Int, ProcessedImage>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProcessedImage> {
        val page = params.key ?: 1
        return try {
            val response = api.getImages(apiKey, query, page, params.loadSize)

            // Xử lý từng ảnh để lấy AI Tags
            val processedImages = response.hits.map { imageItem ->
                // Kiểm tra cache trong Room Database[cite: 1]
                val cachedTagsStr = dao.getAnalysis(imageItem.id)
                val aiTags = if (cachedTagsStr != null) {
                    cachedTagsStr.split(",")
                } else {
                    // Nếu chưa có cache, chạy ML Kit[cite: 1]
                    val tags = analyzer.analyzeImage(imageItem.webformatURL)
                    // Lưu lại vào cache
                    if (tags.isNotEmpty()) {
                        dao.insertAnalysis(AnalysisResult(imageItem.id, tags.joinToString(",")))
                    }
                    tags
                }
                ProcessedImage(imageItem, aiTags)
            }

            LoadResult.Page(
                data = processedImages,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.hits.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProcessedImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}