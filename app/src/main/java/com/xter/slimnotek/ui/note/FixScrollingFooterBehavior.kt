package com.xter.slimnotek.ui.note

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.xter.slimnotek.util.L

class FixScrollingFooterBehavior @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    AppBarLayout.ScrollingViewBehavior(context,attr) {

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        L.i("view changed:$child ,$dependency")
        val appBarLayout = dependency as AppBarLayout
        val result = super.onDependentViewChanged(parent, child, dependency)
        val bottomPadding: Int = calculateBottomPadding(appBarLayout)
        val paddingChanged = bottomPadding != child.paddingBottom
        if (paddingChanged) {
            child.setPadding(
                child.paddingLeft,
                child.paddingTop,
                child.paddingRight,
                bottomPadding
            )
            child.requestLayout()
        }
        return paddingChanged || result
    }

    private fun calculateBottomPadding(dependency: AppBarLayout): Int {
        val totalScrollRange = dependency.totalScrollRange
        return totalScrollRange + dependency.top
    }
}