package dev.catbit.mosaic.client.application

import dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import dev.catbit.mosaic.core.data.models.graph.GraphModel
import dev.catbit.mosaic.core.domain.base.invoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MosaicApplicationStateHolder(
    private val getInitialGraphUseCase: GetInitialGraphUseCase,
    private val screenExtrasHolder: ScreenExtrasHolder
) : ScreenStateHolder<State, Event, Effect>() {

    override val internalUIState = MutableStateFlow<State>(State.Loading)

    override fun onFirstDisplay() {
        getInitialGraph()
    }

    private fun getInitialGraph() {
        stateHolderScope.launch {
            internalUIState.update { State.Loading }
            getInitialGraphUseCase()
                .onSuccess { graphModel ->
                    setupScreenExtras(graphModel)
                    internalUIState.update {
                        State.Displaying(
                            graph = with(graphModel) {
                                GraphUIState(
                                    entries = entries.map { entry ->
                                        GraphUIState.Entry(entry.screenId)
                                    },
                                    startEntryId = startEntryId
                                )
                            }
                        )
                    }
                }
                .onFailure { failure ->
                    internalUIState.update {
                        State.Failure(
                            title = "Erro",
                            details = failure.stackTraceToString()
                        )
                    }.also {
                        failure.printStackTrace()
                    }
                }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.OnTryAgainClick -> getInitialGraph()
        }
    }

    private fun setupScreenExtras(graphModel: GraphModel) {
        graphModel.entries.forEach { entry ->
            with(entry) {
                screenExtrasHolder.registerExtra(
                    screenId = screenId,
                    initialTiles = initialTiles,
                    initialEvents = initialEvents,
                    failureTiles = failureTiles,
                    failureEvents = failureEvents
                )
            }
        }
    }
}