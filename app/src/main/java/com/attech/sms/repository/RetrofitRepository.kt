package com.attech.sms.repository

import android.util.Log
import com.attech.sms.interfaces.ApiService
import com.attech.sms.models.AttendanceModel
import com.attech.sms.models.AttendanceResponse
import com.attech.sms.models.BatchesModel
import com.attech.sms.models.CourseTeacherResponse
import com.attech.sms.models.GetAttendanceModel
import com.attech.sms.models.GetAttendanceModelResponse
import com.attech.sms.models.GetCourse
import com.attech.sms.models.GetCourseResponse
import com.attech.sms.models.GetNewsModelResponse
import com.attech.sms.models.LoginResponse
import com.attech.sms.models.LogoutResponse
import com.attech.sms.models.MarksData
import com.attech.sms.models.Student
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.models.StudentClassAndCoursesResponse
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.Teacher
import com.attech.sms.models.TeacherDetailsResponse
import com.attech.sms.models.TestMarksRequest
import com.attech.sms.models.TestMarksResponse
import com.attech.sms.models.UploadMarksResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class RetrofitRepository(private val apiService: ApiService) {

    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return apiService.login(type, username, password)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return apiService.logout(type, token)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetailsResponse>> {
        return apiService.getStudents(type, token)
    }

    suspend fun getTeachers(type: String, token: String): Response<List<TeacherDetailsResponse>> {
        return apiService.getTeachers(type, token)
    }

    suspend fun getBatches(type: String, token: String): Response<List<BatchesModel>> {
        return apiService.getBatches(type, token)
    }

    suspend fun getBatchStudents(type: String, token: String, bcode: String): Response<List<StudentDetailsResponse>> {
        return apiService.getBatchStudents(type, token, bcode)
    }

    suspend fun getTeacherCourses(type: String, token: String, teacher: Int): Response<List<CourseTeacherResponse>> {
        return apiService.getTeacherCourses(type, token, teacher)
    }

    suspend fun getCourseTeachers(type: String, token: String, course: Int): Response<List<TeacherDetailsResponse>> {
        return apiService.getCourseTeachers(type, token, course)
    }

    suspend fun getNewsEvents(type: String, token: String): Response<List<GetNewsModelResponse>> {
        return apiService.getNewsEvents(type, token)
    }

    suspend fun markAttendance(attendanceModel: AttendanceModel): Response<AttendanceResponse> {
        try {
            return apiService.markAttendance(
                type = attendanceModel.type,
                token = attendanceModel.token,
                bcode = attendanceModel.bcode,
                course = attendanceModel.course,
                student = attendanceModel.student,
                date = attendanceModel.date
            )
        } catch (e: Exception) {
            Log.e("ApiCallError", "HTTP ${e.message}")
            throw e
        }
    }


    suspend fun getAttendance(getAttendanceModel: GetAttendanceModel): Response<List<GetAttendanceModelResponse>> {
        try {
            return apiService.getAttendance(
                type = getAttendanceModel.type,
                token = getAttendanceModel.token,
                bcode = getAttendanceModel.bcode,
                student = getAttendanceModel.student
            )
        } catch (e: Exception) {
            Log.e("ApiCallError", "HTTP ${e.message}")
            throw e
        }
    }


    suspend fun uploadMarks(marksData: MarksData): Response<UploadMarksResponse> {
        try {
            return apiService.uploadMarks(
                type = marksData.type,
                token = marksData.token,
                course = marksData.course,
                student = marksData.student,
                bcode = marksData.bcode,
                score = marksData.score
            )
        } catch (e: Exception) {
            Log.e("ApiCallError", "HTTP ${e.message}")
            throw e
        }
    }

    suspend fun getMarks(testMarks: TestMarksRequest): Response<TestMarksResponse> {
        try {
            return apiService.getMarks(
                type = testMarks.type,
                token = testMarks.token,
                course = testMarks.course,
                student = testMarks.student,
                bcode = testMarks.bcode,
            )
        } catch (e: Exception) {
            Log.e("ApiCallError", "HTTP ${e.message}")
            throw e
        }
    }

    suspend fun getStudentClassAndCourses(studentClassAndCourses: StudentClassAndCourses): Response<StudentClassAndCoursesResponse>{
        try {
            return apiService.getStudentClassAndCourses(
                type = studentClassAndCourses.type,
                token = studentClassAndCourses.token,
                id = studentClassAndCourses.id
            )
        } catch (e: Exception) {
            Log.e("API_CALL", "Error code: ${e.message}, Error body: ${e.toString()}")
            throw (e)
        }
    }

    suspend fun getCourses(getCourse: GetCourse): Response<List<GetCourseResponse>>{
        try {
            return apiService.getCourses(
                type = getCourse.type,
                token = getCourse.token
            )
        } catch (e: Exception) {
            Log.e("API_CALL", "Error code: ${e.message}, Error body: ${e.toString()}")
            throw (e)
        }
    }


    suspend fun addStudent(type: String, token: String, student: Student): Boolean {
        return suspendCancellableCoroutine { continuation ->
            try {
                val addStudentCall = apiService.addStudent(
                    type = type,
                    token = token,
                    firstname = student.firstname,
                    lastname = student.lastname,
                    rollno = student.rollno,
                    contact = student.contact,
                    nic = student.nic,
                    address = student.address,
                    username = student.username,
                    password = student.password,
                    image = student.image!!
                )

                addStudentCall.enqueue(object : Callback<StudentDetailsResponse> {
                    override fun onResponse(
                        call: Call<StudentDetailsResponse>,
                        response: Response<StudentDetailsResponse>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            Log.e("API_CALL", "Error code: ${response.code()}, Error body: ${response.errorBody()}")
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
                        handleCallFailure(t)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                Log.e("API_CALL", "Exception: ${e.message}")
                continuation.resume(false)
            }
        }
    }


    suspend fun addTeacher(type: String, token: String, teacher: Teacher): Boolean {
        return suspendCancellableCoroutine { continuation ->
            try {
                val addTeacherCall = apiService.addTeacher(
                    type = type,
                    token = token,
                    firstname = teacher.firstname,
                    lastname = teacher.lastname,
                    contact = teacher.contact,
                    nic = teacher.nic,
                    address = teacher.address,
                    username = teacher.username,
                    password = teacher.password,
                    image = teacher.image!!
                )

                addTeacherCall.enqueue(object : Callback<TeacherDetailsResponse> {
                    override fun onResponse(
                        call: Call<TeacherDetailsResponse>,
                        response: Response<TeacherDetailsResponse>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            Log.e("API_CALL", "Error code: ${response.code()}, Error body: ${response.errorBody()}")
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<TeacherDetailsResponse>, t: Throwable) {
                        handleCallFailure(t)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                Log.e("API_CALL", "Exception: ${e.message}")
                continuation.resume(false)
            }
        }
    }

    private fun handleCallFailure(t: Throwable, message: String = ":") {
        Log.e("API_CALL", "Error $message ${t.message}", t)
    }
}