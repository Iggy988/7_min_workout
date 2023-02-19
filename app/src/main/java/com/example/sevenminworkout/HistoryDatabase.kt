package com.example.sevenminworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {

        /**Marks the JVM backing field of the annotated property as volatile,
        meaning that writes to this field are immediately made visible to other threads.
         */
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context):HistoryDatabase?{
            //vise threads mogu trayiti pristup database u isto vrijeme, pa da osiguramo da samo jednom inicijaliyiramo
            // koristimo synchronized - samo jedan po jedan zahtjev se mogu obradjivati
            synchronized(this){
                var instance = INSTANCE

                if (INSTANCE == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}