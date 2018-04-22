package kg.jedi.jftracker.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author Joodar on 4/22/18.
 */
class DbHelper(context: Context?)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private val DATABASE_NAME = "JFinance.db"
        private val VERSION = 1
        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${ExpenseEntry.TABLE_NAME} (" +
                        "${ExpenseEntry.COLUMN_ID} INTEGER PRIMARY KEY," +
                        "${ExpenseEntry.COLUMN_TYPE} VARCHAR(50) NOT NULL, " +
                        "${ExpenseEntry.COLUMN_CREATED_AT} DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "${ExpenseEntry.COLUMN_SUM} INTEGER NOT NULL);"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ExpenseEntry.TABLE_NAME};"
    }
}