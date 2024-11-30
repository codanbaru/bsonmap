package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.BsonType
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal class BsonmapPolymorphicMapCompositeEncoder(
    private val property: String,

    private val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule,

    private val consumer: (Map<String, BsonValue>) -> Unit
): CompositeEncoder {
    private var typeAttribute: Pair<String, BsonValue>? = null
    private var mapAttribute: Map<String, BsonValue>? = null

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean = true

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder =
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        val elementName = descriptor.getElementName(index)
        if (elementName != "type") throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

        val discriminator = configuration.classDiscriminator.ifBlank { "type" }

        typeAttribute = discriminator to BsonString(value)
    }

    override fun <T> encodeSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T) {
        val elementName = descriptor.getElementName(index)
        if (elementName != "value") throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)

        val encoder = BsonmapEncoder(property, BsonType.DOCUMENT, configuration, serializersModule) {
            mapAttribute = when(it) {
                is BsonDocument -> it.mapValues { it.value }
                else -> throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
            }
        }
        serializer.serialize(encoder, value)
    }

    override fun <T : Any> encodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T?) {
        throw BsonmapSerializationException.PolymorphicInvalid.Type(property = property)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        val typeAttribute = this.typeAttribute
        val mapAttribute = this.mapAttribute

        if (typeAttribute == null) throw BsonmapSerializationException.PolymorphicInvalid.Uncompleted(property = property)
        if (mapAttribute == null) throw BsonmapSerializationException.PolymorphicInvalid.Uncompleted(property = property)

        if (mapAttribute[typeAttribute.first] != null && mapAttribute[typeAttribute.first] != typeAttribute.second) throw BsonmapSerializationException.PolymorphicInvalid.DiscriminatorCollision(
            property = property
        )

        val attributes = mapAttribute.toMutableMap()
        attributes[typeAttribute.first] = typeAttribute.second
        consumer(attributes.toMap())
    }
}
