package dev.catbit.mosaic.core.serialization.serializers

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializes an [ImmutableMap] the same way a plain `Map` would (delegating entirely to
 * `MapSerializer`), converting back to [ImmutableMap] on decode — the map-shaped counterpart of
 * [ImmutableListSerializer]. Reached via the [SerializableImmutableMap] typealias.
 */
class ImmutableMapSerializer<K, V>(
    keySerializer: KSerializer<K>,
    valueSerializer: KSerializer<V>
) : KSerializer<ImmutableMap<K, V>> {

    private val delegate = MapSerializer(keySerializer, valueSerializer)

    override val descriptor: SerialDescriptor get() = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableMap<K, V>) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableMap<K, V> =
        delegate.deserialize(decoder).toImmutableMap()
}

/** `ImmutableMap<K, V>` pre-wired to [ImmutableMapSerializer] — the map-shaped counterpart of
 * [SerializableImmutableList], used by any schema field declared as an immutable map. */
typealias SerializableImmutableMap<K, V> = @Serializable(with = ImmutableMapSerializer::class) ImmutableMap<K, V>
