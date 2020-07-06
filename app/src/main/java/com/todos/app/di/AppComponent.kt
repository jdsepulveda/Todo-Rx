package com.todos.app.di

import android.content.Context
import com.todos.app.ui.FragmentDialogTask
import com.todos.app.ui.FragmentTasksDone
import com.todos.app.ui.FragmentTasksList
import com.todos.app.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        PresentationModule::class,
        LocalSourceModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: FragmentTasksList)
    fun inject(fragment: FragmentTasksDone)
    fun inject(dialogFragment: FragmentDialogTask)
}