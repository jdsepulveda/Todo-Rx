package com.todos.app.di

import android.content.Context
import com.todos.local.database.ToDoDB
import com.todos.source.LocalDataSource
import com.todos.source.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocalSourceModule.Binders::class])
class LocalSourceModule {

    @Module
    interface Binders {
        @Binds
        fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
    }

    @Singleton
    @Provides
    fun providesDatabase(context: Context) = ToDoDB.getInstance(context)

    @Singleton
    @Provides
    fun providesTaskDAO(toDoDB: ToDoDB) = toDoDB.getTaskDao()
}