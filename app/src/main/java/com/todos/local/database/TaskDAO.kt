package com.todos.local.database

import androidx.room.*
import com.todos.local.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TaskDAO {

    @Insert
    fun addTask(task: Task): Completable

    @Update
    fun updateTask(task: Task): Completable

    @Delete
    fun deleteTask(task: Task): Completable

    @Query("SELECT * FROM task_table WHERE taskStatus = :status")
    fun getTasks(status: Boolean): Observable<List<Task>>

    @Query("SELECT * FROM task_table WHERE taskId = :id")
    fun getTask(id: Int): Single<Task>

    @Query("DELETE FROM task_table")
    fun deleteAllTasks(): Completable
}