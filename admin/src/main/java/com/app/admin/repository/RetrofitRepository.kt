package com.app.admin.repository

import android.util.Log
import com.app.admin.interfaces.ApiService
import com.app.admin.models.AddBatch
import com.app.admin.models.AddNewsModel
import com.app.admin.models.AdminRemoveAction
import com.app.admin.models.AdminRemoveResponse
import com.app.admin.models.BatchResponse
import com.app.admin.models.BatchStudents
import com.app.admin.models.BatchStudentsResponse
import com.app.admin.models.BatchesModel
import com.app.admin.models.Course
import com.app.admin.models.CoursesResponse
import com.app.admin.models.FeeDetailModel
import com.app.admin.models.FeeDetailResponse
import com.app.admin.models.FeeModel
import com.app.admin.models.FeeResponse
import com.app.admin.models.Finance
import com.app.admin.models.FinanceModel
import com.app.admin.models.FinanceResponse
import com.app.admin.models.GetCourse
import com.app.admin.models.GetCourseResponse
import com.app.admin.models.GetNewsModelResponse
import com.app.admin.models.Student
import com.app.admin.models.StudentDetailsResponse
import com.app.admin.models.LoginResponse
import com.app.admin.models.LogoutResponse
import com.app.admin.models.NewsModelResponse
import com.app.admin.models.Teacher
import com.app.admin.models.TeacherDetailsResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume


class RetrofitRepository(private val apiService: ApiService) {

    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return apiService.login(type, username, password)
    }

    suspend fun getStudents(type: String, token: String): Response<List<StudentDetailsResponse>> {
        return apiService.getStudents(type, token)
    }

    suspend fun getTeachers(type: String, token: String): Response<List<TeacherDetailsResponse>> {
        return apiService.getTeachers(type, token)
    }

    suspend fun getFinancePerson(type: String, token: String): Response<List<FinanceModel>> {
        return apiService.getFinancePerson(type, token)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return apiService.logout(type, token)
    }

    suspend fun getBatches(type: String, token: String): Response<List<BatchesModel>> {
        return apiService.getBatches(type, token)
    }

    suspend fun getNewsEvents(type: String, token: String): Response<List<GetNewsModelResponse>> {
        return apiService.getNewsEvents(type, token)
    }

    suspend fun payFee(feeModel: FeeModel): Response<FeeResponse> {
        return apiService.payFee(
            type = feeModel.type,
            token = feeModel.token,
            student = feeModel.student,
            date = feeModel.date
        )
    }

    suspend fun addBatch(addBatch: AddBatch): Response<BatchResponse> {
        return apiService.addBatch(
            type = addBatch.type,
            token = addBatch.token,
            bcode = addBatch.bcode
        )
    }

    suspend fun addNewsEvents(addNewsModel: AddNewsModel): Response<NewsModelResponse> {
        return apiService.addNewsEvents(
            type = addNewsModel.type,
            token = addNewsModel.token,
            date = addNewsModel.date,
            title = addNewsModel.title,
            description = addNewsModel.description,
            image = addNewsModel.image,
        )
    }

    suspend fun deleteData(adminRemoveAction: AdminRemoveAction): Response<AdminRemoveResponse> {
        return apiService.deleteData(
            type = adminRemoveAction.type,
            token = adminRemoveAction.token,
            id = adminRemoveAction.id,
            del = adminRemoveAction.del
        )
    }

    suspend fun addCourse(course: Course): Response<CoursesResponse> {
        return apiService.addCourse(
            type = course.type,
            token = course.token,
            name = course.name,
            code = course.code,
            max_marks = course.maxMarks
        )
    }

    suspend fun getCourses(getCourse: GetCourse): Response<List<GetCourseResponse>>{
        return apiService.getCourses(type = getCourse.type, token = getCourse.token)
    }

    suspend fun getFeeDetails(feeDetailModel: FeeDetailModel): Response<List<FeeDetailResponse>> {
        return apiService.getFeeDetails(
            type = feeDetailModel.type,
            token = feeDetailModel.token,
            student = feeDetailModel.student
        )
    }

    suspend fun addBatchStudents(batchStudents: BatchStudents): Response<BatchStudentsResponse> {
        return apiService.addBatchStudents(
            type = batchStudents.type,
            token = batchStudents.token,
            bcode = batchStudents.bcode,
            students = batchStudents.students,
            teachers = batchStudents.teachers
        )
    }

    suspend fun addFinancePerson(finance: Finance): Response<FinanceResponse> {
        return apiService.addFinancePerson(
            type = finance.type,
            token = finance.token,
            name = finance.name,
            email = finance.email,
            username = finance.username,
            password = finance.password,
            role = finance.role,
            image = finance.image
        )
    }

    suspend fun addStudent(student: Student): Response<StudentDetailsResponse> {
        try {
            val response = apiService.addStudent (
                    type = student.type,
                    token = student.token,
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
            if (response.isSuccessful) {
                Log.d("responseStudent", "Success ${response.body()}")
            } else {
                Log.d("responseStudent", "Not Success")
            }
            return response
        } catch (e:Exception){
            e.printStackTrace()
            throw e
        }
    }

//    suspend fun addStudent(student: Student): Int? {
//        return suspendCancellableCoroutine { continuation ->
//            try {
//                val addStudentCall = apiService.addStudent(
//                    type = student.type,
//                    token = student.token,
//                    firstname = student.firstname,
//                    lastname = student.lastname,
//                    rollno = student.rollno,
//                    contact = student.contact,
//                    nic = student.nic,
//                    address = student.address,
//                    username = student.username,
//                    password = student.password,
//                    image = student.image!!
//                )
//
//                addStudentCall.enqueue(object : Callback<StudentDetailsResponse> {
//                    override fun onResponse(
//                        call: Call<StudentDetailsResponse>,
//                        response: Response<StudentDetailsResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            continuation.resume(response.body()?.id)
//                        } else {
//                            Log.e(
//                                "API_CALL",
//                                "Error code: ${response.code()}, Error body: ${response.errorBody()}"
//                            )
//                            continuation.resume(null)
//                        }
//                    }
//
//                    override fun onFailure(call: Call<StudentDetailsResponse>, t: Throwable) {
//                        handleCallFailure(t)
//                        continuation.resume(null)
//                    }
//                })
//            } catch (e: Exception) {
//                Log.e("API_CALL", "Exception: ${e.message}")
//                continuation.resume(null)
//            }
//        }
//    }

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
                            Log.e(
                                "API_CALL",
                                "Error code: ${response.code()}, Error body: ${response.errorBody()}"
                            )
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
