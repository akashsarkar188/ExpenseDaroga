package akashsarkar188.expensedaroga.screens.home

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.CommonElementEmptyStateBinding
import akashsarkar188.expensedaroga.databinding.RowMonthlyHistoryBinding
import akashsarkar188.expensedaroga.screens.addTransaction.adapter.EmptyStateViewHolder
import akashsarkar188.expensedaroga.utils.RECYCLER_DATA_VIEW
import akashsarkar188.expensedaroga.utils.RECYCLER_NO_DATA_VIEW
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentFullMonthYearStringFromMonthYear
import akashsarkar188.expensedaroga.utils.commonMethods.getCurrentMonthYearString
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(val callback: (month : MonthDataModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: ArrayList<MonthDataModel> by lazy {
        ArrayList<MonthDataModel>()
    }
    lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) {
            RECYCLER_NO_DATA_VIEW
        } else {
            RECYCLER_DATA_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == RECYCLER_NO_DATA_VIEW) {
            val binding: CommonElementEmptyStateBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.common_element_empty_state,
                parent,
                false
            )
            binding.titleTextView.text = "No History Available!"
            binding.messageTextView.text = "Great people create history 😉"
            binding.imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.incognito_icon
                )
            )
            binding.imageView.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blackOpacity60))

            EmptyStateViewHolder(binding)
        } else {
            HistoryViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_monthly_history,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == RECYCLER_DATA_VIEW) {
            if (holder is HistoryViewHolder) {
                holder.bindView(list[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) {
            1
        } else {
            list.size
        }
    }

    fun addData(data: ArrayList<MonthDataModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun getThisItem(position: Int): MonthDataModel {
        if (list.size > position) {
            return list[position]
        }
        return MonthDataModel(month = "Aug 2022")
    }

    inner class HistoryViewHolder(private val binding: RowMonthlyHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(row: MonthDataModel, position: Int) {
            binding.monthTextView.text = getCurrentFullMonthYearStringFromMonthYear(row.month!!)
            binding.monthInitialTextView.text = row.month!!.substring(0, 1)
            binding.debitedAmountTextView.text = "₹${row.debitAmount.toString()}"
            binding.debitedAmountTextView2.text = "₹${row.debitAmount.toString()}"
            binding.creditedAmountTextView.text = "₹${row.creditAmount.toString()}"
            binding.loanTakenAmountTextView.text = "₹${row.loanTakenAmount.toString()}"
            binding.loanGivenAmountTextView.text = "₹${row.loanGivenAmount.toString()}"

            binding.debitedAmountTextView.isSelected = true
            binding.debitedAmountTextView2.isSelected = true
            binding.creditedAmountTextView.isSelected = true
            binding.loanTakenAmountTextView.isSelected = true
            binding.loanGivenAmountTextView.isSelected = true

            binding.parentCardView.setOnClickListener {
                if (binding.expandedLinearLayout.visibility == View.VISIBLE) {
                    binding.expandedLinearLayout.visibility = View.GONE
                } else {
                    binding.expandedLinearLayout.visibility = View.VISIBLE
                }

                //callback(row)
            }
        }
    }
}