package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonDouble
import org.bson.BsonType
import org.bson.BsonValue

internal fun Double.writeDouble(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.DOUBLE -> writeDoubleAsDouble(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.DOUBLE)
        )
    }
}

private fun Double.writeDoubleAsDouble(configuration: BsonmapConfiguration): BsonValue {
    val value: Double = toDouble()

    return BsonDouble(value)
}
