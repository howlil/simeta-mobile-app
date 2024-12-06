package com.dev.simeta.di

import com.dev.simeta.BuildConfig
import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepository(authApi)
    }
}
