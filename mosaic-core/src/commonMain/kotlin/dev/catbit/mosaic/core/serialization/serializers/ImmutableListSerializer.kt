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

class ImmutableListSerializer<T>(elementSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {

    private val delegate = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor get() = delegate.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        delegate.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> =
        delegate.deserialize(decoder).toImmutableList()
}

typealias SerializableImmutableList<T> = @Serializable(with = ImmutableListSerializer::class) ImmutableList<T>
