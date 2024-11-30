package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import com.codanbaru.serialization.extension.bsonTypeAnnotation
import com.codanbaru.serialization.extension.subproperty
import com.codanbaru.serialization.handlePrimitiveException
import com.codanbaru.serialization.reader.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonNull
import org.bson.BsonType
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal abstract class BsonmapCompositeDecoder(
    open val property: String,

    open val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule
) : CompositeDecoder {
    private val reader: PrimitiveReader by lazy { BsonPrimitiveReader(configuration) }

    abstract fun <T> decodeElement(descriptor: SerialDescriptor, index: Int, builder: (List<Annotation>, SerialDescriptor, String, BsonValue) -> T): T
    abstract override fun decodeElementIndex(descriptor: SerialDescriptor): Int

    override fun decodeSequentially(): Boolean =
        false
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
        super.decodeCollectionSize(descriptor)

    final override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readBoolean(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.BOOLEAN) }
    }
    final override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readByte(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.INT32) }
    }
    final override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readChar(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.INT32) }
    }
    final override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readShort(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.INT32) }
    }
    final override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readInt(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.INT32) }
    }
    final override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readLong(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.INT64) }
    }
    final override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readFloat(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.DOUBLE) }
    }
    final override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readDouble(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.DOUBLE) }
    }
    final override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        return@decodeElement handlePrimitiveException(property.subproperty(elementName)) { reader.readString(element, type = elementAnnotations.bsonTypeAnnotation ?: BsonType.STRING) }
    }

    final override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        if (descriptor.elementsCount == 1) {
            val inlinedElementAnnotation = descriptor.getElementAnnotations(0)
            // val inlinedElementDescriptor = descriptor.getElementDescriptor(0)
            // val inlinedElementName = descriptor.getElementName(0)

            val bsonType = elementAnnotations.bsonTypeAnnotation ?: inlinedElementAnnotation.bsonTypeAnnotation

            return@decodeElement BsonmapDecoder(element, property.subproperty(elementName), bsonType, configuration, serializersModule)
        }

        throw BsonmapSerializationException.InlineInvalid(property)
    }

    final override fun <T> decodeSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?): T = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        val bsonType = elementAnnotations.bsonTypeAnnotation

        val decoder = BsonmapDecoder(element, property.subproperty(elementName), bsonType, configuration, serializersModule)
        return@decodeElement deserializer.deserialize(decoder)
    }

    final override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? = decodeElement(descriptor, index) { elementAnnotations, _, elementName, element ->
        when (element) {
            is BsonNull -> return@decodeElement null
            else -> {
                val bsonType = elementAnnotations.bsonTypeAnnotation

                val decoder = BsonmapDecoder(element, property.subproperty(elementName), bsonType, configuration, serializersModule)
                return@decodeElement deserializer.deserialize(decoder)
            }
        }
    }

    final override fun endStructure(descriptor: SerialDescriptor) {
        // DO NOTHING
    }
}
