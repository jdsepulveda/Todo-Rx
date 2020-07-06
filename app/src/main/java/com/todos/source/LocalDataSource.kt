package com.todos.source

import com.todos.local.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface LocalDataSource {

    fun addTask(task: Task): Completable

    fun updateTask(task: Task): Completable

    fun deleteTask(task: Task): Completable

    fun getTasks(status: Boolean): Observable<List<Task>>

    fun getTask(id: Int): Single<Task>

    fun deleteAllTasks(): Completable
}