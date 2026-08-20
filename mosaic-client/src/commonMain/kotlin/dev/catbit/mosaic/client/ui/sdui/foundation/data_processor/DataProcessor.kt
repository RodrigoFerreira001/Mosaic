package dev.catbit.mosaic.client.ui.sdui.foundation.data_processor

import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope

/**
 * A named, pluggable handler the server DSL's `ProcessData` event dispatches `incomingData` to by
 * [id] — the mechanism behind `ProcessData(processWith = id)`. What "processing" means is entirely
 * opaque to the framework and defined by each implementation.
 *
 * A custom implementation is registered exactly like a custom Tile/Event, but through plain Koin
 * multibinding instead of a `Definition`/`MosaicDependencyInjectionConfig` list: bind it to this
 * interface in the `additionalKoinModule` passed to `mosaicDependencyInjectionConfig` —
 * `single { MyProcessor } bind DataProcessor::class` — the same pattern the framework's own
 * [dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.processors.EventRunnerDataProcessor]
 * uses in `MosaicModules.dataProcessorsModule`. `ProcessData` resolves every bound `DataProcessor` via
 * `getAll<DataProcessor>()` and picks the one whose [id] matches `processWith`; two processors
 * registered under the same [id] means only one of them is reachable, decided by Koin's own
 * resolution order, not the framework.
 */
interface DataProcessor {
    /** Id this processor is addressed by from `ProcessData(processWith = id)`. Must be unique across
     * every `DataProcessor` bound in the app for `ProcessData` to reliably reach this one. */
    val id: String

    /**
     * Processes [data] — [EventRunningScope.incomingData], already validated non-null by
     * `ProcessDataEventRunner` before this is called.
     *
     * @receiver [EventRunningScope] of the `ProcessData` event that dispatched to this processor —
     * gives access to the same collaborators (data holders, Koin, tile editing, etc.) any
     * `EventRunner` has.
     * @param data the non-null `incomingData` to process.
     * @return `Result.success(Unit)` to have `ProcessData` fire its own `onSuccess`, or a failed
     * `Result` to have it fire `onFailure`.
     */
    suspend fun EventRunningScope.process(
        data: Any
    ): Result<Unit>
}