package com.app.admin.interfaces

import com.app.admin.models.AdminRemoveResponse
import com.app.admin.models.BatchResponse
import com.app.admin.models.BatchStudentsResponse
import com.app.admin.models.BatchesModel
import com.app.admin.models.CoursesResponse
import com.app.admin.models.FeeDetailResponse
import com.app.admin.models.FeeResponse
import com.app.admin.models.FinanceModel
import com.app.admin.models.FinanceResponse
import com.app.admin.models.GetCourseResponse
import com.app.admin.models.GetNewsModelResponse
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import com.app.admin.models.NewsModelResponse
import com.app.admin.models.TeacherCourseResponse
import com.app.admin.models.TeacherDetailsResponse
import org.json.JSONArray
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


    @POST("api/getFinancePerson")
    @FormUrlEncoded
    suspend fun getFinancePerson(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<FinanceModel>>


    @POST("api/addStudent")
    @FormUrlEncoded
    suspend fun addStudent(
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
    ): Response<StudentDetailsResponse>


    @POST("api/addFinancePerson")
    @FormUrlEncoded
    suspend fun addFinancePerson(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("image") image: String
    ): Response<FinanceResponse>


    @POST("api/addTeacher")
    @FormUrlEncoded
    suspend fun addTeacher(
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
    ): Response<TeacherDetailsResponse>


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
        @Field("students") students: JSONArray,
        @Field("teachers") teachers: JSONArray
    ): Response<BatchStudentsResponse>


    @FormUrlEncoded
    @POST("api/addCourseTeacher")
    suspend fun addCourseTeacher(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") course: Int,
        @Field("teacher") teacher: Int
    ): Response<TeacherCourseResponse>


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


    @FormUrlEncoded
    @POST("api/addNewsEvents")
    suspend fun addNewsEvents(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("date") date: String,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("image") image: String,
    ): Response<NewsModelResponse>


    @POST("api/getNewsEvents")
    @FormUrlEncoded
    suspend fun getNewsEvents(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<GetNewsModelResponse>>


    @POST("api/deleteData")
    @FormUrlEncoded
    suspend fun deleteData(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("id") id: Int,
        @Field("del") del: String,
    ): Response<AdminRemoveResponse>


    @POST("api/addCourse")
    @FormUrlEncoded
    suspend fun addCourse(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("code") code: String,
        @Field("max_marks") max_marks: Int,
    ): Response<CoursesResponse>


    @POST("api/getCourses")
    @FormUrlEncoded
    suspend fun getCourses(
        @Field("type") type: String,
        @Field("token") token: String,
    ): Response<List<GetCourseResponse>>


}