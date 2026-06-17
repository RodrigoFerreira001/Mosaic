package dev.catbit.mosaic.client.ui.sdui.foundation.screen.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

abstract class ScreenStateHolder<State, Event, Effect> : ViewModel() {

    private var firstAccess = true

    private var internalStateHolderScope: CoroutineScope? = null
    protected val stateHolderScope: CoroutineScope
        get() = internalStateHolderScope?.let { scope ->
            if (scope.isActive) scope else createViewModelScope().also { internalStateHolderScope = it }
        } ?: createViewModelScope().also { internalStateHolderScope = it }

    protected abstract val internalUIState: MutableStateFlow<State>
    val uiState get() = internalUIState.asStateFlow()

    protected val internalEffects = MutableSharedFlow<Effect>()
    val effects get() = internalEffects.asSharedFlow()

    abstract fun onEvent(event: Event)

    protected open fun onFirstDisplay() = Unit
    protected open fun onDisplay() = Unit
    protected open fun onPause() = Unit
    protected open fun onStop() = Unit

    private fun onScreenDisplay() {
        if (firstAccess) {
            firstAccess = false
            onFirstDisplay()
        }
        onDisplay()
    }

    private fun onScreenDispose() {
        onPause()
    }

    override fun onCleared() {
        onStop()
        internalStateHolderScope?.cancel()
        super.onCleared()
    }

    @Composable
    fun bindScreenLifecycle() {
        DisposableEffect(Unit) {
            with(this@ScreenStateHolder) { onScreenDisplay() }
            onDispose {
                with(this@ScreenStateHolder) { onScreenDispose() }
            }
        }
    }

    private fun createViewModelScope() = CoroutineScope(
        try {
            Dispatchers.Main.immediate
        } catch (_: NotImplementedError) {
            EmptyCoroutineContext
        } catch (_: IllegalStateException) {
            EmptyCoroutineContext
        } + SupervisorJob()
    )

    fun MutableSharedFlow<Effect>.dispatch(effect: Effect) {
        stateHolderScope.launch {
            emit(effect)
        }
    }
}