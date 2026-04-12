package com.PillPal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.PillPal.data.db.dao.MedicineDao

@Database(
    entities = [Medicine::class], version = 3, exportSchema = false
)
@TypeConverters(MedicineTypeConverter::class)
abstract class MedicineDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}
