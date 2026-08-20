package dev.catbit.mosaic.core.serialization.serializers

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes an [ImmutableList] the same way a plain `List` would (delegating entirely to
 * `ListSerializer`), converting back to [ImmutableList] on decode. Not used directly — reached via
 * the [SerializableImmutableList] typealias, which is what every schema's own list field (`tiles`,
 * `events`, `searchableTerms`, etc.) actually declares its type as.
 */
class ImmutableListSerializer<T>(elementSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {

    private val delegate = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor get() = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> =
        delegate.deserialize(decoder).toImmutableList()
}

/** `ImmutableList<T>` pre-wired to [ImmutableListSerializer] — every list-typed field on a
 * [dev.catbit.mosaic.core.data.schemas.tile.TileSchema]/[dev.catbit.mosaic.core.data.schemas.event.EventSchema]
 * is declared with this typealias rather than a plain `ImmutableList<T>`, so `kotlinx.serialization`
 * knows how to (de)serialize it without per-field `@Serializable(with = ...)` boilerplate. */
typealias SerializableImmutableList<T> = @Serializable(with = ImmutableListSerializer::class) ImmutableList<T>
