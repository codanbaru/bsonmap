package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readShort(type: BsonType, configuration: BsonmapConfiguration): Short {
    return when (type) {
        BsonType.INT32 -> readShortAsInt32(configuration)
        BsonType.INT64 -> readShortAsInt64(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun BsonValue.readShortAsInt32(configuration: BsonmapConfiguration): Short {
    when (this) {
        is BsonInt32 -> return value.toShort()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT32
        )
    }
}
private fun BsonValue.readShortAsInt64(configuration: BsonmapConfiguration): Short {
    when (this) {
        is BsonInt64 -> return value.toShort()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT64
        )
    }
}
