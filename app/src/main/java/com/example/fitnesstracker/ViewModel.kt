package com.example.fitnesstracker

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.fitnesstracker.ApiData.service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class ViewModel : ViewModel() {
    private val repository : SrRepository = SrRepository()
    var search : String by mutableStateOf("")
    var uiState : String by mutableStateOf("")
    private set
    class SrRepository {
        suspend fun hitCountCheck(name: String):ApiResponse.Reply{
            return service.getPrograms(name)
        }
    }
    fun getPrograms(name: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                search = name
                val serverResp = repository.hitCountCheck(name)
                Log.d("DBG",serverResp.toString())
                println(serverResp)
                println(serverResp.query.searchinfo.exercises)
                uiState = serverResp.query.searchinfo.exercises
            } catch (e: Exception){
                println(e.stackTrace)
            }
        }
    }
}