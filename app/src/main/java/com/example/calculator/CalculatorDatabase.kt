package com.example.calculator

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Calculator::class], version = 1)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun calculatorDao(): CalculatorDao

    companion object {
        @Volatile
        private var INSTANCE: CalculatorDatabase? = null

        fun getDatabase(context: Context): CalculatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalculatorDatabase::class.java,
                    "calculator_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}