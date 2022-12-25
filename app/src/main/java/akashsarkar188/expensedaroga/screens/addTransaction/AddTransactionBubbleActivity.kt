package akashsarkar188.expensedaroga.screens.addTransaction

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.ActivityAddTransactionBubbleBinding
import akashsarkar188.expensedaroga.services.AddTransactionFragmentHelper
import akashsarkar188.expensedaroga.services.MainActivityFragmentHelper
import akashsarkar188.expensedaroga.utils.BUNDLE_MONTH_YEAR_STRING
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentDateObject
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import akashsarkar188.expensedaroga.utils.commonMethods.getLastDateForMonthYear
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil


class AddTransactionBubbleActivity : AppCompatActivity() {

    private var binding: ActivityAddTransactionBubbleBinding? = null
    private lateinit var addTransactionFragment: AddTransactionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction_bubble)

        AddTransactionFragmentHelper.setContext(this)
        saveMonthInViewModel(intent)
        init()
    }

    lateinit var monthYearString: String

    private fun saveMonthInViewModel(intent: Intent) {
        // no month received? lets just focus on present!
        (intent.getStringExtra(BUNDLE_MONTH_YEAR_STRING)).let {
            monthYearString = it ?: getCurrentMonthYearString()
        }
        addTransactionFragment = AddTransactionFragment.newInstance(monthYearString, false)
    }

    private fun init() {

        if (AddTransactionFragmentHelper.isFragmentAdded(addTransactionFragment)) {
            AddTransactionFragmentHelper.showThisFragment(addTransactionFragment)
        } else {
            AddTransactionFragmentHelper.addFragment(addTransactionFragment, false)
        }
    }
}