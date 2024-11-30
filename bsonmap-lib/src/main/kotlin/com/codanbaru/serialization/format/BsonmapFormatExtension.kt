package com.codanbaru.serialization.format

import kotlinx.serialization.serializer
import org.bson.BsonDocument
import org.bson.BsonValue

public inline fun <reified T> BsonmapFormat.encodeToDocument(value: T): BsonDocument =
    encodeToValue(serializersModule.serializer(), value).asDocument()
public inline fun <reified T> BsonmapFormat.encodeToValue(value: T): BsonValue =
    encodeToValue(serializersModule.serializer(), value)

public inline fun <reified T> BsonmapFormat.decodeFromDocument(value: BsonDocument): T =
    decodeFromValue(serializersModule.serializer(), value)
public inline fun <reified T> BsonmapFormat.decodeFromValue(value: BsonValue): T =
    decodeFromValue(serializersModule.serializer(), value)
