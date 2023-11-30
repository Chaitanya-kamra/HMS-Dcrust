package com.chaitanya.hms_dcrust.di


import com.chaitanya.hms_dcrust.data.api.HostelApi
import com.chaitanya.hms_dcrust.utils.Constants.BASE_URL
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HostelModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesApi(retrofit: Retrofit): HostelApi {
        return retrofit.create(HostelApi::class.java)
    }

    @Provides
    fun provideDataBaseInstance(): FirebaseDatabase = FirebaseDatabase.getInstance()
}