package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun Int.writeInt(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.INT32 -> writeIntAsInt32(configuration)
        BsonType.INT64 -> writeIntAsInt64(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun Int.writeIntAsInt32(configuration: BsonmapConfiguration): BsonValue {
    val value: Int = toInt()

    return BsonInt32(value)
}
private fun Int.writeIntAsInt64(configuration: BsonmapConfiguration): BsonValue {
    val value: Long = toLong()

    return BsonInt64(value)
}
