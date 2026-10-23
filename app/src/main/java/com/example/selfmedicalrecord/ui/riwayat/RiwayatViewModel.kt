package com.example.selfmedicalrecord.ui.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfmedicalrecord.data.record.FakeMedicalRecordRepository
import com.example.selfmedicalrecord.data.record.MedicalRecord
import com.example.selfmedicalrecord.data.record.MedicalRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RiwayatUiState(
    val isLoading: Boolean = true,
    val records: List<MedicalRecord> = emptyList(),
    val hasError: Boolean = false
)

class RiwayatViewModel(
    // TODO: ambil dari ServiceLocator setelah branch self-assessment di-merge.
    private val repository: MedicalRecordRepository = FakeMedicalRecordRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RiwayatUiState())
    val uiState: StateFlow<RiwayatUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = RiwayatUiState(isLoading = true)
        viewModelScope.launch {
            repository.getRecords()
                .onSuccess { list ->
                    // Terbaru di atas.
                    _uiState.value = RiwayatUiState(
                        isLoading = false,
                        records = list.sortedByDescending { it.visitDateMillis }
                    )
                }
                .onFailure {
                    _uiState.value = RiwayatUiState(isLoading = false, hasError = true)
                }
        }
    }
}
