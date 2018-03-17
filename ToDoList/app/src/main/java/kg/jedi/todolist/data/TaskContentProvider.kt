package kg.jedi.todolist.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import kg.jedi.todolist.data.TaskContract.TaskEntry


/**
 * @author Joodar on 3/17/18.
 */
class TaskContentProvider : ContentProvider() {

    private lateinit var mTaskDbHelper: TaskDbHelper

    companion object {
        val TASKS = 500
        val TASK_WITH_ID = 501
        private val sUriMatcher = buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS)
            uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID)

            return uriMatcher
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        mTaskDbHelper = TaskDbHelper(context!!)
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val db = mTaskDbHelper.writableDatabase

        val match = sUriMatcher.match(uri)
        val returnUri: Uri

        when (match) {
            TASKS -> {
                val id = db.insert(TaskEntry.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskEntry.CONTENT_URI, id)
                } else {
                    throw android.database.SQLException("Failed to insert row into " + uri)
                }
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)

        return returnUri
    }

    override fun query(uri: Uri?, projection: Array<out String>?,
                       selection: String?, selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {
        val db: SQLiteDatabase = mTaskDbHelper.readableDatabase

        val match = sUriMatcher.match(uri)
        val retCursor: Cursor

        when (match) {
            TASKS -> {
                retCursor = db.query(TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            else -> {
                throw UnsupportedOperationException("Unknown uri: " + uri)
            }
        }
        retCursor.setNotificationUri(context.contentResolver, uri)

        return retCursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = mTaskDbHelper.writableDatabase

        val match = sUriMatcher.match(uri)
        val tasksDeleted: Int

        when (match) {
            TASK_WITH_ID -> {
                val id = uri.pathSegments[1]
                tasksDeleted = db.delete(TaskEntry.TABLE_NAME, "_id=?", arrayOf(id))
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        if (tasksDeleted != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return tasksDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri?): String {
        throw UnsupportedOperationException("Not yet implemented")
    }
}