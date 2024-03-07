package com.example.fitnesstracker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ApiResponse (
    val name: String,
    val exercises: String
)
    data class Reply (
        val batchcomplete: String, val replyContinue: Continue, val query: Query
            )
    data class Query(
        val searchinfo: Searchinfo, val search: List<TestItem>
    )

    data class Search(
        val name: String,
        val exerciseProgram: String

    )

    data class Searchinfo(
        val exercises: String, val suggestion: String, val suggestionsnippet: String
    )

    data class Continue(
        val sroffset: Int, val continueContinue: String
    )
class ProgramResponse : ArrayList<TestItem>()

data class TestItem(
    val exercises: List<String>,
    val name: String
)


object ApiData {
    private const val BASE_URL = "https://users.metropolia.fi/~christio/ProjectX/"
    interface Service {
        @GET("Programs")
        suspend fun getPrograms(@retrofit2.http.Query(".json") action:String): List<TestItem>
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: Service by lazy {
        retrofit.create(Service::class.java)
    }
}