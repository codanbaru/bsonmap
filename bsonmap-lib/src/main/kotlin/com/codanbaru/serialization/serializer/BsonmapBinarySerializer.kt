package com.codanbaru.serialization.serializer

import com.codanbaru.serialization.coder.BsonmapDecoder
import com.codanbaru.serialization.coder.BsonmapEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object BsonmapBinarySerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BsonmapBinarySerializer", PrimitiveKind.BYTE)

    override fun deserialize(decoder: Decoder): ByteArray {
        return when(decoder) {
            is BsonmapDecoder -> decoder.decodeBinary()
            else -> throw SerializationException("BsonmapBinarySerializer can be used with bsonmap serializer only!")
        }
    }

    override fun serialize(encoder: Encoder, value: ByteArray) {
        return when(encoder) {
            is BsonmapEncoder -> encoder.encodeBinary(value)
            else -> throw SerializationException("BsonmapBinarySerializer can be used with bsonmap serializer only!")
        }
    }
}
