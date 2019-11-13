package com.lmoumou.lib_widget.widget

import android.content.Context
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Scroller

/**
 * @author:Lmoumou
 * @date:2019/10/27
 * 描述:
 **/
class TemperatureScroller(
    private val context: Context,
    private val listener: ScrollingListener
) {
    companion object {
        //滚动时间
        const val SCROLLING_DURATION = 400
        //用于滚动的最小增量
        const val MIN_DELTA_FOR_SCROLLING = 1

        const val MESSAGE_SCROLL = 0
        const val MESSAGE_JUSTIFY = 1
    }

    //手势监听
    private val gestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                lastScrollX = 0
                scroller.fling(0, lastScrollX, (-velocityX).toInt(), 0, -0x7FFFFFFF, 0x7FFFFFFF, 0, 0)
                setNextMessage(MESSAGE_SCROLL)
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        }

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(context, gestureListener).apply {
            setIsLongpressEnabled(false)
        }
    }
    private val scroller: Scroller  by lazy {
        Scroller(context).apply {
            setFriction(0.05F)
        }
    }
    private var lastScrollX: Int = 0
    private var lastTouchedX: Float = 0F
    private var isScrollingPerformed: Boolean = false

    /**
     * 手势处理
     * */
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchedX = event.x
                scroller.forceFinished(true)
                clearMessages()
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX: Int = (event.x - lastTouchedX).toInt()
                if (distanceX != 0) {
                    startScrolling()
                    listener.onScroll(distanceX)
                    lastTouchedX = event.x
                }
            }
        }

        if (!gestureDetector.onTouchEvent(event) && event.action == MotionEvent.ACTION_UP) {
            justify()
        }
        return true
    }

    /**
     * 滚动
     * @param distance 距离
     * @param time     时间
     */
    fun scroll(distance: Int, time: Int) {
        scroller.forceFinished(true)
        lastScrollX = 0
        scroller.startScroll(0, 0, distance, 0, if (time != 0) time else SCROLLING_DURATION)
        setNextMessage(MESSAGE_SCROLL)
        startScrolling()
    }

    /**
     * 清楚消息之前，发送下一步消息
     * */
    private fun setNextMessage(message: Int) {
        clearMessages()
        animationHandler.sendEmptyMessage(message)
    }

    /**
     * 清楚所有消息
     * */
    private fun clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL)
        animationHandler.removeMessages(MESSAGE_JUSTIFY)
    }

    /**
     * 动画处理handler
     */
    private val animationHandler: Handler  by lazy {
        Handler(Handler.Callback { msg ->
            scroller.computeScrollOffset()
            val currX = scroller.currX
            val delta = lastScrollX - currX
            lastScrollX = currX
            if (delta != 0) {
                listener.onScroll(delta)
            }
            // 滚动是不是完成时，涉及到最终Y，所以手动完成
            if (Math.abs(currX - scroller.finalX) < MIN_DELTA_FOR_SCROLLING) {
                lastScrollX = scroller.finalX
                scroller.forceFinished(true)
            }
            if (!scroller.isFinished) {
                animationHandler.sendEmptyMessage(msg.what)
            } else if (msg.what == MESSAGE_SCROLL) {
                justify()
            } else {
                finishScrolling()
            }
            true
        })
    }

    /**
     * 滚动停止时待校验
     */
    private fun justify() {
        listener.onJustify()
        setNextMessage(MESSAGE_JUSTIFY)
    }

    /**
     * 开始滚动
     */
    private fun startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true
            listener.onStarted()
        }
    }

    /**
     * 滚动结束
     */
    private fun finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished()
            isScrollingPerformed = false
        }
    }

    interface ScrollingListener {
        /**
         * 滚动中回调
         * @param distance 滚动距离
         * */
        fun onScroll(distance: Int)

        /**
         * 启动滚动时回调
         * */
        fun onStarted()

        /**
         * 校验完成后回调
         * */
        fun onFinished()

        /**
         * 滚动停止时待校验
         * */
        fun onJustify()
    }
}