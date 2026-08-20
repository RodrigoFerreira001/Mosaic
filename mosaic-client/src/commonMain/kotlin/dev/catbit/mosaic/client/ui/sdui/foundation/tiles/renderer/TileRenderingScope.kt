package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.TileGroupEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.events.UIEvent
import dev.catbit.mosaic.client.ui.sdui.foundation.local_providers.LocalTileRendererManager
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import kotlinx.collections.immutable.ImmutableList

/**
 * Receiver passed to every [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRenderer.Render]
 * call — one fresh instance per tile, built by [TileRendererManager] right before rendering it.
 *
 * It carries the tile's own identity ([tileId]) and its own declared [events], and exposes every way a
 * renderer can react to a user interaction: mutate this tile's own local state synchronously
 * ([dispatchEvent]), notify every tile in the tree that shares a group concern ([dispatchGroupEvent]),
 * run this tile's server-declared event chains ([triggerEvent]), or render child [TileSchema]s
 * ([RenderChild]/[RenderChildren]). None of these calls block — they all funnel through [onEvent],
 * which the owning [MosaicScreenStateHolder] observes to mutate the live tile tree and trigger
 * recomposition.
 *
 * @property tileId id of the tile currently being rendered — the same value that reaches
 * [TilesEditor]/[TilesEventDispatcher] when this tile is later addressed by id.
 * @property events the events this tile schema declares (`TileSchema.events`), used by [triggerEvent]
 * to decide which of them actually fire for a given [EventTrigger]. `null` when the tile declares none.
 * @property onEvent the sink every method on this scope funnels into — a callback owned by the
 * screen's state holder that turns a [UIEvent] into a mutation of the live tile tree.
 */
@Stable
data class TileRenderingScope(
    val tileId: String,
    val events: ImmutableList<EventSchema>?,
    val onEvent: (UIEvent) -> Unit
) {
    /**
     * Applies [tileEvent] to this tile's own [TileHolder] synchronously, by emitting a
     * [UIEvent.TileEventHolderUIEvent] addressed to [tileId].
     *
     * This is the *local* half of a stateful tile's interaction — the [TileHolder] that owns this
     * tile's id receives [tileEvent] through its own `TileEventScope.onTileEvent` override and mutates
     * its schema copy directly, with no trigger matching and no coroutine involved. Every built-in
     * stateful input tile (`Checkbox`, `RadioButton`, `Switch`, chips with a `selected` state, `Tabs`)
     * calls this alongside [triggerEvent] on the same interaction, so the visual state updates
     * immediately while the server-declared event chain runs independently.
     *
     * @param tileEvent the tile-specific local mutation to apply — an instance of the `TileEvent`
     * sealed type that tile's own `TileHolder` implementation understands.
     */
    fun dispatchEvent(tileEvent: TileEvent) {
        onEvent(UIEvent.TileEventHolderUIEvent(tileId, tileEvent))
    }

    /**
     * Broadcasts [tileGroupEvent] to every [TileHolder] in the whole screen's tile tree whose
     * `handlesGroupEvent(event)` returns `true` for it, by emitting a
     * [UIEvent.TileGroupEventHolderUIEvent].
     *
     * Unlike [dispatchEvent], this isn't addressed to [tileId] alone — it reaches every matching
     * holder in the tree (not just siblings), which is how `RadioButton`'s mutual-exclusion group
     * works: selecting one radio button broadcasts a group event that every other `RadioButton` with
     * the same `groupId` reacts to independently, clearing its own `selected` state with no server
     * round trip.
     *
     * @param tileGroupEvent the group-scoped event to broadcast — an instance of the `TileGroupEvent`
     * sealed type the target tiles' `TileHolder` implementations understand.
     */
    fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent) {
        onEvent(UIEvent.TileGroupEventHolderUIEvent(tileGroupEvent))
    }

    /**
     * Runs whichever of this tile's own [events] declare a `trigger` structurally equal to [trigger] —
     * the *remote* half of a tile's interaction, and the primary way a [TileRenderer] starts a
     * server-declared event chain.
     *
     * Filtering is a plain equality check against [events], with no listener registry involved; when
     * no declared event matches, this call is a silent no-op (no [UIEvent] is emitted at all). When at
     * least one matches, a single [UIEvent.EventSchemaHolderUIEvent] carrying every match plus [data]
     * is emitted, and each matching event runs with [data] as its `incomingData`.
     *
     * @param trigger the [EventTrigger] to match this tile's declared events against.
     * @param data value passed downstream as the matching event's `incomingData`. `null` when the
     * interaction carries no payload (e.g. a plain click).
     */
    fun triggerEvent(
        trigger: EventTrigger,
        data: Any? = null
    ) {
        events
            ?.filter { it.trigger == trigger }
            ?.let {
                onEvent(
                    UIEvent.EventSchemaHolderUIEvent(
                        events = it,
                        data = data
                    )
                )
            }
    }

    /**
     * Renders one child [tileSchema], delegating to the app-wide [TileRendererManager] reached via
     * [LocalTileRendererManager] and forwarding this scope's own [onEvent] sink unchanged.
     *
     * A container [TileRenderer] (e.g. `Column`, `Card`, `Box`) calls this once per child instead of
     * looking up and invoking a `TileDefinition` by hand — the lookup, the `isGone()` visibility check,
     * and building a fresh [TileRenderingScope] for the child all happen inside [TileRendererManager].
     *
     * @param tileSchema the child tile to render.
     */
    @Composable
    fun RenderChild(
        tileSchema: TileSchema,
    ) {
        with(LocalTileRendererManager.current) {
            Render(
                tileSchema = tileSchema,
                onEvent = onEvent
            )
        }
    }

    /**
     * Renders every schema in [tileSchemas] in order, by calling the same path as [RenderChild] for
     * each one — the usual way a container [TileRenderer] renders its whole children list in one call.
     *
     * @param tileSchemas the children to render, in declaration order.
     */
    @Composable
    fun RenderChildren(
        tileSchemas: ImmutableList<TileSchema>,
    ) {
        with(LocalTileRendererManager.current) {
            tileSchemas.forEach { tileModel ->
                Render(
                    tileSchema = tileModel,
                    onEvent = onEvent
                )
            }
        }
    }
}