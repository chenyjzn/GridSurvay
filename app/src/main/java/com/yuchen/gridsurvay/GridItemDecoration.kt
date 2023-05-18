package com.yuchen.gridsurvay

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private val spanCount: Int, private val spacingInDp: Int) : RecyclerView.ItemDecoration() {
    private val paint = Paint()
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count: Int = parent.childCount
        for (i in 0 until count) {
            val view: View = parent.getChildAt(i)
            val position: Int = parent.getChildAdapterPosition(view)
            val currentRect = RectF(view.left.toFloat(), view.top.toFloat(), view.right.toFloat(), view.bottom.toFloat())
            if (position / spanCount == 0) {
                paint.color = Color.BLACK
            } else {
                if (position / spanCount % 2 == 0) {
                    paint.color = Color.LTGRAY
                } else {
                    paint.color = Color.GRAY
                }
            }
            c.drawRect(currentRect, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spacingInPx = parent.context.dpToPx(spacingInDp)
        val position = parent.getChildAdapterPosition(view)
        if (position / spanCount == 0) {
            outRect.top = spacingInPx
        }
        if (position % spanCount == 0) {
            outRect.left = spacingInPx
        }
        outRect.bottom = spacingInPx
        outRect.right = spacingInPx
    }
}