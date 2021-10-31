package com.eunwoo.contactlensmanagement

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.internal.synchronized


@Database(entities = arrayOf(Lens::class), version = 1, exportSchema = false)
abstract class LensDatabase: RoomDatabase() {
    abstract fun lensDao(): LensDao

    companion object {
        private var INSTANCE: LensDatabase? = null

        fun getInstance(context: Context): LensDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    LensDatabase::class.java,
                    "memo_database")
                    .build()
            }
            return INSTANCE as LensDatabase
        }
    }

}