package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonDouble
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readFloat(type: BsonType, configuration: BsonmapConfiguration): Float {
    return when (type) {
        BsonType.DOUBLE -> readFloatAsDouble(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.DOUBLE)
        )
    }
}

private fun BsonValue.readFloatAsDouble(configuration: BsonmapConfiguration): Float {
    when (this) {
        is BsonDouble -> return value.toFloat()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.DOUBLE
        )
    }
}
