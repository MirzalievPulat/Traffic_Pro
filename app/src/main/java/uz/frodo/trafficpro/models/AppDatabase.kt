package uz.frodo.trafficpro.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sign::class ,Test::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao():Dao

    companion object{
        private var INSTANCE:AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context):AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,AppDatabase::class.java,"app_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }

}