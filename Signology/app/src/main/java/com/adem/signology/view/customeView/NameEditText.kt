package com.adem.signology.view.customeView

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.adem.signology.R

class NameEditText: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        setBackgroundWithRadius()
    }

    private fun setBackgroundWithRadius() {
        val radius = 50F
        val background: Drawable = GradientDrawable().apply {
            cornerRadius = radius
            setColor(resources.getColor(R.color.blue_light))
        }
        background.setBounds(0, 0, width, height)
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        setBackground(background)
    }
}