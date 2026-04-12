package com.PillPal.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.PillPal.data.chat.GenerationParameters
import com.PillPal.data.db.MedicineDatabase
import com.PillPal.data.db.dao.MedicineDao
import com.PillPal.repository.Auth
import com.PillPal.repository.AuthImplementation
import com.PillPal.repository.MedicineRepo
import com.PillPal.repository.MedicineRepoImplementation
import com.PillPal.services.GeminiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMedicineDataBase(@ApplicationContext context: Context): MedicineDatabase {
        return Room.databaseBuilder(
            context = context, MedicineDatabase::class.java,
            name = "medicine_db"
        ).fallbackToDestructiveMigration().build();
    }


    @Singleton
    @Provides
    fun provideMedicineDao(db: MedicineDatabase): MedicineDao {
        return db.medicineDao();
    }

    @Singleton
    @Provides
    fun provideMedicineRepo(dao: MedicineDao): MedicineRepo {
        return MedicineRepoImplementation(
            dao = dao
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    fun provideFirebaseInstance(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseImpl(firebase: FirebaseAuth): Auth {
        return AuthImplementation(firebase)
    }
}
