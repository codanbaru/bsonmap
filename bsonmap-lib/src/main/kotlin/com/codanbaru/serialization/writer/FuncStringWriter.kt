package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

internal fun String.writeString(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.STRING -> writeStringAsString(configuration)
        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.STRING)
        )
    }
}

private fun String.writeStringAsString(configuration: BsonmapConfiguration): BsonValue {
    return BsonString(this)
}
