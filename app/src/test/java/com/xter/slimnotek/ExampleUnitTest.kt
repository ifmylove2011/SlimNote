package com.xter.slimnotek


import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMap(){
        val str = ('a'..'z') + ('A'..'Z') +('0'..'9')
        println(str.shuffled().subList(0,4).joinToString(""))
        (0..10).map{
            println(it)
        }
    }

    @Test
    fun testScope() {
        val numbers = mutableListOf<Int>()
        val result = with(numbers) {
            this.also {
                for (i in 1..4) {
                    it.add(i)
                }
            }
        }
            .let {
                var sum: Int = 0
                for (i in it) {
                    sum += i
                }
                sum.run {
                    val mod = sum % 3
                    mod.apply {
                        println(mod % 2 == 0)
                    }
                }
            }
        println(result)
    }

    @Test
    fun testLet() {
        var s: String? = "abc"
        s = if (System.currentTimeMillis().rem(2) == 0L) null else s
        s?.let { key ->
            println(key.length)
        }
    }

    @Test
    fun testWith() {
        var s: String = "abc"
        with(s) {
            for (i in s) {
                println(i)
            }
        }
    }

    @Test
    fun testRun() {
        val bytes: IntArray = IntArray(3)
        val s = bytes.run {
            bytes[0] = 11
            bytes[1] = 12
            bytes[2] = 13
            bytes[0] + bytes[1] + bytes[2]
        }
        println(s)
    }

    @Test
    fun testApply() {
        val s = IntArray(3).apply {
            this[0] = 11
            this[1] = 12
            this[2] = 13
        }
        println(s.last())
    }

    @Test
    fun testAlso() {
        var s: String = "abc"
        s.also {
            println(it.length)
        }
            .plus("d")
            .also {
                println(it.length)
            }
        print(s)
    }

    @Test
    fun testTakeIf(){
        System.currentTimeMillis().takeIf { it%2==0L }?.let {
            println("1 is $it")
        }
        System.currentTimeMillis().takeUnless { it%2==0L }?.let {
            println("2 is $it")
        }
    }
}
