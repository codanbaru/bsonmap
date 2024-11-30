package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import com.codanbaru.serialization.extension.asArrayOrNull
import com.codanbaru.serialization.extension.asDocumentOrNull
import com.codanbaru.serialization.extension.bsonTypeAnnotation
import com.codanbaru.serialization.handlePrimitiveException
import com.codanbaru.serialization.reader.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonBinary
import org.bson.BsonNull
import org.bson.BsonType
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal class BsonmapDecoder(
    private val attributeValue: BsonValue,

    private val property: String,

    private val desiredType: BsonType?,

    private val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule
): Decoder {
    private val reader: PrimitiveReader = BsonPrimitiveReader(configuration)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            PrimitiveKind.BOOLEAN -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.BYTE -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.CHAR -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.DOUBLE -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.FLOAT -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.INT -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.LONG -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.SHORT -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PrimitiveKind.STRING -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)

            PolymorphicKind.OPEN -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            PolymorphicKind.SEALED -> beginStructureOnPolymorphicDescriptorKind()

            SerialKind.CONTEXTUAL -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
            SerialKind.ENUM -> throw BsonmapSerializationException.InvalidKind(
                property,
                descriptor.kind
            ) // CHECK: Should we allow structured enums?

            StructureKind.CLASS -> beginStructureOnMapDescriptorKind()
            StructureKind.LIST -> beginStructureOnListDescriptorKind()
            StructureKind.MAP -> beginStructureOnMapDescriptorKind()
            StructureKind.OBJECT -> throw BsonmapSerializationException.InvalidKind(property, descriptor.kind)
        }
    }

    private fun beginStructureOnListDescriptorKind(): CompositeDecoder {
        val bsonType = desiredType ?: BsonType.ARRAY

        val values = when (bsonType) {
            BsonType.ARRAY -> {
                attributeValue.asArrayOrNull() ?: throw BsonmapSerializationException.UnexpectedType(
                    property = property,
                    type = bsonType,
                    value = attributeValue
                )
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = attributeValue,
                supportedTypes = listOf(BsonType.ARRAY)
            )
        }

        return BsonmapListCompositeDecoder(values, property, configuration, serializersModule)
    }

    private fun beginStructureOnMapDescriptorKind(): CompositeDecoder {
        val bsonType = desiredType ?: BsonType.DOCUMENT

        val values = when(bsonType) {
            BsonType.DOCUMENT -> {
                attributeValue.asDocumentOrNull() ?: throw BsonmapSerializationException.UnexpectedType(
                    property = property,
                    type = bsonType,
                    value = attributeValue
                )
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = attributeValue,
                supportedTypes = listOf(BsonType.DOCUMENT)
            )
        }

        return BsonmapMapCompositeDecoder(values, property, configuration, serializersModule)
    }

    private fun beginStructureOnPolymorphicDescriptorKind(): CompositeDecoder {
        val bsonType = desiredType ?: BsonType.DOCUMENT

        val values = when(bsonType) {
            BsonType.DOCUMENT -> {
                attributeValue.asDocumentOrNull() ?: throw BsonmapSerializationException.UnexpectedType(
                    property = property,
                    type = bsonType,
                    value = attributeValue
                )
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = attributeValue,
                supportedTypes = listOf(BsonType.DOCUMENT)
            )
        }

        return BsonmapPolymorphicMapCompositeDecoder(values, property, configuration, serializersModule)
    }

    override fun decodeBoolean(): Boolean = handlePrimitiveException(property) { reader.readBoolean(attributeValue, type = desiredType ?: BsonType.BOOLEAN) }
    override fun decodeByte(): Byte = handlePrimitiveException(property) { reader.readByte(attributeValue, type = desiredType ?: BsonType.INT32) }
    override fun decodeChar(): Char = handlePrimitiveException(property) { reader.readChar(attributeValue, type = desiredType ?: BsonType.INT32) }
    override fun decodeShort(): Short = handlePrimitiveException(property) { reader.readShort(attributeValue, type = desiredType ?: BsonType.INT32) }
    override fun decodeInt(): Int = handlePrimitiveException(property) { reader.readInt(attributeValue, type = desiredType ?: BsonType.INT32) }
    override fun decodeLong(): Long = handlePrimitiveException(property) { reader.readLong(attributeValue, type = desiredType ?: BsonType.INT64) }
    override fun decodeFloat(): Float = handlePrimitiveException(property) { reader.readFloat(attributeValue, type = desiredType ?: BsonType.DOUBLE) }
    override fun decodeDouble(): Double = handlePrimitiveException(property) { reader.readDouble(attributeValue, type = desiredType ?: BsonType.DOUBLE) }
    override fun decodeString(): String = handlePrimitiveException(property) { reader.readString(attributeValue, type = desiredType ?: BsonType.STRING) }

    fun decodeBinary(): ByteArray {
        return when(attributeValue) {
            is BsonBinary -> attributeValue.data

            else -> throw BsonmapSerializationException.UnexpectedType(
                property = property,
                type = BsonType.BINARY,
                value = attributeValue
            )
        }
    }

    override fun decodeNull(): Nothing? {
        return when(attributeValue) {
            is BsonNull -> null

            else -> throw BsonmapSerializationException.UnexpectedType(
                property = property,
                type = BsonType.NULL,
                value = attributeValue
            )
        }
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val element = handlePrimitiveException(property) { reader.readString(attributeValue, type = desiredType ?: BsonType.STRING) }
        val elementIndex = enumDescriptor.getElementIndex(element)

        if (elementIndex in IntRange(0, enumDescriptor.elementsCount - 1)) {
            return elementIndex
        } else {
            throw BsonmapSerializationException.EnumInvalid(property, element, enumDescriptor.elementNames.toList())
        }
    }

    override fun decodeInline(descriptor: SerialDescriptor): Decoder {
        if (descriptor.elementsCount == 1) {
            val elementAnnotation = descriptor.getElementAnnotations(0)
            // val elementDescriptor = descriptor.getElementDescriptor(0)
            // val elementName = descriptor.getElementName(0)

            val bsonType = desiredType ?: elementAnnotation.bsonTypeAnnotation

            return BsonmapDecoder(attributeValue, property, bsonType, configuration, serializersModule)
        }

        throw BsonmapSerializationException.InlineInvalid(property)
    }

    override fun decodeNotNullMark(): Boolean {
        return when(attributeValue) {
            is BsonNull -> false
            else -> true
        }
    }

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        deserializer.deserialize(this)

    override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T?  {
        val isNullabilitySupported = deserializer.descriptor.isNullable

        return if (isNullabilitySupported || decodeNotNullMark()) decodeSerializableValue(deserializer) else decodeNull()
    }
}
