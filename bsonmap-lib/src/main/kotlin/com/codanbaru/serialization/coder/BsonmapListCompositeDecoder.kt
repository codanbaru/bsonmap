package com.codanbaru.serialization.coder

import com.codanbaru.serialization.BsonmapConfiguration
import com.codanbaru.serialization.BsonmapSerializationException
import com.codanbaru.serialization.extension.subproperty
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule
import org.bson.BsonNull
import org.bson.BsonValue

@OptIn(ExperimentalSerializationApi::class)
internal class BsonmapListCompositeDecoder(
    private val `object`: List<BsonValue>,

    override val property: String,

    override val configuration: BsonmapConfiguration,
    override val serializersModule: SerializersModule
): BsonmapCompositeDecoder(property, configuration, serializersModule) {
    override fun <T> decodeElement(descriptor: SerialDescriptor, index: Int, builder: (List<Annotation>, SerialDescriptor, String, BsonValue) -> T): T {
        val elementAnnotations = annotationsAtIndex(descriptor, index)
        val elementDescriptor = descriptorAtIndex(descriptor, index)
        val elementName = propertyAtIndex(descriptor, index)
        val element = elementAtIndex(descriptor, index)

        return builder(elementAnnotations, elementDescriptor, elementName, element)
    }

    private var currentIndex = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (currentIndex >= `object`.size) return CompositeDecoder.DECODE_DONE

        // if (currentIndex >= descriptor.elementsCount) {
        //     return CompositeDecoder.UNKNOWN_NAME
        // }
        //
        // try { descriptor.getElementName(currentIndex) } catch (throwable: Throwable) { return CompositeDecoder.UNKNOWN_NAME }

        val currentIndex = this.currentIndex
        this.currentIndex += 1
        return currentIndex
    }

    private fun annotationsAtIndex(descriptor: SerialDescriptor, index: Int): List<Annotation> =
        descriptor.getElementAnnotations(index)

    private fun propertyAtIndex(descriptor: SerialDescriptor, index: Int): String =
        "$index"

    private fun elementAtIndex(descriptor: SerialDescriptor, index: Int): BsonValue {
        var element = `object`.getOrNull(index)
        if (element == null && configuration.evaluateUndefinedAttributesAsNullAttribute) {
            element = BsonNull()
        }

        return element ?: throw BsonmapSerializationException.UnexpectedUndefined(property.subproperty("$index"))
    }

    private fun descriptorAtIndex(descriptor: SerialDescriptor, index: Int): SerialDescriptor =
        descriptor.getElementDescriptor(index)
}
