package kg.jedi.jftracker.modal

import android.support.v4.app.FragmentActivity
import kg.jedi.jftracker.db.model.Expense

/**
 * @author Joodar on 4/30/18.
 */
class AddExpenseModal(activity: FragmentActivity,
                      private val listener: ContentUpdatedListener) : ExpenseModal(activity) {

    fun showModal() {
        super.initModal()
        setListeners()
        super.show()
    }

    private fun setListeners() {
        btnAdd.setOnClickListener {
            val howMuch = Integer.valueOf(etSum.text.toString())
            val expenseType = spinnerTypes.selectedItem.toString()
            val newExpense = Expense().apply {
                type = expenseType
                sum = howMuch
            }

            expenseDao.addExpense(newExpense)
            listener.updated()
            super.dismiss()
        }
    }
}