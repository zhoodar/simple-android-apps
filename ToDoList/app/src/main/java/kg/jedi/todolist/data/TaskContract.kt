package kg.jedi.todolist.data

import android.net.Uri

/**
 * @author Joodar on 3/17/18.
 */
class TaskContract {

    companion object {
        val AUTHORITY = "kg.jedi.todolist"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY)
        val PATH_TASKS = "tasks"
    }

    class TaskEntry {

        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build()
            val TABLE_NAME = "tasks"

            val _ID = "_id";
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_PRIORITY = "priority"
        }
    }
}
