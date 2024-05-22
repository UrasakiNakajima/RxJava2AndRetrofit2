package com.phone.module_square.function_menu.evaluator

import android.animation.TypeEvaluator
import android.graphics.PointF

/**
 * 贝塞尔曲线估值器：计算动画的执行轨迹
 *
 * @params 传入贝塞尔曲线需要的四个点
 * @return 通过计算返回贝塞尔曲线的坐标
 */
class BezierEvaluator(private val point1: PointF, private val point2: PointF) :
    TypeEvaluator<PointF> {

    override fun evaluate(t: Float, point0: PointF, point3: PointF): PointF {
        val point = PointF()
        //t 取值为 [0,1]
        /**
         * 三阶贝塞尔公式
         *
         * B(t)=(1 - t)^3 P0
         * + 3 t (1 - t)^2 P1
         * + 3 t^2 (1 - t) P2
         * + t^3 P3
         */
        point.x =
            point0.x * (1 - t) * (1 - t) * (1 - t) + 3 * point1.x * t * (1 - t) * (1 - t) + 3 * point2.x * t * t * (1 - t) + point3.x * t * t * t
        /**
         * 三阶贝塞尔公式
         *
         * B(t)=(1 - t)^3 P0
         * + 3 t (1 - t)^2 P1
         * + 3 t^2 (1 - t) P2
         * + t^3 P3
         */
        point.y =
            point0.y * (1 - t) * (1 - t) * (1 - t) + 3 * point1.y * t * (1 - t) * (1 - t) + 3 * point2.y * t * t * (1 - t) + point3.y * t * t * t
        return point
    }
}
