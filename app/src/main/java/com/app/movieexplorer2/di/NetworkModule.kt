package com.app.movieexplorer2.di

import com.app.movieexplorer2.BuildConfig
import com.app.movieexplorer2.app.AppConstant
import com.app.movieexplorer2.app.AppConstant.BASE_URL
import com.app.movieexplorer2.data.remote.MovieApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides

    fun provideAPIService(): MovieApiService {

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

        val gson = gsonBuilder.create()

        val httpClient = OkHttpClient.Builder()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        httpClient.addInterceptor { chain ->

            val original = chain.request()

            val token = BuildConfig.TMDB_READ_ACCESS_TOKEN

            val requestBuilder = original.newBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("Cache-control", "no-cache")

            if(!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        httpClient.interceptors().add(Interceptor { chain ->

            val request = chain.request()

            val response = chain.proceed(request)
            response.code
            response
        })


        val okHttpClient = httpClient
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .cache(null)
                .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))

        return retrofit.build().create(MovieApiService::class.java)


    }
}