package com.mobile.rxjava2andretrofit2.kotlin.resources.bean

data class ResourcesBean(
    val error: Boolean,
    val results: List<Result>
)

data class Result(
    val _id: String,
    val createdAt: String,
    val desc: String,
    val images: List<String>,
    val publishedAt: String,
    val source: String,
    val type: String,
    val url: String,
    val used: Boolean,
    val who: String
)