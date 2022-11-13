package akashsarkar188.expensedaroga.utils

import akashsarkar188.expensedaroga.services.AppDatabase

object ObjectFactory {

    val appDatabaseInstance: AppDatabase by lazy {
        AppDatabase.getInstance(MyApp.getApplicationContext())
    }
}