package com.attech.teacher.interfaces


import com.attech.teacher.models.AttendanceResponse
import com.attech.teacher.models.BatchesModel
import com.attech.teacher.models.CourseTeacherResponse
import com.attech.teacher.models.GetNewsModelResponse
import com.attech.teacher.models.LoginResponse
import com.attech.teacher.models.LogoutResponse
import com.attech.teacher.models.StudentDetailsResponse
import com.attech.teacher.models.TeacherClassesResponse
import com.attech.teacher.models.TeacherDetailsResponse
import com.attech.teacher.models.TestMarksResponse
import com.attech.teacher.models.UploadMarksResponse
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


    @POST("api/getBatchStudents")
    @FormUrlEncoded
    suspend fun getBatchStudents(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("bcode") bcode: String
    ): Response<List<StudentDetailsResponse>>


    @POST("api/getCourseTeacher")
    @FormUrlEncoded
    suspend fun getCourseTeacher(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("teacher") teacher: Int
    ): Response<List<CourseTeacherResponse>>


    @POST("api/markAttandance")
    @FormUrlEncoded
    suspend fun markAttendance(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("bcode") bcode: String,
        @Field("course") course: String,
        @Field("student") student: Int,
        @Field("date") date: String,
    ): Response<AttendanceResponse>



    @POST("api/uploadMarks")
    @FormUrlEncoded
    suspend fun uploadMarks(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") course: Int,
        @Field("student") student: Int,
        @Field("bcode") bcode: String,
        @Field("score") score: String,
    ): Response<UploadMarksResponse>


    @POST("api/getMarks")
    @FormUrlEncoded
    suspend fun getMarks(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") course: Int,
        @Field("student") student: Int,
        @Field("bcode") bcode: String,
    ): Response<TestMarksResponse>


    @POST("api/getTeacherClasses")
    @FormUrlEncoded
    suspend fun getTeacherClasses(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("id") id: Int
    ): Response<TeacherClassesResponse>


    @POST("api/getNewsEvents")
    @FormUrlEncoded
    suspend fun getNewsEvents(
        @Field("type") type: String,
        @Field("token") token: String
    ): Response<List<GetNewsModelResponse>>


}