package kg.jedi.jftracker.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.jedi.jftracker.R
import kg.jedi.jftracker.adapter.ArchiveExpense
import kg.jedi.jftracker.adapter.ArchiveExpenseAdapter
import kg.jedi.jftracker.adapter.InnerExpense
import kg.jedi.jftracker.db.dao.ExpenseDao
import kg.jedi.jftracker.db.model.ExpenseStatus
import kotlinx.android.synthetic.main.history_fragment.*
import java.util.*
import kotlin.concurrent.thread

class HistoryFragment : Fragment() {

    private lateinit var expenseDao: ExpenseDao
    private lateinit var archiveExpenseAdapter: ArchiveExpenseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        expenseDao = ExpenseDao(activity)
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        archiveExpenseAdapter = ArchiveExpenseAdapter(activity)

        recViewArchive.adapter = archiveExpenseAdapter
        recViewArchive.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadData() {
        thread {
            val archive: MutableList<ArchiveExpense> = mutableListOf()
            val expenses = expenseDao.findAllByStatus(ExpenseStatus.ARCHIVED.name)

            val groupedByYear = expenses.groupBy { it.createdAt.get(Calendar.YEAR) }
            groupedByYear.entries.forEach { byYear ->
                val year = byYear.key
                val groupedByMoth = byYear.value.groupBy { it.createdAt.get(Calendar.MONTH) }

                groupedByMoth.entries.forEach { byMonth ->
                    val listInnerExpense = mutableListOf<InnerExpense>()
                    val groupedByType = byMonth.value.groupBy { it.getType() }
                    groupedByType.entries.forEach { byType ->
                        val iExpense = InnerExpense().apply {
                            type = byType.key.name
                            amount = byType.value.map { it.sum }.sum().toString()
                        }
                        listInnerExpense.add(iExpense)
                    }

                    val expense = ArchiveExpense().apply {
                        this.year = year
                        this.moth = byMonth.key
                        this.amount = byMonth.value.map { it.sum }.sum()
                        this.innerExpenses = listInnerExpense
                    }
                    archive.add(expense)
                }
            }

            activity.runOnUiThread {
                archiveExpenseAdapter.updateData(archive)
            }
        }
    }

}
