package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readChar(type: BsonType, configuration: BsonmapConfiguration): Char {
    return when (type) {
        BsonType.INT32 -> readCharAsInt32(configuration)
        BsonType.INT64 -> readCharAsInt64(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun BsonValue.readCharAsInt32(configuration: BsonmapConfiguration): Char {
    when (this) {
        is BsonInt32 -> return value.toChar()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT32
        )
    }
}
private fun BsonValue.readCharAsInt64(configuration: BsonmapConfiguration): Char {
    when (this) {
        is BsonInt64 -> return value.toChar()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT64
        )
    }
}
