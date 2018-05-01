package kg.jedi.jftracker.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.jedi.jftracker.R
import kg.jedi.jftracker.adapter.ActiveExpenseAdapter
import kg.jedi.jftracker.db.dao.ExpenseDao
import kg.jedi.jftracker.db.model.ExpenseStatus
import kg.jedi.jftracker.modal.AddExpenseModal
import kg.jedi.jftracker.modal.ContentUpdatedListener
import kotlinx.android.synthetic.main.home_fragment.*
import kotlin.concurrent.thread


class HomeFragment : Fragment(), ContentUpdatedListener {

    private lateinit var expenseDao: ExpenseDao
    private lateinit var activeExpenseAdapter: ActiveExpenseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        expenseDao = ExpenseDao(activity)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        activeExpenseAdapter = ActiveExpenseAdapter(activity)
        recViewExpense.adapter = activeExpenseAdapter
        recViewExpense.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        setSwipeListeners(recViewExpense)

        btnExpense.setOnClickListener {
            val expenseModal = AddExpenseModal(activity, this)
            expenseModal.showModal()
        }
        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun updated() {
        loadData()
    }

    private fun loadData() {
        thread {
            val data = expenseDao.findAllByStatus(ExpenseStatus.ACTIVE.name)
            activity.runOnUiThread {
                activeExpenseAdapter.updateData(data)
            }
        }
    }

    private fun setSwipeListeners(recyclerView: RecyclerView) {

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                thread {
                    val id = viewHolder.itemView.tag as String
                    expenseDao.updateExpenseStatus(id, ExpenseStatus.ARCHIVED.name)
                    loadData()
                }
            }
        }).attachToRecyclerView(recyclerView)
    }
}
