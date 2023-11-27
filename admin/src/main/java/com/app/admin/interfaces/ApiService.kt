package com.app.admin.interfaces

import com.app.admin.models.BatchResponse
import com.app.admin.models.BatchStudentsResponse
import com.app.admin.models.BatchesModel
import com.app.admin.models.FResponce
import com.app.admin.models.FeeDetailResponse
import com.app.admin.models.FeeModel
import com.app.admin.models.FeeResponse
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import com.app.admin.models.TeacherDetailsResponse
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


    @POST("api/getStudents")
    @FormUrlEncoded
    suspend fun getStudents(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<StudentDetailsResponse>>


    @POST("api/getTeachers")
    @FormUrlEncoded
    suspend fun getTeachers(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<TeacherDetailsResponse>>


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
        @Field("password") password: String,
        @Field("image") image: String
    ): Call<StudentDetailsResponse>


    @POST("api/addFinancePerson")
    @FormUrlEncoded
    fun addFinancePerson(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: String,
    ): Response<FResponce>


    @POST("api/addTeacher")
    @FormUrlEncoded
    fun addTeacher(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("contact") contact: String,
        @Field("nic") nic: String,
        @Field("address") address: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("image") image: String
    ): Call<TeacherDetailsResponse>


    @POST("api/getBatches")
    @FormUrlEncoded
    suspend fun getBatches(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<BatchesModel>>


    @FormUrlEncoded
    @POST("api/addBatchStudents")
    suspend fun addBatchStudents(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("bcode") bcode: String,
        @Field("students[]") students: List<Int>,
        @Field("teachers[]") teachers: List<Int>,
    ): Response<BatchStudentsResponse>

    @FormUrlEncoded
    @POST("api/payFee")
    suspend fun payFee(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("student") student: Int,
        @Field("date") date: String,
    ): Response<FeeResponse>


    @FormUrlEncoded
    @POST("api/getFeeDetails")
    suspend fun getFeeDetails(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("student") student: Int
    ): Response<List<FeeDetailResponse>>


    @FormUrlEncoded
    @POST("api/addBatch")
    suspend fun addBatch(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("bcode") bcode: String,
    ): Response<BatchResponse>
}