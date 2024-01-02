package com.dicoding.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private val addTaskViewModel: AddTaskViewModel by viewModels {
        AddTaskViewModelFactory(TaskRepository.getInstance(application))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTask() {
        val taskTitle = findViewById<TextView>(R.id.add_ed_title).text.toString()
        val taskDescription = findViewById<TextView>(R.id.add_ed_description).text.toString()
        if (taskTitle.isBlank()) {
            Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val newTask = Task(title = taskTitle, description = taskDescription, dueDate = dueDateMillis, completed = false)

        // Observe taskInsertionStatus and handle UI accordingly
        addTaskViewModel.taskInsertionStatus.observe(this, { status ->
            when (status) {
                is AddTaskViewModel.TaskInsertionStatus.Success -> {
                    // Handle success, maybe navigate back or show a success message
                    Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Finish the activity after saving the task
                }
                is AddTaskViewModel.TaskInsertionStatus.Error -> {
                    // Handle error, show an error message to the user
                    Toast.makeText(this, status.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Call the insertTask method in the AddTaskViewModel
        addTaskViewModel.insertTask(newTask)
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}
