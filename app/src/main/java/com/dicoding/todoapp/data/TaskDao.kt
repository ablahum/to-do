package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE (:completed IS NULL OR completed = :completed) ORDER BY dueDate DESC")
    fun getTasks(completed: Boolean?): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks ORDER BY ABS(dueDate - :currentDate) ASC LIMIT 1")
    fun getNearestActiveTask(currentDate: Long): Task

    @Insert
    suspend fun insertTask(task: Task): Long

    @Insert
    suspend fun insertAll(vararg tasks: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)

    @RawQuery(observedEntities = [Task::class])
    fun getFilteredTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>
}


