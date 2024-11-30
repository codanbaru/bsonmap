package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import com.codanbaru.serialization.extension.asStringOrNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonDocument
import org.bson.BsonElement
import org.bson.BsonType
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal class BsonmapPolymorphicMapCompositeDecoder(
    private val `object`: Map<String, BsonValue>,

    private val property: String,

    private val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule
): CompositeDecoder {
    override fun decodeSequentially(): Boolean = false
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = -1

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

    private var currentDecodeElementIndex = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (currentDecodeElementIndex >= descriptor.elementsCount) return CompositeDecoder.DECODE_DONE

        currentDecodeElementIndex += 1

        return currentDecodeElementIndex - 1
    }

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        val elementName = descriptor.getElementName(index)
        if (elementName != "type") throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

        val discriminator = configuration.classDiscriminator.ifBlank { "type" }
        val elementAttribute = `object`[discriminator] ?: throw BsonmapSerializationException.PolymorphicInvalid.DiscriminatorNotPresent(
            property = property
        )

        val value = elementAttribute.asStringOrNull() ?: throw BsonmapSerializationException.PolymorphicInvalid.DiscriminatorNotPresent(
            property = property
        )

        return value.value
    }

    override fun <T> decodeSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?): T {
        val elementName = descriptor.getElementName(index)
        if (elementName != "value") throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

        val element = BsonDocument(`object`.map { BsonElement(it.key, it.value) })
        val decoder = BsonmapDecoder(element, elementName, BsonType.DOCUMENT, configuration, serializersModule)
        return deserializer.deserialize(decoder)
    }

    override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? {
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    }

    override fun endStructure(descriptor: SerialDescriptor) {

    }
}
