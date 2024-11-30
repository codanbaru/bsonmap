package com.codanbaru.serialization.extension

import com.codanbaru.serialization.annotation.SerialType
import org.bson.BsonType

internal val List<Annotation>.serialTypeAnnotation: SerialType?
    get() {
        return firstOrNull() { it is SerialType } as SerialType?
    }
internal val List<Annotation>.bsonTypeAnnotation: BsonType?
    get() {
        return serialTypeAnnotation?.type
    }
