package kg.jedi.jftracker.db

import android.net.Uri

/**
 * @author Joodar on 4/22/18.
 */
object DbContract {
    val AUTHORITY = "kg.jedi.jftracker"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY)
}

object ExpenseEntry {
    val TABLE_NAME = "expenses"
    val CONTENT_URI = DbContract.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build()

    val COLUMN_ID = "id"
    val COLUMN_SUM = "sum"
    val COLUMN_TYPE = "type"
    val COLUMN_CREATED_AT = "created_at"
    val COLUMN_STATUS = "status"

    val SQL_CREATE_ENTRY = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY," +
            "$COLUMN_TYPE VARCHAR(50) NOT NULL, " +
            "$COLUMN_CREATED_AT INTEGER NOT NULL, " +
            "$COLUMN_SUM INTEGER NOT NULL, " +
            "$COLUMN_STATUS INTEGER NOT NULL);"

    val SQL_DROP_ENTRY = "DROP TABLE IF EXISTS $TABLE_NAME;"

    fun columnIndex(column: String) = when (column) {
        COLUMN_ID -> 0
        COLUMN_TYPE -> 1
        COLUMN_CREATED_AT -> 2
        COLUMN_SUM -> 3
        COLUMN_STATUS -> 4
        else -> 0
    }
}