package com.yuchen.gridsurvay

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val context: Context,
    private val horizontalDividerInDp: Int = 0,
    private val horizontalDividerColor: Int? = null,
    private val verticalDividerInDp: Int = 0,
    private val verticalDividerColor: Int? = null,
    private val frameInDp: Int = 0,
    private val frameColor: Int? = null,
    private val borderRadiusInDp: Int = 0,
    private val titleBackGroundColor: Int? = null,
    private val gridBackGroundColorList: List<Int> = listOf(Color.LTGRAY, Color.GRAY),
) : RecyclerView.ItemDecoration() {
    private val path = Path()
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = false
    }

    private val horizontalDividerWidth = context.dpToPx(horizontalDividerInDp)
    private val horizontalDividerPaint = Paint().apply {
        color = horizontalDividerColor ?: Color.TRANSPARENT
        strokeWidth = horizontalDividerWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = false
    }

    private val verticalDividerWidth = context.dpToPx(verticalDividerInDp)
    private val verticalDividerPaint = Paint().apply {
        color = verticalDividerColor ?: Color.TRANSPARENT
        strokeWidth = verticalDividerWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = false
    }

    private val frameWidth = context.dpToPx(frameInDp)
    private val framePaint = Paint().apply {
        color = frameColor ?: Color.TRANSPARENT
        strokeWidth = frameWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = false
    }

    private val borderRadius = context.dpToPx(borderRadiusInDp)
    private val borderPaint = Paint().apply {
        strokeWidth = frameWidth.toFloat()
        isAntiAlias = true
    }

    private var frameX1: Float = 0f
    private var frameY1: Float = 0f
    private var frameX2: Float = 0f
    private var frameY2: Float = 0f
    private val frameDetectRect = Rect()

    private val horizontalDividerMap = mutableMapOf<Int, LineCoordinate>()
    private val verticalDividerMap = mutableMapOf<Int, LineCoordinate>()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val orientation = (parent.layoutManager as? GridLayoutManager)?.orientation ?: return
        val totalItemCount = parent.adapter?.itemCount ?: return
        val position: Int = parent.getChildAdapterPosition(view)
        val frameDetectRect = getGridFrameDetectRectByPosition(totalItemCount, position, spanCount, orientation)
        outRect.left = if (frameDetectRect.left == IS_FRAME) frameWidth else verticalDividerWidth / 2
        outRect.top = if (frameDetectRect.top == IS_FRAME) frameWidth else horizontalDividerWidth / 2
        outRect.right = if (frameDetectRect.right == IS_FRAME) frameWidth else verticalDividerWidth / 2
        outRect.bottom = if (frameDetectRect.bottom == IS_FRAME) frameWidth else horizontalDividerWidth / 2
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val orientation = (parent.layoutManager as? GridLayoutManager)?.orientation ?: return
        val totalItemCount = parent.adapter?.itemCount ?: return
        val onScreenCount: Int = parent.childCount

        frameDetectRect.set(0, 0, 0, 0)
        verticalDividerMap.clear()
        horizontalDividerMap.clear()
        val backLayer = c.saveLayer(0f, 0f, parent.width.toFloat(), parent.height.toFloat(), null)
        for (i in 0 until onScreenCount) {
            val view: View = parent.getChildAt(i)
            val position: Int = parent.getChildAdapterPosition(view)
            val currentRect = RectF(view.left.toFloat(), view.top.toFloat(), view.right.toFloat(), view.bottom.toFloat())
            // draw background
            val backgroundColor = getBackgroundCColor(position, spanCount)
            val gridFrameDetectRect = getGridFrameDetectRectByPosition(totalItemCount, position, spanCount, orientation)
            backgroundPaint.color = backgroundColor
            c.drawRect(currentRect, backgroundPaint)

            // GetFrame
            if (i == 0) {
                frameX1 = currentRect.left
                frameY1 = currentRect.top
                frameDetectRect.left = gridFrameDetectRect.left
                frameDetectRect.top = gridFrameDetectRect.top
            }
            if (i == onScreenCount - 1) {
                frameX2 = currentRect.right
                frameY2 = currentRect.bottom
                frameDetectRect.right = gridFrameDetectRect.right
                frameDetectRect.bottom = gridFrameDetectRect.bottom
            }

            var rowOnScreen = i % spanCount
            var columnOnScreen = i / spanCount
            if (orientation == RecyclerView.HORIZONTAL) {
                rowOnScreen = columnOnScreen.also { columnOnScreen = rowOnScreen }
            }

            // Get vertical divider
            if (rowOnScreen != 0) {
                if (verticalDividerMap.contains(rowOnScreen)) {
                    verticalDividerMap[rowOnScreen]?.endY = currentRect.bottom
                } else {
                    verticalDividerMap[rowOnScreen] = LineCoordinate(currentRect.left, currentRect.top, currentRect.left, currentRect.bottom)
                }
            }

            // Get horizontal divider
            if (columnOnScreen != 0) {
                if (horizontalDividerMap.contains(columnOnScreen)) {
                    horizontalDividerMap[columnOnScreen]?.endX = currentRect.right
                } else {
                    horizontalDividerMap[columnOnScreen] = LineCoordinate(currentRect.left, currentRect.top, currentRect.right, currentRect.top)
                }
            }
        }

        // Draw vertical divider
        if (verticalDividerWidth > 0 && verticalDividerColor != null) {
            val iterator = verticalDividerMap.iterator()
            while (iterator.hasNext()) {
                val currentLine = iterator.next().value
                c.drawLine(currentLine.startX - verticalDividerWidth / 2, currentLine.startY, currentLine.endX - verticalDividerWidth / 2, currentLine.endY, verticalDividerPaint)
            }
        }

        // Draw horizontal divider
        if (horizontalDividerWidth > 0 && horizontalDividerColor != null) {
            val iterator = horizontalDividerMap.iterator()
            while (iterator.hasNext()) {
                val currentLine = iterator.next().value
                c.drawLine(currentLine.startX, currentLine.startY - horizontalDividerWidth / 2, currentLine.endX, currentLine.endY - horizontalDividerWidth / 2, horizontalDividerPaint)
            }
        }

        // Draw frame
        if (frameWidth > 0 && frameColor != null) {
            if (frameDetectRect.left == 1) c.drawLine(frameX1 - frameWidth / 2, frameY1 - frameWidth, frameX1 - frameWidth / 2, frameY2 + frameWidth, framePaint)
            if (frameDetectRect.top == 1) c.drawLine(frameX1 - frameWidth, frameY1 - frameWidth / 2, frameX2 + frameWidth, frameY1 - frameWidth / 2, framePaint)
            if (frameDetectRect.right == 1) c.drawLine(frameX2 + frameWidth / 2, frameY1 - frameWidth, frameX2 + frameWidth / 2, frameY2 + frameWidth, framePaint)
            if (frameDetectRect.bottom == 1) c.drawLine(frameX1 - frameWidth, frameY2 + frameWidth / 2, frameX2 + frameWidth, frameY2 + frameWidth / 2, framePaint)
        }

        // Draw border
        if (borderRadius > 0 && frameWidth > 0 && frameColor != null) {
            if (frameDetectRect.left == 1 && frameDetectRect.top == 1) {
                path.reset()
                path.moveTo(frameX1 - frameWidth, frameY1 - frameWidth)
                path.rLineTo(0f, borderRadius.toFloat())
                val arcRect = RectF(frameX1 - frameWidth, frameY1 - frameWidth, frameX1 - frameWidth + borderRadius * 2, frameY1 - frameWidth + borderRadius * 2)
                path.arcTo(arcRect, 180f, 90f, false)
                borderPaint.color = Color.TRANSPARENT
                borderPaint.style = Paint.Style.FILL
                borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                c.drawPath(path, borderPaint)

                path.reset()
                arcRect.inset(frameWidth / 2f, frameWidth / 2f)
                path.arcTo(arcRect, 180f, 90f, true)
                borderPaint.color = frameColor
                borderPaint.style = Paint.Style.STROKE
                borderPaint.xfermode = null
                c.drawPath(path, borderPaint)
            }
            if (frameDetectRect.right == 1 && frameDetectRect.top == 1) {
                path.reset()
                path.moveTo(frameX2 + frameWidth, frameY1 - frameWidth)
                path.rLineTo(-borderRadius.toFloat(), 0f)
                val arcRect = RectF(frameX2 + frameWidth - 2 * borderRadius, frameY1 - frameWidth, frameX2 + frameWidth, frameY1 - frameWidth + borderRadius * 2)
                path.arcTo(arcRect, 270f, 90f, false)
                borderPaint.color = Color.TRANSPARENT
                borderPaint.style = Paint.Style.FILL
                borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                c.drawPath(path, borderPaint)

                path.reset()
                arcRect.inset(frameWidth / 2f, frameWidth / 2f)
                path.arcTo(arcRect, 270f, 90f, true)
                borderPaint.color = frameColor
                borderPaint.style = Paint.Style.STROKE
                borderPaint.xfermode = null
                c.drawPath(path, borderPaint)
            }
            if (frameDetectRect.left == 1 && frameDetectRect.bottom == 1) {
                path.reset()
                path.moveTo(frameX1 - frameWidth, frameY2 + frameWidth)
                path.rLineTo(borderRadius.toFloat(), 0f)
                val arcRect = RectF(frameX1 - frameWidth, frameY2 + frameWidth - 2 * borderRadius, frameX1 - frameWidth + borderRadius * 2, frameY2 + frameWidth)
                path.arcTo(arcRect, 90f, 90f, false)
                borderPaint.color = Color.TRANSPARENT
                borderPaint.style = Paint.Style.FILL
                borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                c.drawPath(path, borderPaint)

                path.reset()
                arcRect.inset(frameWidth / 2f, frameWidth / 2f)
                path.arcTo(arcRect, 90f, 90f, true)
                borderPaint.color = frameColor
                borderPaint.style = Paint.Style.STROKE
                borderPaint.xfermode = null
                c.drawPath(path, borderPaint)
            }
            if (frameDetectRect.right == 1 && frameDetectRect.bottom == 1) {
                path.reset()
                path.moveTo(frameX2 + frameWidth, frameY2 + frameWidth)
                path.rLineTo(0f, -borderRadius.toFloat())
                val arcRect = RectF(frameX2 + frameWidth - 2 * borderRadius, frameY2 + frameWidth - 2 * borderRadius, frameX2 + frameWidth, frameY2 + frameWidth)
                path.arcTo(arcRect, 0f, 90f, false)
                borderPaint.color = Color.TRANSPARENT
                borderPaint.style = Paint.Style.FILL
                borderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                c.drawPath(path, borderPaint)

                path.reset()
                arcRect.inset(frameWidth / 2f, frameWidth / 2f)
                path.arcTo(arcRect, 0f, 90f, true)
                borderPaint.color = frameColor
                borderPaint.style = Paint.Style.STROKE
                borderPaint.xfermode = null
                c.drawPath(path, borderPaint)
            }
        }
        c.restoreToCount(backLayer)
    }
    
    private fun getGridFrameDetectRectByPosition(totalItemCount: Int, position: Int, spanCount: Int, orientation: Int): Rect {
        var row = position / spanCount + 1
        var column = position % spanCount + 1
        var rowSize = totalItemCount / spanCount
        var columnSize = spanCount
        if (orientation == RecyclerView.HORIZONTAL) {
            row = column.also { column = row }
            rowSize = columnSize.also { columnSize = rowSize }
        }
        return getGridFrameDetectRect(row, column, rowSize, columnSize)
    }

    private fun getGridFrameDetectRect(row: Int, column: Int, rowSize: Int, columnSize: Int): Rect {
        val rect = Rect()
        if (row == 1) rect.top = IS_FRAME
        if (row == rowSize) rect.bottom = IS_FRAME
        if (column == 1) rect.left = IS_FRAME
        if (column == columnSize) rect.right = IS_FRAME
        return rect
    }

    private fun getBackgroundCColor(position: Int, spanCount: Int): Int {
        return try {
            val relatePosition = position / spanCount
            if (titleBackGroundColor != null && relatePosition == 0) {
                titleBackGroundColor
            } else {
                gridBackGroundColorList[relatePosition % gridBackGroundColorList.size]
            }
        } catch (e: Exception) {
            Color.TRANSPARENT
        }
    }

    data class LineCoordinate(
        var startX: Float = 0f,
        var startY: Float = 0f,
        var endX: Float = 0f,
        var endY: Float = 0f
    )

    companion object {
        const val IS_FRAME = 1
    }
}