package dev.catbit.mosaic.client.application

import dev.catbit.mosaic.client.domain.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.base.invoke
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.GraphUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.base.ScreenStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MosaicApplicationStateHolder(
    private val getInitialGraphUseCase: GetInitialGraphUseCase
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
                    internalUIState.update {
                        State.Displaying(
                            graph = with(graphModel) {
                                GraphUIState(
                                    id = id,
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
                    }
                }
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.OnTryAgainClick -> getInitialGraph()
        }
    }
}