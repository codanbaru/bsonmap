package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonBoolean
import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

internal fun Boolean.writeBoolean(type: BsonType, configuration: BsonmapConfiguration): BsonValue {
    return when (type) {
        BsonType.BOOLEAN -> writeBooleanAsBoolean(configuration)
        BsonType.STRING -> writeBooleanAsString(configuration)

        else -> throw PrimitiveWriterException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.BOOLEAN, BsonType.STRING)
        )
    }
}

private fun Boolean.writeBooleanAsBoolean(configuration: BsonmapConfiguration): BsonValue {
    val value: Boolean = this

    return BsonBoolean(value)
}
private fun Boolean.writeBooleanAsString(configuration: BsonmapConfiguration): BsonValue {
    val yesLiteral = configuration.booleanLiteral.yes
    val noLiteral = configuration.booleanLiteral.no

    return BsonString(
        when (this) {
            true -> yesLiteral
            false -> noLiteral
        }
    )
}
