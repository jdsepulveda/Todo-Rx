package com.todos.app.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.todos.R
import com.todos.app.ui.FragmentTasksListDirections
import com.todos.app.utils.Event
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Resource
import com.todos.app.utils.ResourceString
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FragmentTasksListVM @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val taskList = MutableLiveData<Resource<List<Task>>>()
    val tasks: LiveData<Resource<List<Task>>>
        get() = taskList

    private val eventTypes = MutableLiveData<Event<EventTypes>>()
    val event: LiveData<Event<EventTypes>>
        get() = eventTypes

    init {
        taskList.postValue(Resource.loading())
        disposable.add(
            localDataSource.getTasks(false)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { tasks -> taskList.postValue(Resource.success(tasks)) },
                    { taskList.postValue(Resource.error(it.localizedMessage)) }
                )
        )
    }

    val onCreateTaskClick = View.OnClickListener {
        it.findNavController().navigate(FragmentTasksListDirections.actionFragmentTasksListToFragmentDialogTask())
    }

    fun updateTaskStatus(task: Task) {
        disposable.add(
            localDataSource.updateTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { eventTypes.value = Event(EventTypes.ShowMsgContextString(ResourceString(R.string.task_updated))) },
                    { eventTypes.value = Event(EventTypes.ShowErrorContextString(ResourceString(R.string.error_msg))) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}