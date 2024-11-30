package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import com.codanbaru.serialization.extension.bsonTypeAnnotation
import com.codanbaru.serialization.handlePrimitiveException
import com.codanbaru.serialization.writer.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonArray
import org.bson.BsonBinary
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonNull
import org.bson.BsonType
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal class BsonmapEncoder(
    private val property: String,

    private val desiredType: BsonType?,

    private val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule,

    private val consumer: (BsonValue) -> Unit
) : Encoder {
    private val writer: PrimitiveWriter = BsonPrimitiveWriter(configuration)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
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

    private fun beginStructureOnListDescriptorKind(): CompositeEncoder {
        val bsonType = desiredType ?: BsonType.ARRAY
        
        return when (bsonType) {
            BsonType.ARRAY -> {
                BsonmapListCompositeEncoder(property, configuration, serializersModule) { attributeValues ->
                    consumer(BsonArray(attributeValues)) /* CHECK: Should raise exception in consumer is already called? */
                }
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = "<STRUCTURE>",
                supportedTypes = listOf(BsonType.ARRAY)
            )
        }
    }

    private fun beginStructureOnMapDescriptorKind(): CompositeEncoder {
        val bsonType = desiredType ?: BsonType.DOCUMENT

        return when(bsonType) {
            BsonType.DOCUMENT -> {
                BsonmapMapCompositeEncoder(property, configuration, serializersModule) {
                    consumer(BsonDocument(it.map { entry -> BsonElement(entry.key, entry.value) })) /* CHECK: Should raise exception in consumer is already called? */
                }
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = "<STRUCTURE>",
                supportedTypes = listOf(BsonType.DOCUMENT)
            )
        }
    }

    private fun beginStructureOnPolymorphicDescriptorKind(): CompositeEncoder {
        val bsonType = desiredType ?: BsonType.DOCUMENT

        return when (bsonType) {
            BsonType.DOCUMENT -> {
                BsonmapPolymorphicMapCompositeEncoder(property, configuration, serializersModule) {
                    consumer(BsonDocument(it.map { entry -> BsonElement(entry.key, entry.value) })) /* CHECK: Should raise exception in consumer is already called? */
                }
            }

            else -> throw BsonmapSerializationException.UnsupportedType(
                property = property,
                type = bsonType,
                value = "<STRUCTURE>",
                supportedTypes = listOf(BsonType.DOCUMENT)
            )
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return super.beginCollection(descriptor, collectionSize)
    }

    override fun encodeBoolean(value: Boolean) = handlePrimitiveException(property) { consumer(writer.writeBoolean(value, type = desiredType ?: BsonType.BOOLEAN)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeByte(value: Byte) = handlePrimitiveException(property) { consumer(writer.writeByte(value, type = desiredType ?: BsonType.INT32)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeChar(value: Char) = handlePrimitiveException(property) { consumer(writer.writeChar(value, type = desiredType ?: BsonType.INT32)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeShort(value: Short) = handlePrimitiveException(property) { consumer(writer.writeShort(value, type = desiredType ?: BsonType.INT32)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeInt(value: Int) = handlePrimitiveException(property) { consumer(writer.writeInt(value, type = desiredType ?: BsonType.INT32)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeLong(value: Long) = handlePrimitiveException(property) { consumer(writer.writeLong(value, type = desiredType ?: BsonType.INT64)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeFloat(value: Float) = handlePrimitiveException(property) { consumer(writer.writeFloat(value, type = desiredType ?: BsonType.DOUBLE)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeDouble(value: Double) = handlePrimitiveException(property) { consumer(writer.writeDouble(value, type = desiredType ?: BsonType.DOUBLE)) } /* CHECK: Should raise exception in consumer is already called? */
    override fun encodeString(value: String) = handlePrimitiveException(property) { consumer(writer.writeString(value, type = desiredType ?: BsonType.STRING)) } /* CHECK: Should raise exception in consumer is already called? */

    fun encodeBinary(value: ByteArray) {
        consumer(BsonBinary(value))
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        val element = enumDescriptor.getElementName(index)

        consumer(handlePrimitiveException(property) { writer.writeString(element, type = desiredType ?: BsonType.STRING) })
    }

    override fun encodeNull() {
        consumer(BsonNull()) /* CHECK: Should raise exception in consumer is already called? */
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        if (descriptor.elementsCount == 1) {
            val elementAnnotation = descriptor.getElementAnnotations(0)
            // val elementDescriptor = descriptor.getElementDescriptor(0)
            // val elementName = descriptor.getElementName(0)

            val bsonType = desiredType ?: elementAnnotation.bsonTypeAnnotation

            return BsonmapEncoder(property, bsonType, /* annotations + elementAnnotation, elementDescriptor, */ configuration, serializersModule) { consumer(it) }  /* CHECK: Should raise exception in consumer is already called? */
        }

        throw BsonmapSerializationException.InlineInvalid(property)
    }

    override fun encodeNotNullMark() {
        // DO NOTHING
    }

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        serializer.serialize(this, value)
    }

    override fun <T : Any> encodeNullableSerializableValue(serializer: SerializationStrategy<T>, value: T?) {
        val isNullabilitySupported = serializer.descriptor.isNullable
        if (isNullabilitySupported) {
            // Instead of `serializer.serialize` to be able to intercept this
            return encodeSerializableValue(serializer as SerializationStrategy<T?>, value)
        }

        // Else default path used to avoid allocation of NullableSerializer
        if (value == null) {
            encodeNull()
        } else {
            encodeNotNullMark()
            encodeSerializableValue(serializer, value)
        }
    }
}
