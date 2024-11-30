package com.codanbaru.serialization.extension

import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.BsonValue

internal fun BsonValue.asDocumentOrNull(): BsonDocument? {
    return when (this) {
        is BsonDocument -> this

        else -> null
    }
}

internal fun BsonValue.asArrayOrNull(): BsonArray? {
    return when (this) {
        is BsonArray -> this

        else -> null
    }
}

internal fun BsonValue.asStringOrNull(): BsonString? {
    return when (this) {
        is BsonString -> this

        else -> null
    }
}
