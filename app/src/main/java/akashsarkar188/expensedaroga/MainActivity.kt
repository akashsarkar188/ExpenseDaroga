package akashsarkar188.expensedaroga

import akashsarkar188.expensedaroga.databinding.ActivityMainBinding
import akashsarkar188.expensedaroga.databinding.BottomNavItemBinding
import akashsarkar188.expensedaroga.screens.addTransaction.AddTransactionFragment
import akashsarkar188.expensedaroga.screens.home.HomeFragment
import akashsarkar188.expensedaroga.screens.settings.SettingsFragment
import akashsarkar188.expensedaroga.services.MainActivityFragmentHelper
import akashsarkar188.expensedaroga.utils.NotificationHelper
import akashsarkar188.expensedaroga.utils.commonMethods.doIfFalse
import akashsarkar188.expensedaroga.utils.commonMethods.doIfTrue
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import android.animation.LayoutTransition
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        MainActivityFragmentHelper.setContext(this)

        init()
    }

    private fun init() {
        binding?.openBubbleFAB?.setOnClickListener {
            NotificationHelper.openTransactionsInBubble(this, getCurrentMonthYearString())
        }

        initBottomNavigation()
    }

    private val homeFragment : HomeFragment by lazy {
        HomeFragment.newInstance()
    }
    private val settingsFragment : SettingsFragment by lazy {
        SettingsFragment.newInstance()
    }
    private val addTransactionFragment : AddTransactionFragment by lazy {
        AddTransactionFragment.newInstance(null, true)
    }

    private fun initBottomNavigation() {
        binding?.homeLayout?.parentCard?.setOnClickListener {
            changeBottomNavSelection(binding?.homeContainerLinearLayout, binding?.homeLayout, true)
            changeBottomNavSelection(binding?.addTransactionContainerLinearLayout, binding?.addTransactionsLayout, false)
            changeBottomNavSelection(binding?.settingsContainerLinearLayout, binding?.settingsLayout, false)

            binding?.openBubbleFAB?.visibility = View.VISIBLE

            if (MainActivityFragmentHelper.isFragmentAdded(homeFragment)) {
                MainActivityFragmentHelper.showThisFragment(homeFragment)
            } else {
                MainActivityFragmentHelper.addFragment(homeFragment, false)
            }
        }

        binding?.settingsLayout?.parentCard?.setOnClickListener {
            changeBottomNavSelection(binding?.homeContainerLinearLayout, binding?.homeLayout, false)
            changeBottomNavSelection(binding?.addTransactionContainerLinearLayout, binding?.addTransactionsLayout, false)
            changeBottomNavSelection(binding?.settingsContainerLinearLayout, binding?.settingsLayout, true)

            binding?.openBubbleFAB?.visibility = View.VISIBLE

            if (MainActivityFragmentHelper.isFragmentAdded(settingsFragment)) {
                MainActivityFragmentHelper.showThisFragment(settingsFragment)
            } else {
                MainActivityFragmentHelper.addFragment(settingsFragment, false)
            }
        }

        binding?.addTransactionsLayout?.parentCard?.setOnClickListener {
            changeBottomNavSelection(binding?.homeContainerLinearLayout, binding?.homeLayout, false)
            changeBottomNavSelection(binding?.addTransactionContainerLinearLayout, binding?.addTransactionsLayout, true)
            changeBottomNavSelection(binding?.settingsContainerLinearLayout, binding?.settingsLayout, false)

            binding?.openBubbleFAB?.visibility = View.GONE

            if (MainActivityFragmentHelper.isFragmentAdded(addTransactionFragment)) {
                MainActivityFragmentHelper.showThisFragment(addTransactionFragment)
            } else {
                MainActivityFragmentHelper.addFragment(addTransactionFragment, false)
            }
        }

        setupLayout(binding?.homeLayout, "Home", R.drawable.home_icon)
        setupLayout(binding?.addTransactionsLayout, "Transactions", R.drawable.rupee_icon)
        setupLayout(binding?.settingsLayout, "Settings", R.drawable.setting_icon)

        binding?.homeLayout?.parentCard?.performClick()
    }

    private fun setupLayout(binding: BottomNavItemBinding?, titleStr: String, iconId: Int) {
        binding?.apply {
            icon.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, iconId))
            title.text = titleStr
        }
    }

    private fun changeBottomNavSelection(parentContainer: View?, binding: BottomNavItemBinding?, setSelected: Boolean) {
        binding?.apply {
            (parentContainer as ViewGroup).layoutTransition.enableTransitionType(
                LayoutTransition.CHANGING
            )
            val parentLayoutParams = parentContainer.getLayoutParams() as LinearLayout.LayoutParams

            doIfTrue(setSelected) {
                cardLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.themeGreenOpacity15
                    )
                )
                icon.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.themeGreenDarker
                    )
                )
                title.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.themeGreenDarker
                    )
                ))
                parentLayoutParams.weight = 1.4f
                title.visibility = View.VISIBLE
            }

            doIfFalse(setSelected) {
                cardLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.white
                    )
                )
                icon.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.basicBlack
                    )
                )
                parentLayoutParams.weight = 0.8f
                title.visibility = View.GONE
            }
        }
    }
}