package kg.jedi.jftracker.db.dao

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import kg.jedi.jftracker.db.ExpenseEntry
import kg.jedi.jftracker.db.model.Expense
import java.util.*

/**
 * @author Joodar on 4/22/18.
 */
class ExpenseDao(context: Context) {

    private val contentResolver: ContentResolver = context.contentResolver

    fun addExpense(expense: Expense) {
        val values = ContentValues()
        values.put(ExpenseEntry.COLUMN_SUM, expense.sum)
        values.put(ExpenseEntry.COLUMN_TYPE, expense.type)
        values.put(ExpenseEntry.COLUMN_STATUS, expense.status)
        values.put(ExpenseEntry.COLUMN_CREATED_AT, expense.createdAt.timeInMillis)

        contentResolver.insert(ExpenseEntry.CONTENT_URI, values)
    }

    fun findAllByStatus(status: String): List<Expense> {
        val result = mutableListOf<Expense>()

        val selection = "${ExpenseEntry.COLUMN_STATUS} = ?"

        val cursor = contentResolver.query(ExpenseEntry.CONTENT_URI, null, selection,
                arrayOf(status), null)

        while (cursor.moveToNext()) {
            val expense = buildExpense(cursor)
            result.add(expense)
        }
        cursor.close()
        return result

    }

    fun findAll(): List<Expense> {
        val result = mutableListOf<Expense>()
        val cursor = contentResolver.query(ExpenseEntry.CONTENT_URI, null, null,
                null, null)

        while (cursor.moveToNext()) {
            val expense = buildExpense(cursor)
            result.add(expense)
        }
        cursor.close()
        return result
    }

    fun updateExpenseStatus( id: String, status: String): Int {
        val uri = ExpenseEntry.CONTENT_URI.buildUpon().appendPath(id).build()

        val cv = ContentValues()
        cv.put(ExpenseEntry.COLUMN_STATUS, status)

        return contentResolver.update(uri, cv, null, null)
    }


    private fun buildExpense(cursor: Cursor): Expense {
        val id = cursor.getString(ExpenseEntry.columnIndex(ExpenseEntry.COLUMN_ID))
        val sum = Integer.valueOf(cursor.getString(
                ExpenseEntry.columnIndex(ExpenseEntry.COLUMN_SUM)))

        val type = cursor.getString(ExpenseEntry.columnIndex(ExpenseEntry.COLUMN_TYPE))

        val createdAt = java.lang.Long.valueOf(cursor.getString(
                ExpenseEntry.columnIndex(ExpenseEntry.COLUMN_CREATED_AT)))

        val date = Calendar.getInstance()
        date.timeInMillis = createdAt

        return Expense().apply {
            this.id = id
            this.status = status
            this.sum = sum
            this.type = type
            this.createdAt = date
        }
    }
}