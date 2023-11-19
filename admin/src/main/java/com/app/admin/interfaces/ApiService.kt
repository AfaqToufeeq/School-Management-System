package com.app.admin.interfaces

import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {
    @POST("api/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("type") type: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse


    @POST("api/logout")
    @FormUrlEncoded
    suspend fun logout(
        @Field("type") type: String,
        @Field("token") token: String,
    ): LogoutResponse



    //Fetch list of students
    @POST("api/getStudents")
    @FormUrlEncoded
    suspend fun getStudents(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<StudentDetailsResponse>>

//    @POST("api/addStudent")
//    suspend fun addStudent(
//        @Header("type") type: String,
//        @Header("token") token: String,
//        @Query("firstname") firstName: String,
//        @Query("lastname") lastName: String,
//        @Query("rollno") rollNo: String,
//        @Query("contact") contact: String,
//        @Query("nic") nic: String,
//        @Query("address") address: String,
//        @Query("username") username: String,
//        @Query("password") password: String
//    ): Response<StudentResponse>


    @POST("api/addStudent")
    @FormUrlEncoded
    fun addStudent(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("rollno") rollno: String,
        @Field("contact") contact: String,
        @Field("nic") nic: String,
        @Field("address") address: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<StudentDetailsResponse>
}