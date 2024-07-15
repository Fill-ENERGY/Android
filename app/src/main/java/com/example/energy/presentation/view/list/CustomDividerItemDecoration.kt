package com.example.energy.presentation.view.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


class CustomDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {


    //private val divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.divider)
    private val divider: Drawable? = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).drawable

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        divider?.let {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.top - params.topMargin
                val bottom = top + it.intrinsicHeight
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        divider?.let {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = it.intrinsicHeight
            }
            outRect.bottom = it.intrinsicHeight
        }
    }
}