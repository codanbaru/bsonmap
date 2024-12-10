# `kotlinx.serialization` :heart: `BSON` & `MongoDB`

BSON format for [kotlinx.serialization](https://github.com/kotlin/kotlinx.serialization). Serialize and deserialize documents from BSON using `kotlinx.serialization`. This library provides a straightforward and efficient way to convert BsonDocument objects into domain-specific data types in Kotlin. Whether you’re building a server-side application, integrating with MongoDB, or handling BSON data structures, this library makes the transformation process seamless and type-safe.


## Introduction

According with Wikipedia, [BSON](https://en.wikipedia.org/wiki/BSON) is a computer data interchange format. It is a binary form for representing simple or complex data structures including associative arrays, integer indexed arrays, and a suite of fundamental scalar types.

If you are using the native MongoDB driver to interact with MongoDB databases, you are likely working with BSON values. 

Although the native [MongoDB driver for Kotlin](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/) already provides support for [serializing and deserializing](https://www.mongodb.com/docs/drivers/kotlin/coroutine/current/fundamentals/data-formats/serialization/) data objects to and from BSON, there are situations where you may need to work directly with BsonDocuments before converting them into native Kotlin entities. This library is designed specifically to address such use cases.

## Setup

To utilize the BSONMap library, you will need to perform serialization and deserialization in a manner similar to working with kotlinx.serialization.

1. Install `kotlinx.serialization` plugin.
2. Add `BSONMap` serialization dependency.

> Kotlin DSL:

```kotlin
plugins {
    kotlin("jvm")
    
    // ADD SERIALIZATION PLUGIN
    kotlin("plugin.serialization")
}

dependencies {
    // ADD SERIALIZATION DEPENDENCY
    implementation("com.codanbaru.kotlin:bsonmap:0.9.0")
}
```

<details>
    <summary>Groovy DSL</summary>

```gradle
plugins {
    // ADD SERIALIZATION PLUGIN
    id 'org.jetbrains.kotlin.plugin.serialization'
}    

dependencies {
    // ADD SERIALIZATION DEPENDENCY
    implementation 'com.codanbaru.kotlin:bsonmap:0.9.0'
}
```
</details>

## Simple Example

```kotlin
@Serializable
data class Book(val name: String, val author: String?)

val bsonmap = Bsonmap {
    evaluateUndefinedAttributesAsNullAttribute = false
}

fun encodeBook(book: Book): BsonDocument {
    val document: BsonDocument = bsonmap.encodeToDocument(book)
    
    return document
}

fun decodeBook(document: BsonDocument): Book {
    val obj: Book = bsonmap.decodeFromDocument(document)

    return obj
}
```

By default, the `BSONMap` library evaluates `undefined` attributes as `null` attributes. This means that if the document in the database does not contain an attribute for a nullable property, `BSONMap` will create that attribute automatically.

```kotlin
@Serializable
data class Book(val name: String, val author: String?)

val document: BsonDocument = BsonDocument()
    .append(
        "name",
        BsonString("Harry Potter")
    )

// Will deserialize object successfully.
val book1: Book = Bsonmap { 
    evaluateUndefinedAttributesAsNullAttribute = true
}.decodeFromDocument(document)

// Will raise BsonmapSerializationException.UnexpectedUndefined exception.
val book2: Book = Bsonmap {
    evaluateUndefinedAttributesAsNullAttribute = false
}.decodeFromDocument(document)
```


## Property Mapping

`BSONMap` can override the name used in encoded document using `@SerialName` annotation.

```kotlin
@Serializable
data class Book(
    @SerialName("BookName")
    val name: String,

    val author: String?,
    val price: Int
)

val book = Book("Harry Potter", "JKRowling", 10)

val document = Bsonmap.encodeToDocument(book)
println(document) // {"BookName": "Harry Potter", "author": "JKRowling", "price": 10}
```

## Binary Serialization / Deserialization

`kotlinx.serialization` handles `ByteArray` internally as a list of bytes. Consequently, when serializing or deserializing a `ByteArray` using the default serializer, it will be transformed into a list of BsonInt32 elements in BSON format.

```kotlin
@Serializable
data class User(val username: String, val passwordHash: ByteArray

val user = User(username = "Codanbaru", passwordHash = "AQIDBA==".decodeBase64Bytes())
val document: BsonDocument = Bsonmap.encodeToDocument(user

println(document) // {"username": "Codanbaru", "passwordHash": [1, 2, 3, 4]}
```

However, in BSON, it is strongly recommended to store binary data using BsonBinary elements. To facilitate this, the library provides a custom serializer called BsonMapBinarySerializer.

```kotlin
@Serializable
data class User(
    val username: String,

    @Serializable(with = BsonmapBinarySerializer::class)
    val passwordHash: ByteArray
)

val user = User(username = "Codanbaru", passwordHash = "AQIDBA==".decodeBase64Bytes())
val document: BsonDocument = Bsonmap.encodeToDocument(user

println(document) // {"username": "Codanbaru", "passwordHash": {"$binary": {"base64": "AQIDBA==", "subType": "00"}}}
```

## Polymorphism

`BSONMap` also supports serialization / deserialization of polyphormic structures<sup>1</sup>.

At the moment, `Bsonmap` currently support `Closed polymorphism` only.

- :white_check_mark: Closed polymorphism
- :x: Open polymorphism

<sup>1</sup> You can get more information about polymorphism on `kotlinx.serialization` documentation.

```kotlin
val bsonmap = Bsonmap { }

@Serializable
sealed class Social {
    @Serializable
    data class Email(
        var email: String
    ) : Social()

    @Serializable
    data class Phone(
        val country: String,
        val number: String
    ) : Social()

    @Serializable
    data class Instagram(
        val username: String
    ) : Social()
}

@Serializable
data class User(val name: String, val social: Social)

val user0 = User(name = "Codanbaru 0", social = Social.Email("demo@codanbaru.com"))
val user1 = User(name = "Codanbaru 0", social = Social.Instagram("@codanbaru"))

val document0: BsonDocument = bsonmap.encodeToDocument(user0)
val document1: BsonDocument = bsonmap.encodeToDocument(user1)

println(document0) // {"name": "Codanbaru 0", "social": {"email": "demo@codanbaru.com", "__bsonmap_serialization_type": "com.codanbaru.entity.Social.Email"}}
println(document1) // {"name": "Codanbaru 0", "social": {"username": "@codanbaru", "__bsonmap_serialization_type": "com.codanbaru.entity.Social.Instagram"}}
```

You can specify, at `Bsonmap` creation, which key `kotlinx.serialization` should use to discriminate classes.

```kotlin
val bsonmap = Bsonmap {
    classDiscriminator = "type"
}

// ...

println(document0) // {"name": "Codanbaru 0", "social": {"email": "demo@codanbaru.com", "type": "com.codanbaru.entity.Social.Email"}}
println(document1) // {"name": "Codanbaru 0", "social": {"username": "@codanbaru", "type": "com.codanbaru.entity.Social.Instagram"}}
```

Also, with `@SerialName` annotation, you can override the class name used by `kotlinx.serialization` and `Bsonmap`.

```kotlin
@Serializable
sealed class Social {
    @Serializable
    @SerialName("email")
    data class Email(
        var email: String
    ) : Social()

    @Serializable
    @SerialName("phone")
    data class Phone(
        val country: String,
        val number: String
    ) : Social()

    @Serializable
    @SerialName("instagram")
    data class Instagram(
        val username: String
    ) : Social()
}

// ...

println(document0) // {"name": "Codanbaru 0", "social": {"email": "demo@codanbaru.com", "__bsonmap_serialization_type": "email"}}
println(document1) // {"name": "Codanbaru 0", "social": {"username": "@codanbaru", "__bsonmap_serialization_type": "instagram"}}
```
