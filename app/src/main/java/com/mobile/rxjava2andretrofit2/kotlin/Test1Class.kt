package com.mobile.rxjava2andretrofit2.kotlin

import com.mobile.common_library.manager.LogManager
import java.lang.Integer.parseInt

open class Test1Class {

    val TAG: String = "Test1Class";
    val a: Int = 1  // 立即赋值
    open val b = 2   // 自动推断出 `Int` 类型
    open var c: Int = 3  // 如果没有初始值类型不能省略


    // 模板中的简单名称：
    val s1 = "a is $a"
    // 模板中的任意表达式：
    val s2 = "${s1.replace("is", "was")}, but now is $a"

    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    fun sum2(a: Int, b: Int) = a + b

    fun printSum(a: Int, b: Int): Unit {
        println("sum of $a and $b is ${a + b}")
    }

    fun maxOf(a: Int, b: Int): Int {
        if (a > b) {
            return a
        } else {
            return b
        }
    }

    fun maxOf2(a: Int, b: Int) = if (a > b) a else b

    fun parseInt(str: String): Int? {
        // ……
        try {
            return str.toInt()
        } catch (e: Exception) {
            LogManager.i(TAG, "e*****$e")
            return null
        }
    }

    fun loop() {
        println("loop********************")
        val items = listOf("apple", "banana", "kiwifruit")
        for (item in items) {
            println(item)
        }
        for (index in items.indices) {
            println("item at $index is ${items[index]}")
        }
        var index = 0
        while (index < items.size) {
            println("item at $index is ${items[index]}")
            index++
        }
    }

    fun range() {
        println("range********************")
        val x = 10
        val y = 9
        if (x in 1..y + 1) {
            println("fits in range")
        }

        val list = listOf("a", "b", "c")

        if (-1 !in 0..list.lastIndex) {
            println("-1 is out of range")
        }
        if (list.size !in list.indices) {
            println("list size is out of valid list indices range, too")
        }
    }

    fun list() {
        val list = listOf("a", "b", "c")
        val map = mapOf("a" to 1, "b" to 2, "c" to 3)
    }
}


fun main() {
    val TAG: String = "Test1Class";
    println("Hello world!")
    var test1Class: Test1Class = Test1Class();
    println(test1Class.s2)

    test1Class.loop()
    test1Class.range()
    test1Class.c

    val num: Int? = parseInt("10a")
    LogManager.i(TAG, "num*****$num")

//    println("请输入第一个整数...")
//    var inputNumber1 = readLine()
//
//    println("请输入第二个整数...")
//    var inputNumber2 = readLine()
//
//    /**
//     * 转换的代码 由于是用户输入的信息 可能是字符串 有可能是整形 所以需要加入异常捕获处理
//     */
//    try {
//        /**
//         * 把输入的两个整数转 Int
//         * inputNumber1 !! .toInt()  !!代表此inputNumber1一定不为空，一定是有值的
//         */
//        var number1: Int = inputNumber1!!.toInt()
//        var number2: Int = inputNumber2!!.toInt()
//
//        println("$number1 相加 $number2 的结果是:${number1 + number2}")
//
//    } catch (e: Exception) {
//        println("请输入整形 整数，否则无法运输")
//        println("inputNumber1*****$inputNumber1")
//        println("inputNumber2*****$inputNumber2")
//    }
}
