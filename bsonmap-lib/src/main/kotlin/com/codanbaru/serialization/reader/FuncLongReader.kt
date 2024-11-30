package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readLong(type: BsonType, configuration: BsonmapConfiguration): Long {
    return when (type) {
        BsonType.INT32 -> readLongAsInt32(configuration)
        BsonType.INT64 -> readLongAsInt64(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun BsonValue.readLongAsInt32(configuration: BsonmapConfiguration): Long {
    when (this) {
        is BsonInt32 -> return value.toLong()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT32
        )
    }
}
private fun BsonValue.readLongAsInt64(configuration: BsonmapConfiguration): Long {
    when (this) {
        is BsonInt64 -> return value.toLong()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT64
        )
    }
}
