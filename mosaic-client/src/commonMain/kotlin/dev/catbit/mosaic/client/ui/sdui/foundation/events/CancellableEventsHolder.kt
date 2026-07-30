package dev.catbit.mosaic.client.ui.sdui.foundation.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CancellableEventsHolder {

    private val runningJobs = mutableMapOf<String, Job>()

    fun runEvents(
        cancellableEventId: String,
        executionBlock: suspend () -> Unit
    ) {
        runningJobs[cancellableEventId] = CoroutineScope(
            context = Dispatchers.Main.immediate + SupervisorJob()
        ).launch {
            executionBlock()
        }.also { cancellableJob ->
            cancellableJob.invokeOnCompletion {
                runningJobs.remove(cancellableEventId)
            }
        }
    }

    fun cancelEvents(
        cancellableEventId: String
    ) {
        runningJobs[cancellableEventId]?.let { cancellableJob ->
            cancellableJob.cancel()
            runningJobs.remove(cancellableEventId)
        }
    }
}