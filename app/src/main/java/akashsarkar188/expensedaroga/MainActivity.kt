package akashsarkar188.expensedaroga

import akashsarkar188.expensedaroga.databinding.ActivityMainBinding
import akashsarkar188.expensedaroga.screens.home.HomeFragment
import akashsarkar188.expensedaroga.services.FragmentHelper
import akashsarkar188.expensedaroga.utils.NotificationHelper
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        FragmentHelper.setContext(this)

        init()
    }

    private fun init() {
        FragmentHelper.addFragment(HomeFragment.newInstance())

        binding?.openBubbleFAB?.setOnClickListener {
            NotificationHelper.openTransactionsInBubble(this, getCurrentMonthYearString())
        }

    }
}