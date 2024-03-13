package com.example.fitnesstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.db.User
import com.example.fitnesstracker.db.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun signUpUser(user: User, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            val userName = user.name
            UserSession.username = userName
            UserSession.height = user.height
            UserSession.weight = user.weight
            UserSession.age = user.age
            UserSession.gender = user.gender
            onSuccess(userName)

        }
    }
}


