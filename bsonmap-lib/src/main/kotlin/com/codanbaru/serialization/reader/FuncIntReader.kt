package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readInt(type: BsonType, configuration: BsonmapConfiguration): Int {
    return when (type) {
        BsonType.INT32 -> readIntAsInt32(configuration)
        BsonType.INT64 -> readIntAsInt64(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun BsonValue.readIntAsInt32(configuration: BsonmapConfiguration): Int {
    when (this) {
        is BsonInt32 -> return value.toInt()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT32
        )
    }
}
private fun BsonValue.readIntAsInt64(configuration: BsonmapConfiguration): Int {
    when (this) {
        is BsonInt64 -> return value.toInt()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT64
        )
    }
}

