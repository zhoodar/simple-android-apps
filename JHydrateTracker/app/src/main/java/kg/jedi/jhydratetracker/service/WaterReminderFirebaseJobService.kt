package kg.jedi.jhydratetracker.service

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import kg.jedi.jhydratetracker.Actions.ACTION_HYDRATE_REMINDER
import kg.jedi.jhydratetracker.util.executeTask

open class WaterReminderFirebaseJobService : JobService() {

    private var mBackgroundTask: AsyncTask<*, *, *>? = null

    @SuppressLint("StaticFieldLeak")
    override fun onStartJob(jobParameters: JobParameters): Boolean {

        mBackgroundTask = object : AsyncTask<Any, Any, Any?>() {
            override fun doInBackground(vararg params: Any?): Any? {
                val context = this@WaterReminderFirebaseJobService
                executeTask(context, ACTION_HYDRATE_REMINDER)
                return null
            }

            override fun onPostExecute(result: Any?) {
                jobFinished(jobParameters, false)
            }
        }

        mBackgroundTask!!.execute()
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        mBackgroundTask?.cancel(true)
        return true
    }
}

