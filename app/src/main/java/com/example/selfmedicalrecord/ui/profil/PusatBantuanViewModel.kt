package com.example.selfmedicalrecord.ui.profil

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.example.selfmedicalrecord.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Satu pertanyaan FAQ yang siap ditampilkan. */
data class FaqUi(val id: Int, val question: String, val answer: String, val expanded: Boolean)

data class PusatBantuanUiState(
    val query: String = "",
    val items: List<FaqUi> = emptyList()
)

/**
 * Daftar FAQ saat ini tetap (dari string resource). Memakai AndroidViewModel
 * hanya supaya bisa membaca teks resource untuk pencarian.
 */
class PusatBantuanViewModel(application: Application) : AndroidViewModel(application) {

    private class Faq(val id: Int, @StringRes val question: Int, @StringRes val answer: Int)

    private val faqs = listOf(
        Faq(1, R.string.faq_q1, R.string.faq_a1),
        Faq(2, R.string.faq_q2, R.string.faq_a2),
        Faq(3, R.string.faq_q3, R.string.faq_a3),
        Faq(4, R.string.faq_q4, R.string.faq_a4)
    )

    private val query = MutableStateFlow("")
    private val expandedId = MutableStateFlow<Int?>(1) // pertanyaan pertama terbuka, sesuai desain

    private val _uiState = MutableStateFlow(build())
    val uiState: StateFlow<PusatBantuanUiState> = _uiState.asStateFlow()

    fun onQueryChanged(value: String) {
        query.value = value
        _uiState.update { build() }
    }

    /** Membuka satu pertanyaan; mengetuk yang sedang terbuka akan menutupnya. */
    fun onToggle(id: Int) {
        expandedId.value = if (expandedId.value == id) null else id
        _uiState.update { build() }
    }

    private fun build(): PusatBantuanUiState {
        val app = getApplication<Application>()
        val q = query.value.trim()
        val items = faqs.map { FaqUi(it.id, app.getString(it.question), app.getString(it.answer), it.id == expandedId.value) }
            .filter { q.isEmpty() || it.question.contains(q, ignoreCase = true) || it.answer.contains(q, ignoreCase = true) }
        return PusatBantuanUiState(query = query.value, items = items)
    }
}
