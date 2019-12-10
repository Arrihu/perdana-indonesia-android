package app.perdana.indonesia.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder

/**
 * Created by ebysofyan on 12/10/19.
 */
class IntroImagesViewPagerAdapter : PagerAdapter() {

    private val images = mutableListOf<String>()

    fun setValues(newImages: MutableList<String>) {
        this.images.addAll(newImages)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = LayoutInflater.from(container.context).inflate(
            R.layout.intro_image_view, container,
            false
        )
        item.findViewById<ImageView>(R.id.intro_image_view)
            .loadWithGlidePlaceholder(images[position])
        container.addView(item)
        return item
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
    override fun getCount(): Int = images.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}