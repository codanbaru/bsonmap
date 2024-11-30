package com.codanbaru.serialization.writer

import org.bson.BsonType
import org.bson.BsonValue

internal interface PrimitiveWriter {
    fun writeBoolean(value: Boolean, type: BsonType): BsonValue
    fun writeByte(value: Byte, type: BsonType): BsonValue
    fun writeChar(value: Char, type: BsonType): BsonValue
    fun writeShort(value: Short, type: BsonType): BsonValue
    fun writeInt(value: Int, type: BsonType): BsonValue
    fun writeLong(value: Long, type: BsonType): BsonValue
    fun writeFloat(value: Float, type: BsonType): BsonValue
    fun writeDouble(value: Double, type: BsonType): BsonValue
    fun writeString(value: String, type: BsonType): BsonValue
}
