package kg.jedi.todolist

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kg.jedi.todolist.data.TaskContract
import kotlinx.android.synthetic.main.activity_add_task.*


class AddTaskActivity : AppCompatActivity() {

    private var mPriority: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        rBtnHigh.isChecked = true
        mPriority = 1
    }

    fun onClickAddTask(view: View) {
        val input = editTextTaskDescription.text.toString()
        if (input.isEmpty()) {
            return
        }
        val contentValues = ContentValues()
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input)
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority)
        val uri = contentResolver.insert(TaskContract.TaskEntry.CONTENT_URI, contentValues)

        if (uri != null) {
            Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
        }

        finish()
    }

    fun onPrioritySelected(view: View) {
        if (rBtnHigh.isChecked) {
            mPriority = 1
        } else if (rBtnMid.isChecked) {
            mPriority = 2
        } else if (rBtnLow.isChecked) {
            mPriority = 3
        }
    }
}
