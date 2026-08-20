package dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder

import dev.catbit.mosaic.core.serialization.MosaicSerializer

/**
 * Receiver passed to [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolder.update]/
 * [dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolder.update] — bundles the
 * two collaborators a JSON-patch merge needs: [serializer] to encode the current schema, merge the
 * patch, and decode the result back into a typed instance, and [builderScope] to rebuild `events`
 * holders when the patch changes an owner's own `events` list.
 *
 * @property serializer the framework's [MosaicSerializer], used for the encode/merge/decode cycle.
 * @property builderScope used to rebuild affected child holders after a patch is applied.
 */
class UpdateScope(
    val serializer: MosaicSerializer,
    val builderScope: BuilderScope
)