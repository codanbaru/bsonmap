package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readString(type: BsonType, configuration: BsonmapConfiguration): String {
    return when (type) {
        BsonType.STRING -> readStringAsString(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.STRING)
        )
    }
}

private fun BsonValue.readStringAsString(configuration: BsonmapConfiguration): String {
    when (this) {
        is BsonString -> return value

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.STRING
        )
    }
}
