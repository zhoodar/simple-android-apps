package kg.jedi.jftracker.modal

import android.app.Dialog
import android.support.v4.app.FragmentActivity
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import fr.ganfra.materialspinner.MaterialSpinner
import kg.jedi.jftracker.R
import kg.jedi.jftracker.db.dao.ExpenseDao
import kg.jedi.jftracker.db.model.Expense
import kg.jedi.jftracker.db.model.ExpenseType

/**
 * @author Joodar on 4/30/18.
 */
class AddExpenseModal(private val activity: FragmentActivity,
                      private val listener:  ContentUpdatedListener) : Dialog(activity) {

    private lateinit var etSum: EditText
    private lateinit var btnAdd: Button
    private lateinit var spinnerTypes: MaterialSpinner
    private val expenseDao: ExpenseDao = ExpenseDao(activity)

    fun showModal() {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setCancelable(true)
        super.setContentView(R.layout.add_expense_lay)

        etSum = super.findViewById(R.id.etSum)
        btnAdd = super.findViewById(R.id.btnAdd)
        spinnerTypes = super.findViewById(R.id.btrSpnType)

        initSpinner(activity)
        setListeners()
        super.show()
    }

    private fun initSpinner(activity: FragmentActivity) {
        val types = arrayOf(
                ExpenseType.CLOTHES.name, ExpenseType.ACTIVITIES.name,
                ExpenseType.FOOD.name, ExpenseType.DEPOSIT.name,
                ExpenseType.TRANSPORT.name, ExpenseType.GROCERIES.name)

        val spnAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, types)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypes.adapter = spnAdapter
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

interface ContentUpdatedListener {
    fun updated()
}