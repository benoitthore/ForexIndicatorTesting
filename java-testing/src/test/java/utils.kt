import org.junit.jupiter.api.Assertions

private fun assert(bool: Boolean) = Assertions.assertTrue(bool)
infix fun <T> T.shouldEqual(excepted: T) = Assertions.assertEquals(excepted, this)
fun List<*>.shouldBeEmpty() = assert(isEmpty())
fun List<*>.shouldNotBeEmpty() = assert(isNotEmpty())
fun <T> List<T>.shouldContain(vararg element: T) {
    element.forEach {
        assert(contains(it))
    }
}

fun <T> List<T>.shouldNotContain(vararg element: T) {
    element.forEach {
        assert(!contains(it))
    }
}