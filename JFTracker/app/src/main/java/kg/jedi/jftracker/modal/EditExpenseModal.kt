package kg.jedi.jftracker.modal

import android.support.v4.app.FragmentActivity
import kg.jedi.jftracker.R
import kg.jedi.jftracker.db.model.Expense

/**
 * @author Joodar on 5/13/18.
 */
class EditExpenseModal(activity: FragmentActivity,
                       private var expense: Expense,
                       private val listener: ContentUpdatedListener) : ExpenseModal(activity) {

    fun showModal() {
        super.initModal()
        setListeners()
        initData()
        super.show()
    }

    private fun initData() {
        btnAdd.text = context.getString(R.string.btn_lbl_update)
        etSum.setText(expense.sum.toString())
        val position = spnAdapter.getPosition(expense.type)
        spinnerTypes.setSelection(position + 1)
    }

    private fun setListeners() {
        btnAdd.setOnClickListener {
            val howMuch = Integer.valueOf(etSum.text.toString())
            val expenseType = spinnerTypes.selectedItem.toString()

            expense.apply {
                sum = howMuch
                type = expenseType
            }

            expenseDao.updateExpense(expense)
            listener.updated()
            super.dismiss()
        }
    }
}