package com.gramzin.noteapp.di

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.gramzin.noteapp.data.data_storage.database.NoteDao
import com.gramzin.noteapp.data.data_storage.database.NoteDatabase
import com.gramzin.noteapp.data.data_storage.database.ReminderDao
import com.gramzin.noteapp.data.repository.NoteRepositoryImpl
import com.gramzin.noteapp.data.repository.ReminderRepositoryImpl
import com.gramzin.noteapp.domain.repository.NoteRepository
import com.gramzin.noteapp.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Singleton
    @Binds
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository

    companion object {
        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
            return Room
                .databaseBuilder(context, NoteDatabase::class.java, "note_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
            return noteDatabase.noteDao()
        }

        @Singleton
        @Provides
        fun provideReminderDao(noteDatabase: NoteDatabase): ReminderDao {
            return noteDatabase.reminderDao()
        }

        @Singleton
        @Provides
        fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager{
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }
}