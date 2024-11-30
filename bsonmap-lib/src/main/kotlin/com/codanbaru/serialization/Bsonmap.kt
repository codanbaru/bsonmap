package com.codanbaru.serialization

import com.codanbaru.serialization.coder.BsonmapDecoder
import com.codanbaru.serialization.coder.BsonmapEncoder
import com.codanbaru.serialization.format.BsonmapFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonValue

public abstract class Bsonmap internal constructor(
    val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule
): BsonmapFormat {

    public companion object Default : Bsonmap(BsonmapConfiguration(), EmptySerializersModule())
    internal class Impl(configuration: BsonmapConfiguration, serializersModule: SerializersModule): Bsonmap(configuration, serializersModule)

    override fun <T> encodeToValue(serializer: SerializationStrategy<T>, value: T): BsonValue {
        lateinit var attributeValue: BsonValue
        val encoder = BsonmapEncoder(
            property = "\$",

            desiredType = null,

            configuration = configuration,
            serializersModule = serializersModule,
            consumer = { attributeValue = it  }
        )
        encoder.encodeSerializableValue(serializer, value)
        return attributeValue
    }

    override fun <T> decodeFromValue(deserializer: DeserializationStrategy<T>, value: BsonValue): T {
        val decoder = BsonmapDecoder(
            attributeValue = value,

            property = "\$",

            desiredType = null,

            configuration = configuration,
            serializersModule = serializersModule
        )
        return decoder.decodeSerializableValue(deserializer)
    }
}
