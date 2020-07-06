package com.todos.app.di

import android.app.Application

open class ToDoApplication : Application() {
    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}