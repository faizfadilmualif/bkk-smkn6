
package com.faiz.bkksmkn6.data

import android.adservices.common.AdData
import com.google.gson.annotations.SerializedName

data class Meta(
    val page: Int = 1,
    @SerializedName("per_page")
    val perPage: Int = 10,
    val total: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 1
)

data class ListResponse<T>(val data: List<T>?,val meta: Meta?)

data class DetailResponse<T>(
    val data: T
)

data class PerusahaanMini(
    val id: Int = 0,
    val slug: String? = null,
    val name: String? = null,
    @SerializedName("logo_url")
    val logoUrl: String? = null
)

data class Lowongan(
    val id: Int = 0,
    val slug: String? = null,
    val url: String? = null,
    val title: String? = null,
    val company: PerusahaanMini? = null,
    val type: String? = null,
    val field: String? = null,
    val location: String? = null,
    @SerializedName("salary_text")
    val salaryText: String? = null,
    val deadline: String? = null,
    @SerializedName("is_open")
    val isOpen: Boolean? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    val summary: String? = null,


    @SerializedName("description_html")
    val descriptionHtml: String? = null,
    @SerializedName("description_text")
    val descriptionText: String? = null,
    @SerializedName("requirements_html")
    val requirementsHtml: String? = null,
    @SerializedName("requirements_text")
    val requirementsText: String? = null,
    @SerializedName("how_to_apply_html")
    val howToApplyHtml: String? = null,
    @SerializedName("how_to_apply_text")
    val howToApplyText: String? = null
)

data class Berita(
    val id: Int = 0,
    val slug: String? = null,
    val url: String? = null,
    val title: String? = null,

    @SerializedName(
        value = "summary",
        alternate = ["excerpt", "description"]
    )
    val summary: String? = null,

    @SerializedName(
        value = "thumb_url",
        alternate = ["image_url", "thumbnail_url"]
    )
    val thumbUrl: String? = null,

    @SerializedName(
        value = "published_at",
        alternate = ["created_at", "date"]
    )
    val publishedAt: String? = null,

    // Hanya tersedia pada detail berita
    @SerializedName("body_html")
    val bodyHtml: String? = null,
    @SerializedName("body_text")
    val bodyText: String? = null
)

data class Perusahaan(
    val id: Int = 0,
    val slug: String? = null,
    val url: String? = null,
    val name: String? = null,

    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName(
        value = "city",
        alternate = ["location", "kota"]
    )
    val city: String? = null,

    @SerializedName(
        value = "open_jobs_count",
        alternate = ["jobs_count", "active_jobs_count"]
    )
    val openJobsCount: Int = 0,


    @SerializedName("description_html")
    val descriptionHtml: String? = null,
    @SerializedName("description_text")
    val descriptionText: String? = null,

    val jobs: List<Lowongan>? = null
)
