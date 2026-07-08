package dev.catbit.mosaic.client.application

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityHolder {

    @Volatile
    private var activity: WeakReference<Activity>? = null

    fun provideActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    fun getActivity(): Activity = activity?.get() ?: throw RuntimeException("ActivityHolder: Activity not provided")

    fun release() {
        activity = null
    }
}