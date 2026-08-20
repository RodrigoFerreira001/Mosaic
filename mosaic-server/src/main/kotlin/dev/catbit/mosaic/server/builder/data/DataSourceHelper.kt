package dev.catbit.mosaic.server.builder.data

import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.serialization.serializers.AnySerializable

/** A literal map of values baked into the event itself — not a real store, just static data to read. */
fun inlineData(data: Map<String, AnySerializable?>) = DataSourceSchema.Inline(data)

/** A literal set of key-value pairs baked into the event itself — not a real store, just static data to read. */
fun inlineData(vararg data: Pair<String, AnySerializable?>) = DataSourceSchema.Inline(data.toMap())

/** App-global, in-memory flat key-value store, shared across every screen for the app process's lifetime (not persisted). */
fun applicationPlainData() = DataSourceSchema.ApplicationPlainData

/** App-global, in-memory segmented store under [segmentId], shared across every screen for the app process's lifetime (not persisted). */
fun applicationSegmentedData(segmentId: String) = DataSourceSchema.ApplicationSegmentedData(segmentId)

/** Persistent (SQLite-backed) segmented store under [segmentId], surviving app restarts. Reads and writes must use the same [segmentId]. */
fun segmentedDataBase(segmentId: String) = DataSourceSchema.SegmentedDataBase(segmentId)

/** Persistent (SQLite-backed) flat key-value store, surviving app restarts. */
fun plainDataBase() = DataSourceSchema.PlainDataBase

/** Read-only data passed into the current screen via `Navigate`. */
fun screenNavigationData() = DataSourceSchema.ScreenNavigationData

/** In-memory flat key-value store scoped to the current screen's ViewModel — cleared when the screen is left. */
fun screenPlainData() = DataSourceSchema.ScreenPlainData

/** In-memory segmented store under [segmentId], scoped to the current screen's ViewModel — cleared when the screen is left. */
fun screenSegmentedData(segmentId: String) = DataSourceSchema.ScreenSegmentedData(segmentId)

/** Reads the current value held by another tile — [tileId]'s [dataKey] property (e.g. a `TextField`'s `"text"`). Read-only. */
fun tile(tileId: String, dataKey: String) = DataSourceSchema.Tile(tileId, dataKey)
