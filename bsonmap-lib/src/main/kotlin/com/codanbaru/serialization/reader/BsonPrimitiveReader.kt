package com.codanbaru.serialization.reader

import com.codanbaru.serialization.BsonmapConfiguration
import org.bson.BsonType
import org.bson.BsonValue

internal class BsonPrimitiveReader(
    val configuration: BsonmapConfiguration
): PrimitiveReader {
    override fun readBoolean(value: BsonValue, type: BsonType): Boolean =
        value.readBoolean(type, configuration)
    override fun readByte(value: BsonValue, type: BsonType): Byte =
        value.readByte(type, configuration)
    override fun readChar(value: BsonValue, type: BsonType): Char =
        value.readChar(type, configuration)
    override fun readShort(value: BsonValue, type: BsonType): Short =
        value.readShort(type, configuration)
    override fun readInt(value: BsonValue, type: BsonType): Int =
        value.readInt(type, configuration)
    override fun readLong(value: BsonValue, type: BsonType): Long =
        value.readLong(type, configuration)
    override fun readFloat(value: BsonValue, type: BsonType): Float =
        value.readFloat(type, configuration)
    override fun readDouble(value: BsonValue, type: BsonType): Double =
        value.readDouble(type, configuration)
    override fun readString(value: BsonValue, type: BsonType): String =
        value.readString(type, configuration)
}
