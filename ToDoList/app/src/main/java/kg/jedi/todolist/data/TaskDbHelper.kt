package kg.jedi.todolist.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kg.jedi.todolist.data.TaskContract.TaskEntry

/**
 * @author Joodar on 3/17/18.
 */
class TaskDbHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL);"

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db)
    }

    companion object {
        private val DATABASE_NAME = "tasksDb.db"
        private val VERSION = 1
    }
}