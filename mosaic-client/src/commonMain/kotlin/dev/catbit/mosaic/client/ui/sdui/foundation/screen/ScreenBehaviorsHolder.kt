package dev.catbit.mosaic.client.ui.sdui.foundation.screen

import dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastData
import dev.catbit.mosaic.core.data.models.screen.ScreenModel

/**
 * Surface a screen exposes for two screen-level behaviors an event may need: publishing on the
 * screen's own broadcast channel, and moving the screen between its 3 lifecycle states — the
 * collaborator behind `EventRunningScope.screenBehaviorsHolder`, implemented by
 * `MosaicScreenStateHolder`.
 */
interface ScreenBehaviorsHolder {
    /** Publishes [data] on this screen's [dev.catbit.mosaic.client.ui.sdui.foundation.screen_tiles_broadcast.ScreenTilesBroadcastChannel]
     * — what `EventRunningScope.broadcastData(...)` delegates to. */
    fun broadcastData(data: ScreenTilesBroadcastData)

    /** Moves this screen to [state] — the mechanism behind `ChangeScreenState`, and what
     * `GetScreen`/`RefreshScreen` ultimately call once a fetch resolves. */
    fun setState(state: State)

    /** The 3 states a screen can be in — mirrors the server-authored `ScreenTileSchema.State` the
     * `ChangeScreenState` event's own `state` parameter targets. */
    sealed interface State {
        /** The screen successfully has content to show. @property screenModel the tiles/events/
         * navigation-drawer content to install. */
        data class Success(
            val screenModel: ScreenModel
        ) : State

        /** The screen failed to load — renders whatever `failureTiles`/`failureEvents` the screen's
         * `entry{}` declared. */
        data object Failure : State

        /** The screen is loading — renders whatever `initialTiles`/`initialEvents` the screen's
         * `entry{}` declared. The state every screen starts in. */
        data object Initial : State
    }
}