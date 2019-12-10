package app.perdana.indonesia.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import app.perdana.indonesia.R

/**
 * Created by @ebysofyan on 6/13/19
 */
class CenteredTextToolbar : Toolbar {

    private var _titleTextView: TextView? = null
    private var _screenWidth: Int = 0
    private var _centerTitle = true

    private val screenSize: Point
        get() {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screenSize = Point()
            display.getSize(screenSize)

            return screenSize
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        _screenWidth = screenSize.x

        _titleTextView = TextView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _titleTextView?.setTextAppearance(R.style.ToolbarTitleText)
        } else {
            _titleTextView?.setTextAppearance(context, R.style.ToolbarTitleText)
        }
        _titleTextView?.text = "Toolbar Title"
        addView(_titleTextView)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (_centerTitle) {
            val location = IntArray(2)
            _titleTextView!!.getLocationOnScreen(location)
            _titleTextView!!.translationX =
                _titleTextView!!.translationX + (-location[0] + _screenWidth / 2 - _titleTextView!!.width / 2)
        }
    }

    override fun setTitle(title: CharSequence) {
        _titleTextView!!.text = title
        requestLayout()
    }

    override fun setTitle(titleRes: Int) {
        _titleTextView!!.setText(titleRes)
        requestLayout()
    }

    fun setTitleCentered(centered: Boolean) {
        _centerTitle = centered
        requestLayout()
    }
}