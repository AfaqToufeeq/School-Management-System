package com.attech.sms.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attech.sms.models.AttendanceModel
import com.attech.sms.models.AttendanceResponse
import com.attech.sms.models.BatchesModel
import com.attech.sms.models.CourseTeacherResponse
import com.attech.sms.models.GetAttendanceModel
import com.attech.sms.models.GetAttendanceModelResponse
import com.attech.sms.models.GetCourse
import com.attech.sms.models.GetCourseResponse
import com.attech.sms.models.LoginResponse
import com.attech.sms.models.LogoutResponse
import com.attech.sms.models.MarksData
import com.attech.sms.models.Student
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.models.StudentClassAndCoursesResponse
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.TeacherDetailsResponse
import com.attech.sms.models.TestMarksRequest
import com.attech.sms.models.TestMarksResponse
import com.attech.sms.models.UploadMarksResponse
import com.attech.sms.repository.RetrofitRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val _students = MutableLiveData<List<StudentDetailsResponse>>()
    val students: LiveData<List<StudentDetailsResponse>> get() = _students

    private val _addStudentResult = MutableLiveData<Boolean?>()
    val addStudentResult: LiveData<Boolean?> get() = _addStudentResult

    private val _teachers = MutableLiveData<List<TeacherDetailsResponse>>()
    val teachers: LiveData<List<TeacherDetailsResponse>> get() = _teachers

    private val _allBatches = MutableLiveData<List<BatchesModel>>()
    val allBatches: LiveData<List<BatchesModel>> get() = _allBatches

    private val _batchStudents = MutableLiveData<List<StudentDetailsResponse>>()
    val batchStudents: LiveData<List<StudentDetailsResponse>> get() = _batchStudents

    private val _teacherCourses = MutableLiveData<List<CourseTeacherResponse>>()
    val teacherCourses: LiveData<List<CourseTeacherResponse>> get() = _teacherCourses

    private val _studentClassAndCoursesResponse = MutableLiveData<StudentClassAndCoursesResponse>()
    val studentClassAndCoursesResponse: LiveData<StudentClassAndCoursesResponse> get() = _studentClassAndCoursesResponse

    private val _getAttendance = MutableLiveData<List<GetAttendanceModelResponse>>()
    val getAttendance: LiveData<List<GetAttendanceModelResponse>> get() = _getAttendance

    private val _getCourses = MutableLiveData<GetCourseResponse>()
    val getCourses: LiveData<GetCourseResponse> get() = _getCourses

    private val _testMarks = MutableLiveData<TestMarksResponse>()
    val testMarks: LiveData<TestMarksResponse> get() = _testMarks




    suspend fun login(type: String, username: String, password: String): LoginResponse {
        return repository.login(type, username, password)
    }

    suspend fun logout(type: String, token: String): LogoutResponse {
        return repository.logout(type, token)
    }

    fun fetchStudents(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStudents(type, token)
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }

    suspend fun markAttendance(attendanceModel: AttendanceModel): Response<AttendanceResponse> {
        return repository.markAttendance(attendanceModel)
    }

    suspend fun uploadMarks(marksData: MarksData): Response<UploadMarksResponse> {
        return repository.uploadMarks(marksData)
    }

    fun getAttendance(getAttendanceModel: GetAttendanceModel){
        viewModelScope.launch {
            try {
                val response = repository.getAttendance(getAttendanceModel)
                if (response.isSuccessful) {
                    _getAttendance.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun getMarks(testMarksRequest: TestMarksRequest){
        viewModelScope.launch {
            try {
                val response = repository.getMarks(testMarksRequest)
                if (response.isSuccessful) {
                    _testMarks.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun getStudentClassAndCourses(studentClassAndCourses: StudentClassAndCourses){
        viewModelScope.launch {
            try {
                val response = repository.getStudentClassAndCourses(studentClassAndCourses)
                if (response.isSuccessful) {
                    _studentClassAndCoursesResponse.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    suspend fun getCourses(getCourse: GetCourse): Response<List<GetCourseResponse>>{
        try {
            val response = repository.getCourses(getCourse)
            if (response.isSuccessful) {
//                    _getCourses.value = response.body()
                Log.d("Token", "Success ${response.body()}")
            }
            return response
        } catch (e: Exception) {
            Log.d("Token", "Error: ${e.message}")
            throw(e)
        }
    }


    fun fetchTeachers(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTeachers(type, token)
                if (response.isSuccessful) {
                    _teachers.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    fun fetchBatches(type: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBatches(type, token)
                if (response.isSuccessful) {
                    _allBatches.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.d("Token", "Error: ${e.message}")
            }
        }
    }


    suspend fun getBatchStudents(type: String, token: String, bcode: String){
        try {
            val response = repository.getBatchStudents(type, token, bcode)
            if (response.isSuccessful) {
                _batchStudents.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.d("getBatchStudentsToken", "Error: ${e.message}")
        }

    }


    fun getCourseTeacher(type: String, token: String, teacher: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCourseTeacher(type, token, teacher)
                if (response.isSuccessful) {
                    _teacherCourses.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.d("getCourseTeacher", "Error: ${e.message}")
            }
        }
    }


    fun addStudent(type: String, token: String, student: Student) {
        viewModelScope.launch {
            try {
                val response = repository.addStudent(type, token, student)
                _addStudentResult.value = response
            } catch (e: Exception) {
                Log.d("API_ERROR", "View Model Failed ${e.message.toString()}")
                _addStudentResult.value = false
            }
        }
    }
}
