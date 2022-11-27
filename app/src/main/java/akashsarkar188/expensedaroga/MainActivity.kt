package akashsarkar188.expensedaroga

import akashsarkar188.expensedaroga.databinding.ActivityMainBinding
import akashsarkar188.expensedaroga.databinding.BottomNavItemBinding
import akashsarkar188.expensedaroga.screens.home.HomeFragment
import akashsarkar188.expensedaroga.screens.settings.SettingsFragment
import akashsarkar188.expensedaroga.services.FragmentHelper
import akashsarkar188.expensedaroga.utils.NotificationHelper
import akashsarkar188.expensedaroga.utils.commonMethods.doIfFalse
import akashsarkar188.expensedaroga.utils.commonMethods.doIfTrue
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import android.animation.LayoutTransition
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
        FragmentHelper.setContext(this)

        init()
    }

    private fun init() {
        binding?.openBubbleFAB?.setOnClickListener {
            NotificationHelper.openTransactionsInBubble(this, getCurrentMonthYearString())
        }

        initBottomNavigation()
    }

    private val homeFragment : HomeFragment by lazy {
        HomeFragment()
    }
    private val settingsFragment : SettingsFragment by lazy {
        SettingsFragment()
    }

    private fun initBottomNavigation() {
        binding?.homeLayout?.parentCard?.setOnClickListener {
            changeBottomNavSelection(binding?.homeContainerLinearLayout, binding?.homeLayout, true)
            changeBottomNavSelection(binding?.settingsContainerLinearLayout, binding?.settingsLayout, false)
            if (FragmentHelper.isFragmentAdded(homeFragment)) {
                FragmentHelper.showThisFragment(homeFragment)
            } else {
                FragmentHelper.addFragment(homeFragment, false)
            }
        }

        binding?.settingsLayout?.parentCard?.setOnClickListener {
            changeBottomNavSelection(binding?.homeContainerLinearLayout, binding?.homeLayout, false)
            changeBottomNavSelection(binding?.settingsContainerLinearLayout, binding?.settingsLayout, true)
            if (FragmentHelper.isFragmentAdded(settingsFragment)) {
                FragmentHelper.showThisFragment(settingsFragment)
            } else {
                FragmentHelper.addFragment(settingsFragment, false)
            }
        }

        setupLayout(binding?.homeLayout, "Home", R.drawable.home_icon)
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
                title.visibility = View.GONE
            }
        }
    }
}