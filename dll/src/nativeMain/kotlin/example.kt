package example

@CName(externName = "example_create_thing", shortName = "create_thing")
fun createThing(): Int = 0


//@Serializable
//data class Message(val msg: String)

//class Thing() {
//    fun encode(buffer: CArrayPointer<UByteVar>, bufferSize: Int): Int {
//        val msg = Message(
//            "x".repeat(bufferSize - 20)
//        )
//
//        val json = Json(JsonConfiguration.Stable);
//        val data = json.stringify(Message.serializer(), msg);
//
//        val dataArray = data.encodeToByteArray().asUByteArray();
//
//        if (dataArray.size > bufferSize) {
//            return -1
//        }
//
//        for (i in dataArray.indices){
//            buffer[i] = dataArray[i]
//        }
//
//        return bufferSize
//    }
//
//    fun decode(buffer: CArrayPointer<UByteVar>, bufferSize: Int): Int {
//        val bufferCopy = UByteArray(bufferSize)
//
//        for (i in 0.until(bufferSize)) {
//            //println(i)
//            bufferCopy[i] = buffer[i]
//        }
//
//        return bufferSize
//    }
//}

//@CName(externName = "example_create_thing", shortName = "create_thing")
//fun createThing(): COpaquePointer {
//    val t = Thing();
//    return StableRef.create(t).asCPointer()
//}
//
//@CName(externName = "example_destroy_thing", shortName = "destroy_thing")
//fun destroyThing(thing: COpaquePointer) {
//    thing.asStableRef<Thing>().dispose()
//}
//
//@CName(externName = "example_encode", shortName = "encode")
//fun doThing(thing: COpaquePointer, buffer: CArrayPointer<UByteVar>, bufferSize: Int): Int{
//    val t = thing.asStableRef<Thing>().get()
//    return t.encode(buffer, bufferSize)
//}
//
//@CName(externName = "example_decode", shortName = "decode")
//fun doSomethingElse(thing: COpaquePointer, buffer: CArrayPointer<UByteVar>, bufferSize: Int): Int{
//    val t = thing.asStableRef<Thing>().get()
//    return t.decode(buffer, bufferSize)
//}
//
