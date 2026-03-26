package com.bof.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bof.mobile.data.repository.DashboardRepository
import com.bof.mobile.model.ApiResult
import com.bof.mobile.model.DashboardResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val data: DashboardResponse? = null,
    val errorMessage: String? = null
)

class DashboardViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun loadDashboard(customerId: Int) {
        if (customerId <= 0) {
            _uiState.update { it.copy(errorMessage = "Invalid customer id") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = dashboardRepository.getDashboard(customerId)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, data = result.data, errorMessage = null) }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }
}
