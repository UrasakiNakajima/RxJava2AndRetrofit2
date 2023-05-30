package com.phone.module_square.ui

import com.phone.library_common.base.BaseRxAppActivity
import com.phone.module_square.R
import kotlinx.coroutines.*

class KotlinCoroutineActivity : BaseRxAppActivity() {

    companion object {
        private val TAG = KotlinCoroutineActivity::class.java.simpleName
    }

    var number: Int = 10 //还有Double Float Long Short Byte
    var char: Char = 'c'
    var isFood: Boolean = true
    var ages: IntArray =
        intArrayOf(1, 2, 3) //还有FloatArray DoubleArray CharArray，都属于kotlin 的 built-in函数
    var str: String = "Lili"

    override fun initLayoutId() = R.layout.square_activity_kotlin_coroutine

    override fun initData() {

    }

    override fun initViews() {

    }

    override fun initLoadData() {
//        test()
//        test2()

//        coroutine()
    }

    fun coroutine() {


        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 无阻塞的等待1秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 主线程的协程将会继续等待
        Thread.sleep(2000L) // 阻塞主线程2秒钟来保证 JVM 存活


        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个函数阻塞了主线程
            delay(2000L)  // ……我们延迟2秒来保证 JVM 的存活
        }



        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个函数阻塞了主线程
            delay(2000L)  // ……我们延迟2秒来保证 JVM 的存活
        }

    }


    fun main() = runBlocking<Unit> { // 开始执行主协程
        GlobalScope.launch { // 在后台开启一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟2秒来保证 JVM 存活
    }

    fun test() {
        val list = listOf(1, 2, 3, 4, 5, 6) // 是个内联函数

        //最大最小、加减、总和
        list.any { it > 4 } // 只要有符合条件的，就返回true
        list.all { it > 3 } // 集合的全部元素都符合条件，才返回true
        list.none { it < 1 } // 集合的全部元素都不符合条件，才返回true
        list.count { it > 3 } // 返回集合中符合条件的总数
        list.sum() // 请求总和，只有Int类型的list可使用
        list.sumOf { // 每个元素都做一定操作后的值相加获得所有的元素总和
            it + 1
        }
        list.maxOrNull() // 返回集合中最大的元素，可为null，元素继承Comparable类型的list可使用
        list.minOrNull() // 返回集合中最小的元素，可为null，元素继承Comparable类型的list可使用


        //遍历
        list.forEach { // 遍历每一个元素，没有返回值
            print(it)
        }
        list.onEach { // 遍历每一个元素，返回集合本身
            print(it)
        }.size // 可以引用到数量
        list.forEachIndexed { index, value -> // 遍历每一个元素，包含下标和值
            print("下标 $index 值 $value")
        }
        //过滤
        list.drop(3) // 从0开始，去除前3个元素，返回剩余的元素列表
        list.dropWhile { it > 3 } //从0开始，去除符合条件的前面的元素(该元素不去除)，返回剩余的元素列表
        list.dropLastWhile { it > 3 } // 同上，只不过从最后开始去除
        list.filter { it > 3 } //返回所有符合条件的元素的列表
        list.filterNot { it > 3 } //返回所有不符合条件的元素的列表
        list.slice(2..5) //切片，返回2到5的元素组成的列表
        list.take(3) // 返回前3个元素组成列表
        list.takeLast(3) // 返回最后的3个元素组成的列表
        list.takeWhile { it > 3 } //从0开始，返回符合条件的前面的元素(该元素不返回)组成的集合，和drop相反
        list.takeLastWhile { it > 3 }// 同上，只不过从最后开始取


        val students: List<Student> = listOf()
        val mapResult = students.map {
            "你好" + it.name
        }
        println("mapResult $mapResult")
        val flatmapResult: List<String> = list.flatMap {
            listOf("数字是" + 1)
        }
        println("flatmapResult $flatmapResult")
    }

    fun test2() {
        val list = listOf(
            User("Jack", 18, listOf("吃饭", "睡觉")),
            User("Tom", 18, listOf("跑步", "游泳")),
            User("Lili", 18, listOf("篮球", "跳远"))
        )
        //map:将List中每个元素转换成新的元素,并添加到一个新的List中,最后将新List返回
        val mapResult = list.map {
            it.name
        }
        //flatMap:返回指定list的所有元素，组成的一个list (表达式中必须返回list)
        val flatMapResult = list.flatMap {
            listOf(it.name)  // name不是list，需要包装成list
        }
        println("mapResult = $mapResult")
        println("flatMapResult = $flatMapResult")


        //map:将List中每个元素转换成新的元素,并添加到一个新的List中,最后将新List返回
        val mapResult2 = list.map {
            it.ability
        }
        //flatMap:返回指定list的所有元素，组成的一个list (表达式中必修返回list)
        val flatMapResult2 = list.flatMap {
            it.ability  //本身是list不用再包装
        }
        println("mapResult2 = $mapResult2")
        println("flatMapResult2 = $flatMapResult2")


        val user = User("Jack", 18, listOf("吃饭", "喝水"))
        val result = with(user) { //最后一行为返回值
            "年龄是 $age"
        }
        val result2 = user.run { //最后一行为返回值
            "年龄是 $age"
        }
        println("result = $result")
        println("result2 = $result2")
        val result3 = user.apply { //返回该对象本身
            age = 999
        }
        println("result3 = $result3")


    }

    data class User(
        val name: String = "Jack",
        var age: Int = 28,
        val ability: List<String> = listOf()
    )

    data class Student(
        val name: String = "",
        val age: Int = 0,
    )


}