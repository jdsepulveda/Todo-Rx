package com.todos.app.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar

import com.todos.R
import com.todos.app.adapter.TasksListAdapter
import com.todos.app.di.ToDoApplication
import com.todos.app.utils.EventObserver
import com.todos.app.utils.EventTypes
import com.todos.app.utils.Status
import com.todos.app.utils.appBarNavConfiguration
import com.todos.app.utils.extensions.gone
import com.todos.app.utils.extensions.visible
import com.todos.app.viewmodels.FragmentTasksListVM
import com.todos.app.viewmodels.factory.ViewModelFactory
import com.todos.databinding.FragmentTasksListBinding
import com.todos.local.model.Task
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import javax.inject.Inject

class FragmentTasksList : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var fragmentTasksListVM: FragmentTasksListVM

    private lateinit var tasksListAdapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTasksListVM = ViewModelProvider(this, viewModelFactory).get(FragmentTasksListVM::class.java)

        return DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, R.layout.fragment_tasks_list, container, false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataBindingUtil.findBinding<FragmentTasksListBinding>(view)?.apply {
            viewModel = fragmentTasksListVM
            lifecycleOwner = viewLifecycleOwner
        }

        NavigationUI.setupWithNavController(
            tasks_list_toolbar,
            findNavController(),
            appBarNavConfiguration
        )

        initRecyclerView()
        setUpDataObservers()
        setUpObservers()
    }

    private fun initRecyclerView() {
        tasksListAdapter = TasksListAdapter { taskItem: Task -> taskItemClicked(taskItem) }
        rvTaskList.adapter = tasksListAdapter
    }

    private fun taskItemClicked(taskItem: Task) {
        fragmentTasksListVM.updateTaskStatus(taskItem)
    }

    private fun setUpDataObservers() {
        fragmentTasksListVM.tasks.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.LOADING -> {
                    progressBarLoading.visible()
                }
                Status.SUCCESS -> {
                    progressBarLoading.gone()
                    it.data.orEmpty().let { tasks ->
                        Log.d("Task List", tasks.size.toString())
                        tasksListAdapter.populate(tasks)
                    }
                }
                Status.ERROR -> {
                    progressBarLoading.gone()
                }
            }
        })
    }

    private fun setUpObservers() {
        // TODO: Create a way similar to: override fun processEffect(event: ViewEffect) { when (event) { ... } }
        fragmentTasksListVM.event.observe(viewLifecycleOwner, EventObserver { eventTypes ->
            when(eventTypes) {
                is EventTypes.ShowMsgContextString -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.msg.format(this.requireContext()), Snackbar.LENGTH_LONG).show()
                    }
                }
                is EventTypes.ShowError -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.errorMsg, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as ToDoApplication).appComponent.inject(this)
    }
}