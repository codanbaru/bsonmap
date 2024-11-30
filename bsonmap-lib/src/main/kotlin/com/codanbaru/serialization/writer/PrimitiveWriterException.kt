package com.codanbaru.serialization.writer

import org.bson.BsonType

public sealed class PrimitiveWriterException(
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
    ): PrimitiveWriterException(value, type, message, cause)

    public class UnsupportedType(
        override val value: Any,
        override val type: BsonType,
        public val supportedTypes: List<BsonType>
    ): PrimitiveWriterException(
        value = value,
        type = type,
        message = "Unable to write '$value' value as '$type' type. Supported types: ${ supportedTypes.joinToString { "'$it'" } }."
    )
}
