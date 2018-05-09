package kg.jedi.jftracker.db.model

import java.util.*

/**
 * @author Joodar on 4/22/18.
 */

class Expense {
    var id: String? = null
    var type: String = ExpenseType.GROCERIES.name
    var sum: Int = 0
    var status: String = ExpenseStatus.ACTIVE.name
    var createdAt: Calendar = Calendar.getInstance()

    fun getType() = when (type) {
        "ACTIVITIES" -> ExpenseType.ACTIVITIES
        "SHOPING" -> ExpenseType.SHOPING
        "DEPOSIT" -> ExpenseType.DEPOSIT
        "FOOD" -> ExpenseType.FOOD
        "GROCERIES" -> ExpenseType.GROCERIES
        "TRANSPORT" -> ExpenseType.TRANSPORT
        else -> ExpenseType.GROCERIES
    }

    fun getStatus() = when (status) {
        "ACTIVE" -> ExpenseStatus.ACTIVE
        "ARCHIVED" -> ExpenseStatus.ARCHIVED
        else -> throw IllegalStateException("Unknown type")
    }
}

enum class ExpenseType {
    FOOD, SHOPING, GROCERIES, ACTIVITIES, TRANSPORT, DEPOSIT, NRMI
}

enum class ExpenseStatus {
    ACTIVE, ARCHIVED
}