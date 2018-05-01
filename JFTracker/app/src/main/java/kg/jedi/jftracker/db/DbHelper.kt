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
        db.execSQL(SQL_DROP_ENTRIES)
        onCreate(db)
    }

    companion object {
        private val DATABASE_NAME = "JFinance.db"
        private val VERSION = 1
        private val SQL_CREATE_ENTRIES = ExpenseEntry.SQL_CREATE_ENTRY
        private val SQL_DROP_ENTRIES = ExpenseEntry.SQL_DROP_ENTRY
    }
}