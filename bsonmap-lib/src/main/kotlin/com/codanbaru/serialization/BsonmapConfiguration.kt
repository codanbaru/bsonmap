package com.codanbaru.serialization

public data class BsonmapConfiguration(
    public val classDiscriminator: String = "__bsonmap_serialization_type",

    public val evaluateUndefinedAttributesAsNullAttribute: Boolean = true,

    public val booleanLiteral: BooleanLiteral = BooleanLiteral("TRUE", "FALSE", false)
) {

    public data class BooleanLiteral(
        val yes: String,
        val no: String,
        val caseSensitive: Boolean
    )
}
