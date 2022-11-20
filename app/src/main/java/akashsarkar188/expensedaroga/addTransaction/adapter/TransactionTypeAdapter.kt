package akashsarkar188.expensedaroga.addTransaction.adapter

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.addTransaction.model.TransactionType
import akashsarkar188.expensedaroga.addTransaction.model.TransactionTypeModel
import akashsarkar188.expensedaroga.databinding.RowTransactionTypeChipBinding
import akashsarkar188.expensedaroga.utils.RECYCLER_DATA_VIEW
import akashsarkar188.expensedaroga.utils.RECYCLER_NO_DATA_VIEW
import akashsarkar188.expensedaroga.utils.commonMethods.capitalize
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.StringUtil

class TransactionTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list : ArrayList<TransactionTypeModel> = ArrayList()
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
            TransactionTypeViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_transaction_type_chip,
                    parent,
                    false
                )
            )
        } else {
            TransactionTypeViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_transaction_type_chip,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == RECYCLER_DATA_VIEW) {
            if (holder is TransactionTypeViewHolder) {
                holder.bindView(list[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            1
        } else {
            list.size
        }
    }

    fun addData(data : ArrayList<TransactionTypeModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    private inner class TransactionTypeViewHolder(private val binding: RowTransactionTypeChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(row : TransactionTypeModel, position : Int) {
            binding.textView.text = row.type.toString().capitalize().replace("_", " ")

            if (row.isSelected) {
                binding.cardView.strokeColor = ContextCompat.getColor(context, row.selectedColor)
                binding.cardLinearLayout.setBackgroundColor(ContextCompat.getColor(context, row.selectedColor))
                binding.textView.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                binding.cardView.strokeColor = ContextCompat.getColor(context, row.selectedColor)
                binding.cardLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                binding.textView.setTextColor(ContextCompat.getColor(context, row.selectedColor))
            }

            binding.cardLinearLayout.setOnClickListener {
                setThisItemSelected(row)
            }
        }
    }

    private fun setThisItemSelected(row: TransactionTypeModel) {
        for (i in list.indices) {
            list[i].isSelected = list[i].type == row.type
        }
        notifyDataSetChanged()
    }

    fun getSelectedItem(): TransactionType {
        for (i in list) {
            if (i.isSelected) {
                return i.type
            }
        }
        return TransactionType.DEBIT
    }
}