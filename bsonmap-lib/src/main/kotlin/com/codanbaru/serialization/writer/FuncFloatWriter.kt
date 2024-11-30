package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonDouble
import org.bson.BsonType
import org.bson.BsonValue

internal fun Float.writeFloat(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.DOUBLE -> writeFloatAsDouble(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.DOUBLE)
        )
    }
}

private fun Float.writeFloatAsDouble(configuration: BsonmapConfiguration): BsonValue {
    val value: Double = toDouble()

    return BsonDouble(value)
}
private fun Float.writeFloatAsString(configuration: BsonmapConfiguration): BsonValue {
    val value: Double = toDouble()

    return BsonDouble(value)
}
