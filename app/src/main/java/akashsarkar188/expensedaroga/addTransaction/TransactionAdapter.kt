package akashsarkar188.expensedaroga.addTransaction

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.RowTransactionBinding
import akashsarkar188.expensedaroga.utils.Constants
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var list: ArrayList<TransactionDataModel>
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) {
            Constants.RECYCLER_NO_DATA_VIEW
        } else {
            Constants.RECYCLER_DATA_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.RECYCLER_NO_DATA_VIEW) {
            TransactionViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_transaction,
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
        if (getItemViewType(position) == Constants.RECYCLER_DATA_VIEW) {
            if (holder is TransactionViewHolder) {
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

    class TransactionViewHolder(binding: RowTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(row: TransactionDataModel, position: Int) {

        }
    }
}
