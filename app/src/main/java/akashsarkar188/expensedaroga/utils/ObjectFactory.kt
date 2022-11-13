package akashsarkar188.expensedaroga.utils

import akashsarkar188.expensedaroga.services.AppDatabase

object ObjectFactory {

    var appDatabaseInstance: AppDatabase? = null
        get() {
            if (field == null) {
                field = AppDatabase.getInstance(MyApp.getApplicationContext())
            }
            return field
        }
}