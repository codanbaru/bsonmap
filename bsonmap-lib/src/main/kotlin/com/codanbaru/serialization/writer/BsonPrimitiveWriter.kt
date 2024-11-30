package com.codanbaru.serialization.writer

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonType
import org.bson.BsonValue

internal class BsonPrimitiveWriter(
    val configuration: BsonmapConfiguration
): PrimitiveWriter {
    override fun writeBoolean(value: Boolean, type: BsonType): BsonValue =
        value.writeBoolean(type, configuration)
    override fun writeByte(value: Byte, type: BsonType): BsonValue =
        value.writeByte(type, configuration)
    override fun writeChar(value: Char, type: BsonType): BsonValue =
        value.writeChar(type, configuration)
    override fun writeShort(value: Short, type: BsonType): BsonValue =
        value.writeShort(type, configuration)
    override fun writeInt(value: Int, type: BsonType): BsonValue =
        value.writeInt(type, configuration)
    override fun writeLong(value: Long, type: BsonType): BsonValue =
        value.writeLong(type, configuration)
    override fun writeFloat(value: Float, type: BsonType): BsonValue =
        value.writeFloat(type, configuration)
    override fun writeDouble(value: Double, type: BsonType): BsonValue =
        value.writeDouble(type, configuration)
    override fun writeString(value: String, type: BsonType): BsonValue =
        value.writeString(type, configuration)
}
