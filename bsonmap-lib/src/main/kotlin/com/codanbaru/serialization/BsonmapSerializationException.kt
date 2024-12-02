package com.codanbaru.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialKind
import org.bson.BsonType

public sealed class BsonmapSerializationException(
    open val property: String,
    override val message: String?,
    override val cause: Throwable? = null,
): SerializationException(message, cause) {
    public class Exception(
        override val property: String,
        override val cause: Throwable
    ): BsonmapSerializationException(
        property = property,
        message = "[$property] Unable to decode/encode '$property' value. See attached exception.",
        cause = cause
    )

    @OptIn(ExperimentalSerializationApi::class)
    public class InvalidKind(
        override val property: String,
        kind: SerialKind
    ): BsonmapSerializationException(
        property = property,
        message = "[$property] Invalid SerialKind detected on serializer. Unable to decode/encode structure values using SerialKind '$kind'."
    )

    public class UnexpectedType(
        override val property: String,
        val value: Any,
        val type: BsonType
    ): BsonmapSerializationException(
        property = property,
        message = "[$property] Unable to read/write '$value' value as '$type' type."
    )

    public class UnexpectedUndefined(
        override val property: String
    ): BsonmapSerializationException(
        property = property,
        message = "[$property] Unable to read/write undefined value as null."
    )

    public class UnsupportedType(
        override val property: String,
        val value: Any,
        val type: BsonType,
        val supportedTypes: List<BsonType>
    ): BsonmapSerializationException(
        property = property,
        message = "[$property] Unable to read/write '$value' value as '$type' type. Supported types: ${ supportedTypes.joinToString { "'$it'" } }."
    )

    public class EnumInvalid(
        override val property: String,
        val value: Any,
        val supportedValues: List<String>
    ): BsonmapSerializationException(
        property = property,
        message = "Invalid to decode/encode enum value. Invalid '$value' value detected on a enum. Supported values: ${ supportedValues.joinToString { "'$it'" } }."
    )

    public class SetInvalid(
        override val property: String
    ): BsonmapSerializationException(
        property = property,
        message = "Invalid to decode/encode set value. Invalid type detected on a set."
    )

    public class InlineInvalid(
        override val property: String
    ): BsonmapSerializationException(
        property = property,
        message = "Invalid to decode/encode inline value."
    )

    public sealed class PolymorphicInvalid(
        override val property: String,
        override val message: String?
    ): BsonmapSerializationException(
        property = property,
        message = message
    ) {
        public class Type(
            override val property: String
        ): PolymorphicInvalid(
            property = property,
            message = "Invalid to decode/encode value with Polymorphic decoder. Polymorphic decoder can only decode type and value properties."
        )

        public class Uncompleted(
            override val property: String
        ): PolymorphicInvalid(
            property = property,
            message = "Invalid to encode value with Polymorphic decoder. Polymorphic decoder needs to encode type and value properties."
        )

        public class DiscriminatorNotPresent(
            override val property: String
        ): PolymorphicInvalid(
            property = property,
            message = "Invalid to encode value with Polymorphic decoder. Polymorphic class discriminator is not present."
        )

        public class DiscriminatorCollision(
            override val property: String
        ): PolymorphicInvalid(
            property = property,
            message = "Invalid to encode value with Polymorphic decoder. Polymorphic decoder has detected a collision between a property and the polymorphic discriminator."
        )
    }
}
