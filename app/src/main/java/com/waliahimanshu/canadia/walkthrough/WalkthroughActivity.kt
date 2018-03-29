package com.waliahimanshu.canadia.walkthrough

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import com.waliahimanshu.canadia.R

class WalkthroughActivity :  AppCompatActivity(), ViewPager.OnPageChangeListener{
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var viewPager: ViewPager? = null
    private var evaluator = ArgbEvaluator()
    private var page = 0
    private var indicators: Array<ImageView>? = null
    private var nextButton: ImageButton? = null
    private var previousButton: ImageButton? = null
    private var finishButton: ImageButton? = null
    private var bottomIndicatorNav: FrameLayout? = null
    private lateinit var colorList: IntArray
    private var color1: Int = 0
    private var color2: Int = 0
    private var color3: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        nextButton = findViewById(R.id.intro_btn_next)
        previousButton = findViewById(R.id.intro_btn_previous)
        finishButton = findViewById(R.id.intro_btn_finish)
        bottomIndicatorNav = findViewById(R.id.bottom_indicator_nav)

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById<View>(R.id.sections_view_pager) as ViewPager
        viewPager?.adapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager?.addOnPageChangeListener(this)

        initColors()
        initIndicators()
        nextButton?.setOnClickListener { viewPager?.setCurrentItem(viewPager?.currentItem!!.plus(1), true) }
        previousButton?.setOnClickListener { viewPager?.setCurrentItem(viewPager?.currentItem!!.minus(1), true) }
//        finishButton?.setOnClickListener { startActivity(EERoundOfInvitationsActivity.getLaunchIntent(this)) }


        // Hide the status bar.
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        window.decorView.systemUiVisibility = uiOptions
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val colorUpdate = evaluator.evaluate(
                positionOffset,
                colorList[position],
                colorList[if (position == 2) position else position + 1]) as Int
        viewPager?.setBackgroundColor(colorUpdate)
    }

    override fun onPageSelected(position: Int) {
        page = position
        updateIndicators(page)

        when (position) {
            0 -> viewPager?.setBackgroundColor(color1)
            1 -> viewPager?.setBackgroundColor(color2)
            2 -> viewPager?.setBackgroundColor(color3)
        }

        if (position == 2) nextButton?.visibility = View.GONE else nextButton?.visibility = View.VISIBLE
        if (position == 2) finishButton?.visibility = View.VISIBLE else finishButton?.visibility = View.GONE
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicators?.size as Int) {
            indicators?.get(i)?.setBackgroundResource(
                    if (i == position) R.drawable.active_indicator else R.drawable.inactive_indicator
            )
        }
    }

    private fun initColors() {
        color1 = ContextCompat.getColor(this, R.color.yellow_600)
        color2 = ContextCompat.getColor(this, R.color.red_300)
        color3 = ContextCompat.getColor(this, R.color.light_blue_300)
        colorList = intArrayOf(color1, color2, color3)
    }

    private fun initIndicators() {
        val indicator1 = findViewById<ImageView>(R.id.intro_indicator_0)
        val indicator2 = findViewById<ImageView>(R.id.intro_indicator_1)
        val indicator3 = findViewById<ImageView>(R.id.intro_indicator_2)

        indicators = arrayOf(indicator1, indicator2, indicator3)
    }
}