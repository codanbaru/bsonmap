package com.codanbaru.serialization

import com.codanbaru.serialization.reader.PrimitiveReaderException
import com.codanbaru.serialization.writer.PrimitiveWriterException

internal fun <T> handlePrimitiveException(property: String, block: () -> T): T {
    try {
        return block()
    } catch (exception: Throwable) {
        if (exception is BsonmapSerializationException) throw exception
        if (exception is PrimitiveReaderException) {
            when (exception) {
                is PrimitiveReaderException.Generic -> throw BsonmapSerializationException.Exception(property, exception)
                is PrimitiveReaderException.UnexpectedType -> throw BsonmapSerializationException.UnexpectedType(property, exception.value, exception.type)
                is PrimitiveReaderException.UnsupportedType -> throw BsonmapSerializationException.UnsupportedType(property, exception.value, exception.type, exception.supportedTypes)
            }
        }
        if (exception is PrimitiveWriterException) {
            when (exception) {
                is PrimitiveWriterException.Generic -> throw BsonmapSerializationException.Exception(property, exception)
                is PrimitiveWriterException.UnsupportedType -> throw BsonmapSerializationException.UnsupportedType(property, exception.value, exception.type, exception.supportedTypes)
            }
        }

        throw BsonmapSerializationException.Exception(property, cause = exception)
    }
}
