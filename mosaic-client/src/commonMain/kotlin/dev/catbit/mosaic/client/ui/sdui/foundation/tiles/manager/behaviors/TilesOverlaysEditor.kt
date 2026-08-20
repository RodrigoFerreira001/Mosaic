package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.behaviors

import dev.catbit.mosaic.core.data.schemas.tile.TileSchema

/**
 * Surface a screen's `TilesManager` exposes for adding/dismissing its id-addressed, stackable
 * overlays — bottom sheets, modal bottom sheets, and dialogs — the collaborator behind
 * [dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunningScope.tilesOverlaysEditor], and in
 * turn behind `DisplayBottomSheet`/`DismissBottomSheet`, `DisplayModalBottomSheet`/
 * `DismissModalBottomSheet`, and `DisplayDialog`/`DismissDialog`.
 *
 * These three overlay kinds are addressed by an id the caller chooses and can genuinely fail to add
 * (typically because that id is already in use) — unlike `NavigationDrawer`/`Snackbar`, which are
 * pure broadcast commands with no id and no failure mode, and so aren't part of this interface at all.
 * A dismiss doesn't remove the overlay from the tree instantly — it starts a two-phase animation
 * handshake (flip a dismissing flag, let the exit animation play, then actually remove it), except
 * for `Dialog`, whose dismissal has no exit animation to wait for.
 */
interface TilesOverlaysEditor {

    /**
     * Shows a non-modal bottom sheet built from [tileSchemas], registered under [id] so a later
     * [dismissBottomSheet] call can target it.
     *
     * @param id id to register this sheet under. Adding fails if this id is already in use by a
     * currently-showing bottom sheet.
     * @param isCancellable whether the user can dismiss it by gesture.
     * @param fill whether the sheet takes the full height.
     * @param allowsPartialExpansion whether it stops at a half-expanded state before reaching full
     * height.
     * @param tileSchemas the sheet's content.
     */
    fun addBottomSheet(
        id: String,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    /**
     * Closes the non-modal bottom sheet registered under [id], starting its two-phase dismissal
     * animation.
     *
     * @param id id of the sheet to close.
     */
    fun dismissBottomSheet(
        id: String
    ): Result<Unit>

    /**
     * Shows a modal bottom sheet built from [tileSchemas], registered under [id] — unlike
     * [addBottomSheet], this dims and blocks the content behind it.
     *
     * @param id id to register this sheet under. Adding fails if this id is already in use by a
     * currently-showing modal bottom sheet.
     * @param isCancellable whether the user can dismiss it by gesture or scrim tap.
     * @param fill whether the sheet takes the full height.
     * @param allowsPartialExpansion whether it stops at a half-expanded state before reaching full
     * height.
     * @param tileSchemas the sheet's content.
     */
    fun addModalBottomSheet(
        id: String,
        isCancellable: Boolean,
        fill: Boolean,
        allowsPartialExpansion: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    /**
     * Closes the modal bottom sheet registered under [id], starting its two-phase dismissal
     * animation.
     *
     * @param id id of the sheet to close.
     */
    fun dismissModalBottomSheet(
        id: String
    ): Result<Unit>

    /**
     * Shows a dialog built from [tileSchemas], registered under [id].
     *
     * @param id id to register this dialog under. Adding fails if this id is already in use by a
     * currently-showing dialog.
     * @param isCancellable whether the back gesture or a scrim tap dismisses it.
     * @param usePlatformDefaultWidth `true` keeps the platform's default dialog width; `false` lets
     * the dialog size itself from its content.
     * @param tileSchemas the dialog's content.
     */
    fun addDialog(
        id: String,
        isCancellable: Boolean,
        usePlatformDefaultWidth: Boolean,
        tileSchemas: List<TileSchema>
    ): Result<Unit>

    /**
     * Closes the dialog registered under [id]. Unlike the bottom sheet variants, this resolves
     * without waiting for an exit animation.
     *
     * @param id id of the dialog to close.
     */
    fun dismissDialog(
        id: String
    ): Result<Unit>
}