package com.dicoding.todoapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _taskInsertionStatus = MutableLiveData<TaskInsertionStatus>()
    val taskInsertionStatus: LiveData<TaskInsertionStatus>
        get() = _taskInsertionStatus

    fun insertTask(task: Task) {
        viewModelScope.launch {
            try {
                val taskId = withContext(Dispatchers.IO) {
                    repository.insertTask(task)
                }
                if (isActive) {
                    _taskInsertionStatus.postValue(TaskInsertionStatus.Success(taskId))
                }
            } catch (e: Exception) {
                if (isActive) {
                    _taskInsertionStatus.postValue(TaskInsertionStatus.Error(e.message ?: "Error inserting task"))
                }
            }
        }
    }

    sealed class TaskInsertionStatus {
        data class Success(val taskId: Long) : TaskInsertionStatus()
        data class Error(val errorMessage: String) : TaskInsertionStatus()
    }
}
