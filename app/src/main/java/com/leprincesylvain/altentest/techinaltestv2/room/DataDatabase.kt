package com.leprincesylvain.altentest.techinaltestv2.room

import android.content.Context
import androidx.room.*
import com.leprincesylvain.altentest.techinaltestv2.model.Converters
import com.leprincesylvain.altentest.techinaltestv2.model.device.DeviceTableModel
import com.leprincesylvain.altentest.techinaltestv2.model.user.UserTableModel

@Database(entities = [DeviceTableModel ::class, UserTableModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DataDatabase : RoomDatabase() {

    abstract fun deviceDao() : DeviceDao
    abstract fun userDao() : UserDao

    companion object {

        @Volatile
        private var INSTANCE: DataDatabase? = null

        fun getDatabaseInstance(context: Context) : DataDatabase {
            println("calling database")

            if (INSTANCE != null) return INSTANCE!!
            println("creating instance of database here")
            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, DataDatabase::class.java, "Data_Database")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}
