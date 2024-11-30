package com.codanbaru.serialization

import kotlinx.serialization.modules.SerializersModule

public class BsonmapBuilder internal constructor(bsonmap: Bsonmap) {
    public var serializersModule: SerializersModule = bsonmap.serializersModule

    public var classDiscriminator: String = bsonmap.configuration.classDiscriminator

    public var evaluateUndefinedAttributesAsNullAttribute: Boolean = bsonmap.configuration.evaluateUndefinedAttributesAsNullAttribute

    public var booleanLiteral: BsonmapConfiguration.BooleanLiteral = bsonmap.configuration.booleanLiteral

    fun build(): BsonmapConfiguration {
        return BsonmapConfiguration(
            classDiscriminator = this@BsonmapBuilder.classDiscriminator,
            evaluateUndefinedAttributesAsNullAttribute = this@BsonmapBuilder.evaluateUndefinedAttributesAsNullAttribute,
            booleanLiteral = this@BsonmapBuilder.booleanLiteral
        )
    }
}
