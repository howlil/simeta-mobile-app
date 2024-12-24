package com.dev.simeta.di

import com.dev.simeta.BuildConfig
import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.api.LogbookApi
import com.dev.simeta.data.api.MilestoneApi
import com.dev.simeta.data.api.ProgressApi
import com.dev.simeta.data.api.ReminderApi
import com.dev.simeta.data.repository.AuthRepository
import com.dev.simeta.data.repository.LogbookRepository
import com.dev.simeta.data.repository.MilestoneRepository
import com.dev.simeta.data.repository.ProgressRepository
import com.dev.simeta.data.repository.ReminderRepository
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Pastikan BASE_URL sudah didefinisikan di build.gradle
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepository(authApi)
    }

    @Provides
    @Singleton
    fun provideLogbookApi(retrofit: Retrofit): LogbookApi {
        return retrofit.create(LogbookApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogbookRepository(logbookApi: LogbookApi): LogbookRepository {
        return LogbookRepository(logbookApi)
    }
    @Provides
    @Singleton
    fun provideMilestoneApi(retrofit: Retrofit): MilestoneApi {
        return retrofit.create(MilestoneApi::class.java) // Add this line to provide MilestoneApi
    }
    @Provides
    @Singleton
    fun provideMilestoneRepository(milestoneApi: MilestoneApi): MilestoneRepository {
        return MilestoneRepository(milestoneApi)
    }

    @Provides
    @Singleton
    fun provideProgressApi(retrofit: Retrofit): ProgressApi {
        return retrofit.create(ProgressApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProgressRepository(progressApi: ProgressApi): ProgressRepository {
        return ProgressRepository(progressApi)
    }
    @Provides
    @Singleton
    fun provideReminderApi(retrofit: Retrofit): ReminderApi {
        return retrofit.create(ReminderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReminderRepository(reminderApi: ReminderApi): ReminderRepository {
        return ReminderRepository(reminderApi)
    }
}
