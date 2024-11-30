package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonBoolean
import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

internal fun BsonValue.readBoolean(type: BsonType, configuration: BsonmapConfiguration): Boolean {
    return when (type) {
        BsonType.BOOLEAN -> readBooleanAsBoolean(configuration)
        BsonType.STRING -> readBooleanAsString(configuration)

        else -> throw PrimitiveReaderException.UnsupportedType(
            value = this,
            type = type,
            supportedTypes = listOf(BsonType.BOOLEAN, BsonType.STRING)
        )
    }
}

private fun BsonValue.readBooleanAsBoolean(configuration: BsonmapConfiguration): Boolean {
    when (this) {
        is BsonBoolean -> return value
        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.BOOLEAN
        )
    }
}
private fun BsonValue.readBooleanAsString(configuration: BsonmapConfiguration): Boolean {
    when (this) {
        is BsonString -> {
            val yesLiteral = when (configuration.booleanLiteral.caseSensitive) {
                true -> configuration.booleanLiteral.yes
                false -> configuration.booleanLiteral.yes.uppercase()
            }
            val noLiteral = when (configuration.booleanLiteral.caseSensitive) {
                true -> configuration.booleanLiteral.no
                false -> configuration.booleanLiteral.no.uppercase()
            }
            if (yesLiteral == noLiteral) throw PrimitiveReaderException.Generic(this,  BsonType.STRING, "Invalid boolean literal configuration detected. Yes literal and No literal are equal.", null)

            val value = when (configuration.booleanLiteral.caseSensitive) {
                true -> value
                false -> value.uppercase()
            }

            if (value == yesLiteral) return true
            if (value == noLiteral) return false

            throw PrimitiveReaderException.Generic(this,  BsonType.STRING, "Invalid boolean literal received. Booleans encoded as string need to be '$yesLiteral' or '$noLiteral'.", null)
        }

        else -> throw PrimitiveReaderException.UnexpectedType(
            value = this,
            type = BsonType.STRING
        )
    }
}
