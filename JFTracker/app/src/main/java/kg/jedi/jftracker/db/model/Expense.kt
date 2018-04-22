package kg.jedi.jftracker.db.model

import java.time.LocalDateTime

/**
 * @author Joodar on 4/22/18.
 */

class Expense(val id: String, val type: ExpenseType, val sum: Int, val createdAt: LocalDateTime)

enum class ExpenseType {
    FOOD, CLOTHES, GROCERIES, ACTIVITIES, TRANSPORT, DEPOSIT
}