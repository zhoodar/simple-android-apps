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
import kg.jedi.jftracker.db.model.ExpenseType

/**
 * @author Joodar on 5/12/18.
 */
open class ExpenseModal(private val activity: FragmentActivity) : Dialog(activity) {

    open lateinit var etSum: EditText
    open lateinit var btnAdd: Button
    open lateinit var spinnerTypes: MaterialSpinner
    open lateinit var spnAdapter: ArrayAdapter<String>
    open val expenseDao: ExpenseDao = ExpenseDao(activity)

    protected fun initModal() {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setCancelable(true)
        super.setContentView(R.layout.add_expense_lay)

        etSum = super.findViewById(R.id.etSum)
        btnAdd = super.findViewById(R.id.btnAdd)
        spinnerTypes = super.findViewById(R.id.btrSpnType)

        initSpinner(activity)
    }

    private fun initSpinner(activity: FragmentActivity) {
        val types = arrayOf(
                ExpenseType.SHOPING.name, ExpenseType.ACTIVITIES.name,
                ExpenseType.FOOD.name, ExpenseType.DEPOSIT.name,
                ExpenseType.TRANSPORT.name, ExpenseType.GROCERIES.name,
                ExpenseType.NRMI.name, ExpenseType.LO.name)

        spnAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, types)
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypes.adapter = spnAdapter
    }
}

interface ContentUpdatedListener {
    fun updated()
}