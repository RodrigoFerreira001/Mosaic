package dev.catbit.mosaic.client.context

import android.content.Context
import java.lang.ref.WeakReference

object AndroidContextWrapper {
    private lateinit var context: WeakReference<Context>

    fun setContext(context: Context) {
        this.context = WeakReference(context)
    }

    fun getContext(): Context? = context.get()
}