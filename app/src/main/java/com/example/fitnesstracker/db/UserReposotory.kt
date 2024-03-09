package com.example.fitnesstracker.db

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getUser(): Flow<List<User>> {
        return userDao.getUser()
    }
}
