
package com.faiz.bkksmkn6.util

import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import androidx.core.text.HtmlCompat
import coil.load
import com.faiz.bkksmkn6.R
import java.io.IOException
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import retrofit2.HttpException

/**
 * Mengubah error menjadi pesan yang mudah dipahami pengguna.
 */
object ApiError {

    fun message(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401 -> "Kunci API tidak valid atau sudah dinonaktifkan."
                    404 -> "Data tidak ditemukan."
                    429 -> "Terlalu banyak permintaan. Tunggu sebentar lalu coba lagi."
                    else -> "Terjadi kesalahan di server (kode ${e.code()})."
                }
            }

            is IOException -> "Tidak ada koneksi internet."
            else -> "Terjadi kesalahan: ${e.message ?: "Tidak diketahui"}"
        }
    }
}

private val localeId = Locale("id", "ID")

/**
 * Mengubah tanggal menjadi format Indonesia.
 * Contoh: 2026-10-14T07:44:00+07:00 -> 14 Okt 2026
 */
fun String?.tanggalIndo(): String? {
    if (this.isNullOrBlank()) return null

    val fmt = DateTimeFormatter.ofPattern("d MMM yyyy", localeId)

    return try {
        OffsetDateTime.parse(this).format(fmt)
    } catch (e: Exception) {
        try {
            LocalDate.parse(this.take(10)).format(fmt)
        } catch (e2: Exception) {
            this
        }
    }
}

/**
 * Menampilkan gambar dari URL.
 * Jika URL kosong atau gagal, tampilkan placeholder.
 */
fun ImageView.loadImage(url: String?) {
    if (url.isNullOrBlank()) {
        setImageResource(R.drawable.bg_placeholder)
    } else {
        load(url) {
            crossfade(true)
            placeholder(R.drawable.bg_placeholder)
            error(R.drawable.bg_placeholder)
        }
    }
}

/**
 * Mengisi TextView atau menyembunyikannya jika kosong.
 */
fun TextView.setTextOrHide(value: String?) {
    if (value.isNullOrBlank()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        text = value
    }
}

/**
 * Menampilkan teks HTML jika tersedia.
 * Jika tidak, gunakan teks biasa.
 */
fun TextView.setRichText(html: String?, plain: String?) {
    when {
        !html.isNullOrBlank() -> {
            visibility = View.VISIBLE
            text = HtmlCompat.fromHtml(
                html,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            ).trimEnd()

            movementMethod = LinkMovementMethod.getInstance()
        }

        !plain.isNullOrBlank() -> {
            visibility = View.VISIBLE
            text = plain.trim()
            movementMethod = null
        }

        else -> {
            visibility = View.GONE
            text = ""
            movementMethod = null
        }
    }
}
