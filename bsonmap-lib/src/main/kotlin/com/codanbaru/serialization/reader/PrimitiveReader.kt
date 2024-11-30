package com.codanbaru.serialization.reader

import org.bson.BsonType
import org.bson.BsonValue

internal interface PrimitiveReader {
    fun readBoolean(value: BsonValue, type: BsonType): Boolean
    fun readByte(value: BsonValue, type: BsonType): Byte
    fun readChar(value: BsonValue, type: BsonType): Char
    fun readShort(value: BsonValue, type: BsonType): Short
    fun readInt(value: BsonValue, type: BsonType): Int
    fun readLong(value: BsonValue, type: BsonType): Long
    fun readFloat(value: BsonValue, type: BsonType): Float
    fun readDouble(value: BsonValue, type: BsonType): Double
    fun readString(value: BsonValue, type: BsonType): String
}
