package dev.catbit.mosaic.client.application

import dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.version.CheckCacheVersionUseCase
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.domain.base.invoke
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Owns the app-level loading state — `MosaicApplication`'s own `ViewModel`, fetching the initial
 * navigation graph before any screen can render. This is where [CheckCacheVersionUseCase] runs
 * *before* [GetInitialGraphUseCase] (see [getInitialGraph]), so a stale local cache never serves an
 * outdated graph on cold start. Each entry's `initialTiles`/`initialEvents`/`failureTiles`/
 * `failureEvents`/transitions are staged into [ScreenExtrasHolder] as soon as the graph is known, via
 * [setupScreenExtras], ready for whenever each individual screen actually opens.
 */
internal class MosaicApplicationStateHolder(
    private val logger: MosaicLogger,
    private val getInitialGraphUseCase: GetInitialGraphUseCase,
    private val checkCacheVersionUseCase: CheckCacheVersionUseCase,
    private val screenExtrasHolder: ScreenExtrasHolder
) : ScreenStateHolder<State, Event, Effect>() {

    override val internalUIState = MutableStateFlow<State>(State.Loading)

    override fun onEvent(event: Event) {
        when (event) {
            Event.OnTryAgainClick -> tryAgain()
        }
    }

    override fun onFirstDisplay() {
        getInitialGraph()
    }

    /** Kicks off the cold-start sequence: a fixed 1-second delay (letting the splash screen show
     * briefly rather than flashing instantly), then [checkCacheVersionUseCase], then [getGraph]. */
    private fun getInitialGraph() {
        stateHolderScope.launch {
            delay(1.seconds)
            checkCacheVersionUseCase()
            getGraph()
        }
    }

    /** Fetches the initial graph and moves [internalUIState] to [State.Displaying] on success (after
     * staging every entry's extras via [setupScreenExtras]), or to [State.Failure] on failure, logging
     * the cause. */
    private suspend fun getGraph() {
        getInitialGraphUseCase()
            .onSuccess { graphModel ->
                setupScreenExtras(graphModel)
                internalUIState.update {
                    State.Displaying(
                        graph = with(graphModel) {
                            GraphUIState(
                                entries = entries.map { entry ->
                                    GraphUIState.Entry(
                                        screenId = entry.screenId,
                                        transition = entry.transition,
                                        popTransition = entry.popTransition,
                                        predictivePopTransition = entry.predictivePopTransition,
                                    )
                                },
                                startEntryId = startEntryId,
                                defaultTransition = defaultTransition,
                                defaultPopTransition = defaultPopTransition,
                                defaultPredictivePopTransition = defaultPredictivePopTransition,
                            )
                        }
                    )
                }
            }
            .onFailure { failure ->
                logger.error(failure.stackTraceToString())
                internalUIState.update {
                    State.Failure(loading = false)
                }
            }
    }

    /** Retries the initial-graph fetch — bound to `Event.OnTryAgainClick`, fired by the retry button
     * on the app-level failure screen. */
    private fun tryAgain() {
        internalUIState.update {
            State.Failure(loading = true)
        }
        stateHolderScope.launch {
            getGraph()
        }
    }


    /** Registers every graph entry's `initialTiles`/`initialEvents`/`failureTiles`/`failureEvents`/
     * transitions in [screenExtrasHolder], keyed by `screenId` — decoupling "the graph loaded" from
     * "this specific screen was opened," since a screen may go unvisited for a while after this runs. */
    private fun setupScreenExtras(graphModel: GraphModel) {
        graphModel.entries.forEach { entry ->
            with(entry) {
                screenExtrasHolder.registerExtra(
                    screenId = screenId,
                    initialTiles = initialTiles,
                    initialEvents = initialEvents,
                    failureTiles = failureTiles,
                    failureEvents = failureEvents,
                    transition = transition,
                    popTransition = popTransition,
                    predictivePopTransition = predictivePopTransition,
                )
            }
        }
    }
}