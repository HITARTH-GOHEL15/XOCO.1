package com.app.movieexplorer2.di

import android.content.Context
import com.app.movieexplorer2.utils.ErrorUtils
import com.app.movieexplorer2.utils.resource.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModules {

    @Singleton
    @Provides
    fun provideErrorUtils(
        @ApplicationContext context: Context
    ): ErrorUtils {
        return ErrorUtils(context)
    }

    @Singleton
    @Provides
    fun provideResponseHandler(
        @ApplicationContext context: Context,
        errorUtils: ErrorUtils
    ): ResponseHandler {
        return ResponseHandler(
            context,
            errorUtils
        )
    }


}