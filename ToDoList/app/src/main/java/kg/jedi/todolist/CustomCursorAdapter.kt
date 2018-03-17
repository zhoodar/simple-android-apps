package kg.jedi.todolist

import android.content.Context
import android.database.Cursor
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import kg.jedi.todolist.data.TaskContract
import kotlinx.android.synthetic.main.task_layout.view.*

class CustomCursorAdapter(private val mContext: Context) :
        RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder>() {

    private var mCursor: Cursor? = null

    override fun getItemCount(): Int {
        return if (mCursor == null) {
            0
        } else mCursor!!.count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false)

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val idIndex = mCursor!!.getColumnIndex(TaskContract.TaskEntry._ID)
        val descriptionIndex = mCursor!!.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION)
        val priorityIndex = mCursor!!.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY)

        mCursor!!.moveToPosition(position)

        val id = mCursor!!.getInt(idIndex)
        val description = mCursor!!.getString(descriptionIndex)
        val priority = mCursor!!.getInt(priorityIndex)

        holder.itemView.tag = id
        holder.taskDescriptionView.text = description
        holder.priorityView.text = priority.toString()

        val priorityCircle = holder.priorityView.background as GradientDrawable
        val priorityColor = getPriorityColor(priority)
        priorityCircle.setColor(priorityColor)
    }

    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0

        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
            else -> {
            }
        }
        return priorityColor
    }

    fun swapCursor(c: Cursor?): Cursor? {
        if (mCursor === c) {
            return null
        }
        val temp = mCursor
        this.mCursor = c

        if (c != null) {
            this.notifyDataSetChanged()
        }
        return temp
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskDescriptionView: TextView = itemView.taskDescription
        var priorityView: TextView = itemView.priorityTextView
    }
}