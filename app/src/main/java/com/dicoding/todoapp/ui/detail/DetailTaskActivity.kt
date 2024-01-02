package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.utils.DateConverter

class DetailTaskActivity : AppCompatActivity() {

    private val viewModel: DetailTaskViewModel by viewModels {
        DetailTaskViewModelFactory(TaskRepository.getInstance(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        //TODO 11 : Show detail task and implement delete action

        val taskId = intent.getIntExtra("TASK_ID", -1)

        val taskTitleTextView: TextView = findViewById(R.id.detail_ed_title)
        val taskDescriptionTextView: TextView = findViewById(R.id.detail_ed_description)
        val dueDateTextView: TextView = findViewById(R.id.detail_ed_due_date)
        val deleteButton: Button = findViewById(R.id.btn_delete_task)

        viewModel.setTaskId(taskId)

        viewModel.task.observe(this, Observer { task ->
            if (task != null) {
                taskTitleTextView.text = task.title
                taskDescriptionTextView.text = task.description
                val formattedDate = DateConverter.convertMillisToString(task.dueDate)
                dueDateTextView.text = formattedDate
            } else {
            }
        })

        viewModel.deleteTask.observe(this, Observer { isDeleted ->
            if (isDeleted) {
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
            }
        })

        deleteButton.setOnClickListener {
            viewModel.deleteTask()
        }
    }
}
