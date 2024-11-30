package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun Long.writeLong(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.INT32 -> writeLongAsInt32(configuration)
        BsonType.INT64 -> writeLongAsInt64(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun Long.writeLongAsInt32(configuration: BsonmapConfiguration): BsonValue {
    val value: Int = toInt()

    return BsonInt32(value)
}
private fun Long.writeLongAsInt64(configuration: BsonmapConfiguration): BsonValue {
    val value: Long = toLong()

    return BsonInt64(value)
}
