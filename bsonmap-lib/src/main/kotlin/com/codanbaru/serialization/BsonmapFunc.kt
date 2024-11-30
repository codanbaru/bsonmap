package com.codanbaru.serialization

public fun Bsonmap(from: Bsonmap = Bsonmap.Default, builderAction: BsonmapBuilder.() -> Unit): Bsonmap {
    val builder = BsonmapBuilder(from)
    builder.builderAction()
    val conf = builder.build()
    return Bsonmap.Impl(conf, builder.serializersModule)
}
