package kg.jedi.jftracker.db

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils

/**
 * @author Joodar on 4/22/18.
 */
class DbContentProvider : ContentProvider() {

    private lateinit var dbHelper: DbHelper
    private val EXPENSES = 100
    private val EXPENSES_ID = 101
    private val URIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        URIMatcher.run {
            addURI(DbContract.AUTHORITY, ExpenseEntry.TABLE_NAME, EXPENSES)
            addURI(DbContract.AUTHORITY, "${ExpenseEntry.TABLE_NAME}/#", EXPENSES_ID)
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        dbHelper = DbHelper(context)
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val db = dbHelper.writableDatabase

        val match = URIMatcher.match(uri)
        val returnUri: Uri

        when (match) {
            EXPENSES -> {
                val id = db.insert(ExpenseEntry.TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(ExpenseEntry.CONTENT_URI, id)
                } else {
                    throw android.database.SQLException("Failed to insert row into " + uri)
                }
            }
            else -> throw IllegalArgumentException("Unknown uri: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)

        return returnUri
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?): Cursor {

        val db = dbHelper.readableDatabase

        val match = URIMatcher.match(uri)
        val retCursor: Cursor

        when (match) {
            EXPENSES -> {
                retCursor = db.query(ExpenseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            else -> {
                throw IllegalArgumentException("Unknown uri: " + uri)
            }
        }
        retCursor.setNotificationUri(context.contentResolver, uri)

        return retCursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        val uriType = URIMatcher.match(uri)
        val db = dbHelper.writableDatabase
        val rowsUpdated: Int

        when (uriType) {
            EXPENSES -> rowsUpdated = db.update(ExpenseEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs)
            EXPENSES_ID -> {
                val id = uri.lastPathSegment
                rowsUpdated = if (TextUtils.isEmpty(selection)) {
                    db.update(ExpenseEntry.TABLE_NAME, values,
                            "${ExpenseEntry.COLUMN_ID} = $id", null)

                } else {
                    db.update(ExpenseEntry.TABLE_NAME, values,
                            "${ExpenseEntry.COLUMN_ID} = $id and $selection", selectionArgs)
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)
        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?,
                        selectionArgs: Array<out String>?): Int {

        val db = dbHelper.writableDatabase

        val match = URIMatcher.match(uri)
        val tasksDeleted: Int

        when (match) {
            EXPENSES_ID -> {
                val id = uri.pathSegments[1]
                tasksDeleted = db.delete(ExpenseEntry.TABLE_NAME, "${ExpenseEntry.COLUMN_ID}=?", arrayOf(id))
            }
            else -> throw IllegalArgumentException("Unknown uri: " + uri)
        }

        if (tasksDeleted != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return tasksDeleted
    }

    override fun getType(uri: Uri?): String {
        throw UnsupportedOperationException("Not yet implemented")
    }
}