package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun Char.writeChar(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.INT32 -> writeCharAsInt32(configuration)
        BsonType.INT64 -> writeCharAsInt64(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun Char.writeCharAsInt32(configuration: BsonmapConfiguration): BsonValue {
    val value: Int = toInt()

    return BsonInt32(value)
}
private fun Char.writeCharAsInt64(configuration: BsonmapConfiguration): BsonValue {
    val value: Long = toLong()

    return BsonInt64(value)
}