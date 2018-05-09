package kg.jedi.jftracker.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kg.jedi.jftracker.R

/**
 * @author Joodar on 5/9/18.
 */
class ExpenseListAdapter(private var activity: Activity,
                         private var items: List<InnerExpense>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_expense_lay, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val expense = items[position]
        viewHolder.tvType?.text = expense.type
        viewHolder.tvAmount?.text = expense.amount

        return view as View
    }

    override fun getItem(position: Int): InnerExpense {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    private class ViewHolder(row: View?) {
        var tvType: TextView? = row?.findViewById(R.id.tvListType)
        var tvAmount: TextView? = row?.findViewById(R.id.tvListAmount)
    }
}