package com.adem.signology.view.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.adem.signology.R
import com.adem.signology.data.model.OnBoardingData

class OnBoardingViewPagerAdapter(private var context: Context, private var onBoardingList: List<OnBoardingData>):
    PagerAdapter() {
    override fun getCount(): Int {
        return onBoardingList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout, null)

        val imageView: ImageView = view.findViewById(R.id.ivOnboarding)
        val desc: TextView = view.findViewById(R.id.tvDescription)

        imageView.setImageResource(onBoardingList[position].imageUrl)
        desc.text = onBoardingList[position].desc

        container.addView(view)
        return view
    }
}