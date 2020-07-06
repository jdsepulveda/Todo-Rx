package com.todos.source

import com.todos.local.database.TaskDAO
import com.todos.local.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val taskDAO: TaskDAO
) : LocalDataSource {

    override fun addTask(task: Task): Completable {
        return taskDAO.addTask(task)
    }

    override fun updateTask(task: Task): Completable {
        return taskDAO.updateTask(task)
    }

    override fun deleteTask(task: Task): Completable {
        return taskDAO.deleteTask(task)
    }

    override fun getTasks(status: Boolean): Observable<List<Task>> {
        return taskDAO.getTasks(status)
    }

    override fun getTask(id: Int): Single<Task> {
        return taskDAO.getTask(id)
    }

    override fun deleteAllTasks(): Completable {
        return taskDAO.deleteAllTasks()
    }
}