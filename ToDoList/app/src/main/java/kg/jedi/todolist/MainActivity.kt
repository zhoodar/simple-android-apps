package kg.jedi.todolist

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.widget.Toast
import kg.jedi.todolist.data.TaskContract
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mAdapter: CustomCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = CustomCursorAdapter(this)

        recyclerViewTasks.adapter = mAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        setSwipeListeners(recyclerViewTasks)

        btnAddTask.setOnClickListener({
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        })

        supportLoaderManager.initLoader(TASK_LOADER_ID, null, this)
    }

    private fun setSwipeListeners(recyclerView: RecyclerView) {

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val id = viewHolder.itemView.tag as Int
                val stringId = Integer.toString(id)

                var uri = TaskContract.TaskEntry.CONTENT_URI
                uri = uri.buildUpon().appendPath(stringId).build()

                contentResolver.delete(uri, null, null)
                Toast.makeText(baseContext, "Task has deleted!", Toast.LENGTH_SHORT).show()
                supportLoaderManager.restartLoader(TASK_LOADER_ID, null, this@MainActivity)
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(TASK_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, loaderArgs: Bundle?): Loader<Cursor> {
        return object : AsyncTaskLoader<Cursor>(this) {

            internal var mTaskData: Cursor? = null

            override fun onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData)
                } else {
                    forceLoad()
                }
            }

            override fun loadInBackground(): Cursor? {
                try {
                    return contentResolver.query(TaskContract.TaskEntry.CONTENT_URI,
                            null, null, null,
                            TaskContract.TaskEntry.COLUMN_PRIORITY)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to asynchronously load data.")
                    e.printStackTrace()
                    return null
                }
            }

            override fun deliverResult(data: Cursor?) {
                mTaskData = data
                super.deliverResult(data)
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val TASK_LOADER_ID = 0
    }

}

