package com.attech.sms.interfaces

import com.attech.sms.models.AttendanceResponse
import com.attech.sms.models.BatchesModel
import com.attech.sms.models.CourseTeacherResponse
import com.attech.sms.models.GetAttendanceModelResponse
import com.attech.sms.models.GetCourseResponse
import com.attech.sms.models.LoginResponse
import com.attech.sms.models.LogoutResponse
import com.attech.sms.models.StudentClassAndCoursesResponse
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.TeacherDetailsResponse
import com.attech.sms.models.TestMarksResponse
import com.attech.sms.models.UploadMarksResponse
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
        @Field("image") image: String,
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
    suspend fun getTeacherCourses(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("teacher") teacher: Int
    ): Response<List<CourseTeacherResponse>>


    @POST("api/getCourseTeacher")
    @FormUrlEncoded
    suspend fun getCourseTeachers(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") teacher: Int
    ): Response<List<TeacherDetailsResponse>>


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


    @POST("api/getAttandance")
    @FormUrlEncoded
    suspend fun getAttendance(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("bcode") bcode: String,
        @Field("student") student: Int,
    ): Response<List<GetAttendanceModelResponse>>



    @POST("api/uploadMarks")
    @FormUrlEncoded
    suspend fun uploadMarks(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") course: Int,
        @Field("student") student: Int,
        @Field("bcode") bcode: String,
        @Field("score") score: Int,
    ): Response<UploadMarksResponse>


    @POST("api/getStudentClassAndCourses")
    @FormUrlEncoded
    suspend fun getStudentClassAndCourses(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("id") id: Int
    ): Response<StudentClassAndCoursesResponse>


    @POST("api/getCourses")
    @FormUrlEncoded
    suspend fun getCourses(
        @Field("type") type: String,
        @Field("token") token: String,
    ): Response<List<GetCourseResponse>>


    @POST("api/getMarks")
    @FormUrlEncoded
    suspend fun getMarks(
        @Field("type") type: String,
        @Field("token") token: String,
        @Field("course") course: Int,
        @Field("student") student: Int,
        @Field("bcode") bcode: String,
    ): Response<TestMarksResponse>}