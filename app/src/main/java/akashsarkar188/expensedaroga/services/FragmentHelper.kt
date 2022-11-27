package akashsarkar188.expensedaroga.services

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.utils.commonMethods.doIfFalse
import akashsarkar188.expensedaroga.utils.commonMethods.doIfTrue
import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

object FragmentHelper {

    private var activityContext: WeakReference<AppCompatActivity?>? = null
    private var lastActiveFragment: WeakReference<Fragment?>? = null

    fun setContext(activity: AppCompatActivity) {
        activityContext = WeakReference(activity)
    }

    fun addFragment(fragment: Fragment, addToBackstack: Boolean = true) {
        activityContext?.get()?.let { activityContext ->
            doIfFalse(activityContext.isFinishing) {
                val fragmentManager = activityContext.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(
                    R.id.mainFrameContainer,
                    fragment,
                    fragment::class.java.canonicalName
                )
                doIfTrue(addToBackstack) {
                    fragmentTransaction.addToBackStack(fragment::class.java.canonicalName)
                }
                lastActiveFragment = WeakReference(fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        activityContext?.get()?.let { activityContext ->
            doIfFalse(activityContext.isFinishing) {
                val fragmentManager = activityContext.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.mainFrameContainer,
                    fragment,
                    fragment::class.java.canonicalName
                )
                fragmentTransaction.addToBackStack(fragment::class.java.canonicalName)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    fun removeFragment(fragment: Fragment) {
        activityContext?.get()?.let { activityContext ->
            doIfFalse(activityContext.isFinishing) {
                val fragmentManager = activityContext.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    fun showThisFragment(fragment: Fragment) {
        activityContext?.get()?.let {
            val fragmentManager = it.supportFragmentManager

            if (lastActiveFragment?.get() == null) {
                fragmentManager
                    .beginTransaction()
                    .show(fragment)
                    .commitAllowingStateLoss()
            } else {
                fragmentManager
                    .beginTransaction()
                    .hide(lastActiveFragment?.get()!!)
                    .show(fragment)
                    .commitAllowingStateLoss()
            }

            lastActiveFragment = WeakReference(fragment)
        }
    }

    fun isFragmentAdded(fragment: Fragment): Boolean {
        activityContext?.get()?.let {
            return it.supportFragmentManager.findFragmentByTag(fragment.javaClass.canonicalName) == fragment
        }
        return false
    }
}