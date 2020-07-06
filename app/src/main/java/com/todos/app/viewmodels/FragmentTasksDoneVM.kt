package com.todos.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.todos.R
import com.todos.app.utils.Event
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Resource
import com.todos.app.utils.ResourceString
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FragmentTasksDoneVM @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val eventTypes = MutableLiveData<Event<EventTypes>>()
    val event: LiveData<Event<EventTypes>>
        get() = eventTypes

    var tasks: LiveData<Resource<List<Task>>> = localDataSource.getTasks(true)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .map { Resource.success(it) }
        .startWith(Resource.loading())
        .onErrorResumeNext(Function {
            Observable.just(Resource.error(it.localizedMessage))
        }).toFlowable(BackpressureStrategy.LATEST)
        .toLiveData()

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