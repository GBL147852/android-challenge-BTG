package com.example.desafiobtg.db.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.desafiobtg.db.converters.GeneralTypeConverter
import com.example.desafiobtg.db.entities.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(GeneralTypeConverter::class)
abstract class Database: RoomDatabase() {

    companion object {
        const val DB_NAME = "DesafioBTG_db"
    }

}