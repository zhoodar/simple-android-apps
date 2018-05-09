package kg.jedi.jftracker.adapter

import kg.jedi.jftracker.db.model.ExpenseType

/**
 * @author Joodar on 5/9/18.
 */
class ArchiveExpense {
    var year: Int = 0
    var moth: Int = -1
    var amount: Int = 0
    var innerExpenses: List<InnerExpense> = mutableListOf()
}

class InnerExpense {
    var type: String = ExpenseType.ACTIVITIES.name
    var amount: String = "0"
}