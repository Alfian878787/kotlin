// IGNORE_BACKEND_FIR: JVM_IR
// WITH_RUNTIME
// WITH_COROUTINES
// COMMON_COROUTINES_TEST
// IGNORE_BACKEND_FIR: JVM_IR

import helpers.*
import COROUTINES_PACKAGE.*
import COROUTINES_PACKAGE.intrinsics.*
import kotlin.test.*

fun testLaziness() {
    var sharedVar = -2
    val result = sequence {
        while (true) {
            when (sharedVar) {
                -1 -> return@sequence
                -2 -> error("Invalid state: -2")
                else -> yield(sharedVar)
            }
        }
    }

    val iterator = result.iterator()

    sharedVar = 1
    assertTrue(iterator.hasNext())
    assertEquals(1, iterator.next())

    sharedVar = 2
    assertTrue(iterator.hasNext())
    assertEquals(2, iterator.next())

    sharedVar = 3
    assertTrue(iterator.hasNext())
    assertEquals(3, iterator.next())

    sharedVar = -1
    assertFalse(iterator.hasNext())
    assertFailsWith<NoSuchElementException> { iterator.next() }
}

fun box(): String {
    testLaziness()
    return "OK"
}