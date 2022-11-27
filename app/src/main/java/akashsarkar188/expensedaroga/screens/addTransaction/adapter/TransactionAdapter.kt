package akashsarkar188.expensedaroga.screens.addTransaction.adapter

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionDataModel
import akashsarkar188.expensedaroga.screens.addTransaction.model.TransactionType
import akashsarkar188.expensedaroga.databinding.CommonElementEmptyStateBinding
import akashsarkar188.expensedaroga.databinding.RowTransactionBinding
import akashsarkar188.expensedaroga.utils.RECYCLER_DATA_VIEW
import akashsarkar188.expensedaroga.utils.RECYCLER_NO_DATA_VIEW
import akashsarkar188.expensedaroga.utils.commonMethods.formatAsCurrency
import akashsarkar188.expensedaroga.utils.commonMethods.getDateInDD_MMM
import akashsarkar188.expensedaroga.utils.commonMethods.getTimeInHH_MM_A
import akashsarkar188.expensedaroga.utils.commonMethods.locateView
import akashsarkar188.expensedaroga.utils.popup.transaction.ActionType
import akashsarkar188.expensedaroga.utils.popup.transaction.PopupListener
import akashsarkar188.expensedaroga.utils.popup.transaction.TransactionPopup
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TransactionAdapter(val callback: (action: ActionType, dataObject: TransactionDataModel) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<TransactionDataModel> = ArrayList()
    private lateinit var context: Context

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
            EmptyStateViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.common_element_empty_state,
                    parent,
                    false
                )
            )
        } else {
            TransactionViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_transaction,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == RECYCLER_DATA_VIEW) {
            if (holder is TransactionViewHolder) {
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

    fun addData(data: ArrayList<TransactionDataModel>) {
        if (list.isNullOrEmpty()) {
            list = ArrayList()
        }
        list.clear()

        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(private val binding: RowTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(row: TransactionDataModel, position: Int) {
            binding.amountTextView.text = "₹${formatAsCurrency(num = row.amount)}"

            if (row.meta?.transactionNote.isNullOrEmpty()) {
                row.meta?.transactionNote = "Regular Transaction"
            }
            binding.noteTextView.text = row.meta?.transactionNote

            row.date?.let {
                val date = Date()
                date.time = it
                binding.dateTimeTextView.text =
                    "${getDateInDD_MMM(date)} | ${getTimeInHH_MM_A(date)}"
            }

            val params = binding.parentCardView.layoutParams as RelativeLayout.LayoutParams
            when (row.meta?.transactionType) {
                TransactionType.DEBIT -> {
                    params.addRule(RelativeLayout.ALIGN_PARENT_END)
                }
                else -> {
                    params.addRule(RelativeLayout.ALIGN_PARENT_START)
                }
            }
            binding.parentCardView.layoutParams = params

            binding.parentCardView.setOnLongClickListener {
                val location = locateView(binding.parentCardView)
                location?.let {
                    TransactionPopup(context).showAt(
                        gravity = Gravity.TOP,
                        x = location.left,
                        y = location.bottom,
                        callback = object : PopupListener {
                            override fun onActionButtonClicked(action: ActionType) {
                                callback(action, row)
                            }

                        })
                }
                return@setOnLongClickListener true
            }
        }
    }
}

class EmptyStateViewHolder(private val binding: CommonElementEmptyStateBinding) :
    RecyclerView.ViewHolder(
        binding.root
    )
