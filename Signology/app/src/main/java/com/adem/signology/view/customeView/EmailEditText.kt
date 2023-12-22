package com.adem.signology.view.customeView

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.adem.signology.R

class EmailEditText: AppCompatEditText {
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

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s.toString())) {
                    setError(context.getString(R.string.error_email), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }

            private fun isValidEmail(email: String): Boolean {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                return email.matches(emailPattern.toRegex())
            }
        })
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