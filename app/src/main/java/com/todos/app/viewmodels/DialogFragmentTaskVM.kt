package com.todos.app.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.todos.R
import com.todos.app.ui.FragmentDialogTaskDirections
import com.todos.app.utils.*
import com.todos.local.model.Task
import com.todos.source.LocalDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DialogFragmentTaskVM @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val disposable = CompositeDisposable()
    var taskName = MutableLiveData<String>()

    private val eventTypes = MutableLiveData<Event<EventTypes>>()
    val event: LiveData<Event<EventTypes>>
        get() = eventTypes

    val onCreateTaskClick = View.OnClickListener {
        eventTypes.value = Event(EventTypes.CloseKeyboard(it))

        if (taskName.value.isNullOrBlank()) {
            eventTypes.value = Event(EventTypes.ShowErrorContextString(ResourceString(R.string.task_name_required)))
        } else {
            disposable.add(
                localDataSource.addTask(Task(0, taskName.value.toString(), false))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            eventTypes.value = Event(EventTypes.ShowMsgContextString(ResourceString(R.string.task_saved)))
                            eventTypes.value =
                                Event(EventTypes.Navigate(FragmentDialogTaskDirections.actionFragmentDialogTaskToFragmentTasksList()))
                        },
                        {
                            eventTypes.value =
                                Event(EventTypes.ShowErrorContextString(ResourceString(R.string.error_msg)))
                        }
                    )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}