package kg.jedi.jftracker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kg.jedi.jftracker.fragment.HistoryFragment
import kg.jedi.jftracker.fragment.HomeFragment
import kg.jedi.jftracker.fragment.SettingFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private var toolbar: ActionBar? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        toolbar = supportActionBar
        toolbar!!.title = "Home"
        loadFragment(HomeFragment())

        navigation.setOnNavigationItemSelectedListener { handleOnNavigationItemSelected(it) }
    }

    private fun handleOnNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment
        when (item.itemId) {
            R.id.nav_home -> {
                toolbar!!.title = "Home"
                fragment = HomeFragment()
                loadFragment(fragment)
                return true
            }
            R.id.nav_history -> {
                toolbar!!.title = "Archive"
                fragment = HistoryFragment()
                loadFragment(fragment)
                return true
            }
            R.id.nav_setting -> {
                toolbar!!.title = "Setting"
                fragment = SettingFragment()
                loadFragment(fragment)
                return true
            }
        }
        return false
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
