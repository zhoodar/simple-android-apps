package kg.jedi.jftracker.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kg.jedi.jftracker.R
import kg.jedi.jftracker.db.model.Expense
import kg.jedi.jftracker.db.model.ExpenseType
import kotlinx.android.synthetic.main.active_expense_lay.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Joodar on 4/25/18.
 */
class ActiveExpenseAdapter(private val activity: FragmentActivity
) : RecyclerView.Adapter<ActiveExpenseAdapter.ActiveExpenseViewHolder>() {

    val expenses: MutableList<Expense> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveExpenseViewHolder {
        val context = activity.applicationContext
        val layoutIdForListItem = R.layout.active_expense_lay
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)
        return ActiveExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val date = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(expense.createdAt.time)

        holder.itemView.tag = expense.id
        holder.tvSum.text = expense.sum.toString()
        holder.tvTitle.text = expense.type
        holder.tvDate.text = date
        holder.ivType.setImageResource(getImageRes(expense.getType()))
    }

    private fun getImageRes(type: ExpenseType) = when (type) {
        ExpenseType.GROCERIES -> R.mipmap.ic_shop
        ExpenseType.FOOD -> R.mipmap.ic_food
        ExpenseType.TRANSPORT -> R.mipmap.ic_transport
        ExpenseType.DEPOSIT -> R.mipmap.ic_deposit
        ExpenseType.SHOPING -> R.mipmap.ic_clothes
        ExpenseType.ACTIVITIES -> R.mipmap.ic_activities
        ExpenseType.NRMI -> R.mipmap.ic_nrmi
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    fun updateData(data: List<Expense>) {
        if (data.isEmpty()) return
        expenses.clear()
        expenses.addAll(data)
        this.notifyDataSetChanged()
    }

    inner class ActiveExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivType: ImageView = itemView.ivType
        var tvTitle: TextView = itemView.tvTitle
        var tvDate: TextView = itemView.tvDate
        var tvSum: TextView = itemView.tvSum
    }
}