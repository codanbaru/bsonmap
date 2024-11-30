package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun Short.writeShort(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.INT32 -> writeShortAsInt32(configuration)
        BsonType.INT64 -> writeShortAsInt64(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun Short.writeShortAsInt32(configuration: BsonmapConfiguration): BsonValue {
    val value: Int = toInt()

    return BsonInt32(value)
}
private fun Short.writeShortAsInt64(configuration: BsonmapConfiguration): BsonValue {
    val value: Long = toLong()

    return BsonInt64(value)
}
