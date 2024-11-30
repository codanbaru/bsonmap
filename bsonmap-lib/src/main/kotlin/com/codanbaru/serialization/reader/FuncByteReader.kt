package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonInt32
import org.bson.BsonInt64
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readByte(type: BsonType, configuration: BsonmapConfiguration): Byte {
    return when (type) {
        BsonType.INT32 -> readByteAsInt32(configuration)
        BsonType.INT64 -> readByteAsInt64(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.INT32, BsonType.INT64)
        )
    }
}

private fun BsonValue.readByteAsInt32(configuration: BsonmapConfiguration): Byte {
    when (this) {
        is BsonInt32 -> return value.toByte()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT32
        )
    }
}
private fun BsonValue.readByteAsInt64(configuration: BsonmapConfiguration): Byte {
    when (this) {
        is BsonInt64 -> return value.toByte()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.INT64
        )
    }
}
