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

    val COLUMN_ID = "expense_id"
    val COLUMN_SUM = "sum"
    val COLUMN_TYPE = "type"
    val COLUMN_CREATED_AT = "created_at"
}