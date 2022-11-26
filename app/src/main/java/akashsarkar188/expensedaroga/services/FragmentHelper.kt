package akashsarkar188.expensedaroga.services

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.utils.commonMethods.doIfFalse
import akashsarkar188.expensedaroga.utils.commonMethods.doIfTrue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

object FragmentHelper {

    private var activityContext: WeakReference<AppCompatActivity?>? = null

    fun setContext(activity: AppCompatActivity) {
        activityContext = WeakReference(activity)
    }

    fun addFragment(fragment: Fragment, addToBackstack : Boolean = true) {
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
}