package akashsarkar188.expensedaroga.services

import akashsarkar188.expensedaroga.screens.addTransaction.repository.TransactionDao
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.utils.DATABASE_NAME
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionDataModel::class], version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var databaseInstance : AppDatabase? = null

        @Synchronized
        fun getInstance(context :  Context) : AppDatabase {
            if (databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context, AppDatabase::class.java,
                DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return databaseInstance!!
        }
    }

    abstract fun transactionDao() : TransactionDao
}