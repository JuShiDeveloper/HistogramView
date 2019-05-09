package com.test.view.jushi

import android.graphics.Color
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
//        FlowLayout.removeAllViews()
//        for (i in 0..6) {
//            val textView = TextView(this)
//            FlowLayout.addView(textView)
//            textView.text = "哈哈哈哈哈哈哈"
//            textView.textSize = 25f
//
//        }
        val rects = ArrayList<Rect>()
        val rect1 = Rect()
        rect1.set(100, 100, 200, 200)

        val rect2 = Rect()
        rect2.set(350, 350, 500, 500)

        val rect3 = Rect()
        rect3.set(550, 550, 700, 700)

        val rect4 = Rect()
        rect4.set(200, 700, 400, 800)

        rects.add(rect1)
        rects.add(rect2)
        rects.add(rect3)
        rects.add(rect4)
        CircleView.drawCircle(rects)

        val datas = arrayListOf<Float>()

        datas.add(0.2f)
        datas.add(0.4f)
        datas.add(0.6f)
        datas.add(0.8f)
        datas.add(0.5f)
        datas.add(0.9f)
        datas.add(0.7f)

        btn.setOnClickListener {
            HistogramView.setPercentage(datas)
        }

        val datas1 = arrayListOf<Float>()
        datas1.add(0.2f)
        datas1.add(0.4f)
        datas1.add(0.7f)
        datas1.add(0.5f)
        datas1.add(0.3f)
        datas1.add(0.8f)
        datas1.add(0.9f)
        //ffb22b
        //6ea1ff
        btn1.setOnClickListener {
            HistogramView.setPercentage(datas1)
        }
    }
}
