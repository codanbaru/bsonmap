package com.codanbaru.serialization.format

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy
import org.bson.BsonValue

interface BsonmapFormat: SerialFormat {
    fun <T> encodeToValue(serializer: SerializationStrategy<T>, value: T): BsonValue
    fun <T> decodeFromValue(deserializer: DeserializationStrategy<T>, value: BsonValue): T
}
