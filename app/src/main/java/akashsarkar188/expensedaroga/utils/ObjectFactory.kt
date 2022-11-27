package akashsarkar188.expensedaroga.utils

import akashsarkar188.expensedaroga.services.AppDatabase
import androidx.lifecycle.MutableLiveData

object ObjectFactory {

    val appDatabaseInstance: AppDatabase by lazy {
        AppDatabase.getInstance(MyApp.getApplicationContext())
    }

    val globalRefreshMutableLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }

    val userNameChangeMutableLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData()
    }
}