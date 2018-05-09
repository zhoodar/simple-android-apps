package kg.jedi.jftracker.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kg.jedi.jftracker.R
import kotlinx.android.synthetic.main.archive_expense_lay.view.*
import java.text.DateFormatSymbols

/**
 * @author Joodar on 4/25/18.
 */
class ArchiveExpenseAdapter(private val activity: FragmentActivity
) : RecyclerView.Adapter<ArchiveExpenseAdapter.ArchiveExpenseViewHolder>() {

    private val expenses: MutableList<ArchiveExpense> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveExpenseViewHolder {
        val context = activity.applicationContext
        val layoutIdForListItem = R.layout.archive_expense_lay
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)
        return ArchiveExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArchiveExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val dateFormatSymbols = DateFormatSymbols()

        holder.tvNum.text = (position + 1).toString()
        holder.tvMonth.text = dateFormatSymbols.months[expense.moth]
        holder.tvYear.text = expense.year.toString()
        holder.tvAmount.text = expense.amount.toString()
        holder.lstExpense.adapter = ExpenseListAdapter(activity, expense.innerExpenses)
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    fun updateData(data: List<ArchiveExpense>) {
        if (data.isEmpty()) return
        expenses.clear()
        expenses.addAll(data)
        this.notifyDataSetChanged()
    }

    inner class ArchiveExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvMonth: TextView = itemView.tvMonth
        val tvYear: TextView = itemView.tvYear
        val tvAmount: TextView = itemView.tvAmount
        val tvNum: TextView = itemView.tvNum
        val lstExpense = itemView.lstExpenses
    }
}