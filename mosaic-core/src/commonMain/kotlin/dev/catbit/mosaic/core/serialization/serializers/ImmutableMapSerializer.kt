package dev.catbit.mosaic.core.serialization.serializers

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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

typealias SerializableImmutableMap<K, V> = @Serializable(with = ImmutableMapSerializer::class) ImmutableMap<K, V>
