package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonDouble
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readDouble(type: BsonType, configuration: BsonmapConfiguration): Double {
    return when (type) {
        BsonType.DOUBLE -> readDoubleAsDouble(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.DOUBLE)
        )
    }
}

private fun BsonValue.readDoubleAsDouble(configuration: BsonmapConfiguration): Double {
    when (this) {
        is BsonDouble -> return value.toDouble()

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.DOUBLE
        )
    }
}
