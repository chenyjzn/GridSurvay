package com.yuchen.gridsurvay

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class GridItemDecoration(
    private val context: Context,
    private val horizontalDividerInDp: Float = 0f,
    private val horizontalDividerColor: Int? = null,
    private val verticalDividerInDp: Float = 0f,
    private val verticalDividerColor: Int? = null,
    private val frameInDp: Float = 0f,
    private val frameColor: Int? = null,
    private val cornerRadiusInDp: Float = 0f,
    private val titleBackGroundColor: Int? = null,
    private val gridBackGroundColorList: List<Int>? = null,
) : RecyclerView.ItemDecoration() {
    private fun Context.floatDpToPx(dp: Float): Int = (dp * resources.displayMetrics.density).roundToInt()

    private val path = Path()
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = false
    }

    private val horizontalDividerWidth = context.floatDpToPx(horizontalDividerInDp)
    private val horizontalDividerPaint = Paint().apply {
        color = horizontalDividerColor ?: Color.TRANSPARENT
        strokeWidth = horizontalDividerWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = false
    }

    private val verticalDividerWidth = context.floatDpToPx(verticalDividerInDp)
    private val verticalDividerPaint = Paint().apply {
        color = verticalDividerColor ?: Color.TRANSPARENT
        strokeWidth = verticalDividerWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = false
    }

    private val frameWidth = context.floatDpToPx(frameInDp)
    private val framePaint = Paint().apply {
        color = frameColor ?: Color.TRANSPARENT
        strokeWidth = frameWidth.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val cornerRadius = context.floatDpToPx(cornerRadiusInDp)
    private val borderPaint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val frameRect = RectF()
    private val frameDetectRect = Rect()

    private val horizontalDividerMap = mutableMapOf<Int, RectF>()
    private val verticalDividerMap = mutableMapOf<Int, RectF>()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val orientation = (parent.layoutManager as? GridLayoutManager)?.orientation ?: return
        val totalItemCount = parent.adapter?.itemCount ?: return
        val position: Int = parent.getChildAdapterPosition(view)
        val gridParameter = getGridRowColumnParameter(totalItemCount, position, spanCount, orientation, parent.layoutDirection)
        val frameDetectRect = getGridFrameDetectRect(gridParameter.row(), gridParameter.column(), gridParameter.rowSize(), gridParameter.columnSize())
        outRect.left = if (frameDetectRect.left == IS_FRAME) frameWidth else verticalDividerWidth / 2
        outRect.top = if (frameDetectRect.top == IS_FRAME) frameWidth else horizontalDividerWidth / 2
        outRect.right = if (frameDetectRect.right == IS_FRAME) frameWidth else verticalDividerWidth / 2
        outRect.bottom = if (frameDetectRect.bottom == IS_FRAME) frameWidth else horizontalDividerWidth / 2
    }

    private fun getGridRowColumnParameter(totalItemCount: Int, position: Int, spanCount: Int, orientation: Int, layoutDirection: Int): List<Int> {
        var row = position / spanCount + 1
        var column = position % spanCount + 1
        var rowSize = totalItemCount / spanCount
        var columnSize = spanCount
        if (orientation == RecyclerView.HORIZONTAL) {
            row = column.also { column = row }
            rowSize = columnSize.also { columnSize = rowSize }
        }
        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            column = columnSize - column + 1
        }
        return listOf(row, column, rowSize, columnSize)
    }

    private fun List<Int>.row(): Int = this[0]
    private fun List<Int>.column(): Int = this[1]
    private fun List<Int>.rowSize(): Int = this[2]
    private fun List<Int>.columnSize(): Int = this[3]

    private fun getGridFrameDetectRect(row: Int, column: Int, rowSize: Int, columnSize: Int): Rect {
        val rect = Rect()
        if (row == 1) rect.top = IS_FRAME
        if (row == rowSize) rect.bottom = IS_FRAME
        if (column == 1) rect.left = IS_FRAME
        if (column == columnSize) rect.right = IS_FRAME
        return rect
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val orientation = (parent.layoutManager as? GridLayoutManager)?.orientation ?: return
        val totalItemCount = parent.adapter?.itemCount ?: return
        val onScreenCount: Int = parent.childCount

        frameDetectRect.set(NOT_FRAME, NOT_FRAME, NOT_FRAME, NOT_FRAME)
        verticalDividerMap.clear()
        horizontalDividerMap.clear()

        val layer = c.saveLayer(0f, 0f, parent.width.toFloat(), parent.bottom.toFloat(), null)

        for (i in 0 until onScreenCount) {
            val view: View = parent.getChildAt(i)
            val position: Int = parent.getChildAdapterPosition(view)
            val currentRect = RectF(view.left.toFloat(), view.top.toFloat(), view.right.toFloat(), view.bottom.toFloat())
            val gridParameter = getGridRowColumnParameter(totalItemCount, position, spanCount, orientation, parent.layoutDirection)
            val gridFrameDetectRect = getGridFrameDetectRect(gridParameter.row(), gridParameter.column(), gridParameter.rowSize(), gridParameter.columnSize())
            val gridOnScreenParameter = getGridRowColumnParameter(onScreenCount, i, spanCount, orientation, parent.layoutDirection)
            // draw background
            val backgroundColor = getBackgroundColor(position, spanCount)
            backgroundPaint.color = backgroundColor
            c.drawRect(currentRect, backgroundPaint)
            // GetFrame
            if (gridOnScreenParameter.row() == 1 && gridOnScreenParameter.column() == 1) {
                frameRect.left = currentRect.left
                frameRect.top = currentRect.top
                frameDetectRect.left = gridFrameDetectRect.left
                frameDetectRect.top = gridFrameDetectRect.top
            }
            if (gridOnScreenParameter.row() == gridOnScreenParameter.rowSize() && gridOnScreenParameter.column() == gridOnScreenParameter.columnSize()) {
                frameRect.right = currentRect.right
                frameRect.bottom = currentRect.bottom
                frameDetectRect.right = gridFrameDetectRect.right
                frameDetectRect.bottom = gridFrameDetectRect.bottom
            }

            // Get vertical divider
            if (gridFrameDetectRect.left != 1) {
                if (verticalDividerMap.contains(gridOnScreenParameter.column() - 1)) {
                    verticalDividerMap[gridOnScreenParameter.column() - 1]?.apply {
                        top = minOf(currentRect.top, top)
                        bottom = maxOf(currentRect.bottom, bottom)
                    }
                } else {
                    verticalDividerMap[gridOnScreenParameter.column() - 1] =
                        RectF(currentRect.left - verticalDividerWidth / 2, currentRect.top, currentRect.left - verticalDividerWidth / 2, currentRect.bottom)
                }
            }
            if (gridFrameDetectRect.right != 1) {
                if (verticalDividerMap.contains(gridOnScreenParameter.column())) {
                    verticalDividerMap[gridOnScreenParameter.column()]?.apply {
                        top = minOf(currentRect.top, top)
                        bottom = maxOf(currentRect.bottom, bottom)
                    }
                } else {
                    verticalDividerMap[gridOnScreenParameter.column()] =
                        RectF(currentRect.right + verticalDividerWidth / 2, currentRect.top, currentRect.right + verticalDividerWidth / 2, currentRect.bottom)
                }
            }

            // Get horizontal divider
            if (gridFrameDetectRect.top != 1) {
                if (horizontalDividerMap.contains(gridOnScreenParameter.row() - 1)) {
                    horizontalDividerMap[gridOnScreenParameter.row() - 1]?.apply {
                        right = maxOf(currentRect.right, right)
                        left = minOf(currentRect.left, left)
                    }
                } else {
                    horizontalDividerMap[gridOnScreenParameter.row() - 1] =
                        RectF(currentRect.left, currentRect.top - horizontalDividerWidth / 2, currentRect.right, currentRect.top - horizontalDividerWidth / 2)
                }
            }
            if (gridFrameDetectRect.bottom != 1) {
                if (horizontalDividerMap.contains(gridOnScreenParameter.row())) {
                    horizontalDividerMap[gridOnScreenParameter.row()]?.apply {
                        right = maxOf(currentRect.right, right)
                        left = minOf(currentRect.left, left)
                    }
                } else {
                    horizontalDividerMap[gridOnScreenParameter.row()] =
                        RectF(currentRect.left, currentRect.bottom + horizontalDividerWidth / 2, currentRect.right, currentRect.bottom + horizontalDividerWidth / 2)
                }
            }
        }

        // Draw vertical divider
        if (verticalDividerWidth > 0 && verticalDividerColor != null) {
            verticalDividerMap.forEach {
                c.drawLine(it.value.left, it.value.top, it.value.right, it.value.bottom, verticalDividerPaint)
            }
        }

        // Draw horizontal divider
        if (horizontalDividerWidth > 0 && horizontalDividerColor != null) {
            horizontalDividerMap.forEach {
                c.drawLine(it.value.left, it.value.top, it.value.right, it.value.bottom, horizontalDividerPaint)
            }
        }

        // Draw frame
        if (frameWidth > 0 && frameColor != null) {
            if (frameDetectRect.left == 1) c.drawLine(frameRect.left - frameWidth / 2, frameRect.top - frameWidth, frameRect.left - frameWidth / 2, frameRect.bottom + frameWidth, framePaint)
            if (frameDetectRect.top == 1) c.drawLine(frameRect.left - frameWidth, frameRect.top - frameWidth / 2, frameRect.right + frameWidth, frameRect.top - frameWidth / 2, framePaint)
            if (frameDetectRect.right == 1) c.drawLine(frameRect.right + frameWidth / 2, frameRect.top - frameWidth, frameRect.right + frameWidth / 2, frameRect.bottom + frameWidth, framePaint)
            if (frameDetectRect.bottom == 1) c.drawLine(frameRect.left - frameWidth, frameRect.bottom + frameWidth / 2, frameRect.right + frameWidth, frameRect.bottom + frameWidth / 2, framePaint)
        }

        // Draw corner
        if (cornerRadius > 0) {
            if (frameDetectRect.left == 1 && frameDetectRect.top == 1) drawCornerRadius(frameRect.left, frameRect.top, LEFT_TOP, c)
            if (frameDetectRect.right == 1 && frameDetectRect.top == 1) drawCornerRadius(frameRect.right, frameRect.top, RIGHT_TOP, c)
            if (frameDetectRect.left == 1 && frameDetectRect.bottom == 1) drawCornerRadius(frameRect.left, frameRect.bottom, LEFT_BOTTOM, c)
            if (frameDetectRect.right == 1 && frameDetectRect.bottom == 1) drawCornerRadius(frameRect.right, frameRect.bottom, RIGHT_BOTTOM, c)
        }
        c.restoreToCount(layer)
    }

    private fun getBackgroundColor(position: Int, spanCount: Int): Int {
        try {
            val relatePosition = position / spanCount
            if (relatePosition == 0 && titleBackGroundColor != null) {
                return titleBackGroundColor
            }
            return gridBackGroundColorList?.get(relatePosition % gridBackGroundColorList.size) ?: Color.TRANSPARENT
        } catch (e: Exception) {
            return Color.TRANSPARENT
        }
    }

    private fun drawCornerRadius(x: Float, y: Float, borderType: Int, c: Canvas) {
        path.reset()
        val zeroX = x + CORNER_RADIUS_ZERO_POSITION_TABLE[borderType].first * frameWidth
        val zeroY = y + CORNER_RADIUS_ZERO_POSITION_TABLE[borderType].second * frameWidth
        path.moveTo(zeroX, zeroY)
        path.rLineTo(
            CORNER_RADIUS_FIRST_MOVE_POSITION[borderType].first * cornerRadius.toFloat(),
            CORNER_RADIUS_FIRST_MOVE_POSITION[borderType].second * cornerRadius.toFloat()
        )
        val arcRect = RectF(
            zeroX + CORNER_RADIUS_RECT_TABLE[borderType].left * cornerRadius.toFloat(),
            zeroY + CORNER_RADIUS_RECT_TABLE[borderType].top * cornerRadius.toFloat(),
            zeroX + CORNER_RADIUS_RECT_TABLE[borderType].right * cornerRadius.toFloat(),
            zeroY + CORNER_RADIUS_RECT_TABLE[borderType].bottom * cornerRadius.toFloat()
        )
        path.arcTo(arcRect, CORNER_RADIUS_ARC_START_ANGLE[borderType], 90f, false)
        c.drawPath(path, borderPaint)

        if (frameWidth > 0) {
            path.reset()
            arcRect.inset(frameWidth / 2f, frameWidth / 2f)
            path.arcTo(arcRect, CORNER_RADIUS_ARC_START_ANGLE[borderType], 90f, true)
            c.drawPath(path, framePaint)
        }
    }

    companion object {
        const val NOT_FRAME = 0
        const val IS_FRAME = 1

        const val LEFT_TOP = 0
        const val RIGHT_TOP = 1
        const val LEFT_BOTTOM = 2
        const val RIGHT_BOTTOM = 3

        val CORNER_RADIUS_ZERO_POSITION_TABLE = listOf(
            -1 to -1,
            1 to -1,
            -1 to 1,
            1 to 1
        )

        val CORNER_RADIUS_FIRST_MOVE_POSITION = listOf(
            0 to 1,
            -1 to 0,
            1 to 0,
            0 to -1
        )

        val CORNER_RADIUS_RECT_TABLE = listOf(
            Rect(0, 0, 2, 2),
            Rect(-2, 0, 0, 2),
            Rect(0, -2, 2, 0),
            Rect(-2, -2, 0, 0)
        )

        val CORNER_RADIUS_ARC_START_ANGLE = listOf(180f, 270f, 90f, 0f)
    }
}