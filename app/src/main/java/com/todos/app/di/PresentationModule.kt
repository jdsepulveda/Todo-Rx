package com.todos.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todos.app.viewmodels.DialogFragmentTaskVM
import com.todos.app.viewmodels.FragmentTasksDoneVM
import com.todos.app.viewmodels.FragmentTasksListVM
import com.todos.app.viewmodels.factory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FragmentTasksListVM::class)
    abstract fun bindsFragmentTasksListVM(fragmentTasksListVM: FragmentTasksListVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FragmentTasksDoneVM::class)
    abstract fun bindsFragmentTasksDoneVM(fragmentTasksDoneVM: FragmentTasksDoneVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DialogFragmentTaskVM::class)
    abstract fun bindsDialogFragmentTaskVM(dialogFragmentTaskVM: DialogFragmentTaskVM): ViewModel
}