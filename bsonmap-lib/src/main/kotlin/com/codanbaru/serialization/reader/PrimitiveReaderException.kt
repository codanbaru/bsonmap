package com.codanbaru.serialization.reader

import org.bson.BsonType

public sealed class PrimitiveReaderException(
    open val value: Any,
    open val type: BsonType,
    override val message: String? = null,
    override val cause: Throwable? = null,
): Throwable(message, cause) {
    public class Generic(
        override val value: Any,
        override val type: BsonType,
        override val message: String?,
        override val cause: Throwable?,
    ): PrimitiveReaderException(value, type, message, cause)

    public class UnexpectedType(
        override val value: Any,
        override val type: BsonType
    ): PrimitiveReaderException(
        value = value,
        type = type,
        message = "Unable to read '$value' value as '$type' type."
    )

    public class UnsupportedType(
        override val value: Any,
        override val type: BsonType,
        public val supportedTypes: List<BsonType>
    ): PrimitiveReaderException(
        value = value,
        type = type,
        message = "Unable to read '$value' value as '$type' type. Supported types: ${ supportedTypes.joinToString { "'$it'" } }."
    )
}
