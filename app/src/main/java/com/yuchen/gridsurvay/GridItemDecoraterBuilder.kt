package com.yuchen.gridsurvay

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractGridItemDecorationBuilder {
    abstract fun horizontalDivider(width: Int): AbstractGridItemDecorationBuilder
    abstract fun horizontalDivider(width: Float): AbstractGridItemDecorationBuilder
    abstract fun horizontalDividerColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun verticalDivider(width: Int): AbstractGridItemDecorationBuilder
    abstract fun verticalDivider(width: Float): AbstractGridItemDecorationBuilder
    abstract fun verticalDividerColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun divider(width: Int): AbstractGridItemDecorationBuilder
    abstract fun divider(width: Float): AbstractGridItemDecorationBuilder
    abstract fun dividerColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun dividerAndFrame(width: Int): AbstractGridItemDecorationBuilder
    abstract fun dividerAndFrame(width: Float): AbstractGridItemDecorationBuilder
    abstract fun dividerAndFrameColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun frame(width: Int): AbstractGridItemDecorationBuilder
    abstract fun frame(width: Float): AbstractGridItemDecorationBuilder
    abstract fun frameColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun cornerRadius(radius: Int): AbstractGridItemDecorationBuilder
    abstract fun cornerRadius(radius: Float): AbstractGridItemDecorationBuilder
    abstract fun titleBackGroundColor(color: Int): AbstractGridItemDecorationBuilder
    abstract fun gridBackGroundColorList(colorList: List<Int>): AbstractGridItemDecorationBuilder
    abstract fun build(): AbstractGridItemDecorationBuilder
    abstract fun into(recyclerView: RecyclerView)
    abstract fun get(): GridItemDecoration?
}

class GridItemDecorationBuilder(private val context: Context) : AbstractGridItemDecorationBuilder() {
    private var horizontalDividerInDp: Float = 0f
    private var horizontalDividerColor: Int? = null
    private var verticalDividerInDp: Float = 0f
    private var verticalDividerColor: Int? = null
    private var frameInDp: Float = 0f
    private var frameColor: Int? = null
    private var cornerRadius: Float = 0f
    private var titleBackGroundColor: Int? = null
    private var gridBackGroundColorList: List<Int>? = null
    private var gridItemDecoration: GridItemDecoration? = null

    override fun horizontalDivider(width: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        return this
    }

    override fun horizontalDivider(width: Float): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        return this
    }

    override fun horizontalDividerColor(color: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerColor = color
        return this
    }

    override fun verticalDivider(width: Int): AbstractGridItemDecorationBuilder {
        verticalDividerInDp = width.toFloat()
        return this
    }

    override fun verticalDivider(width: Float): AbstractGridItemDecorationBuilder {
        verticalDividerInDp = width
        return this
    }

    override fun verticalDividerColor(color: Int): AbstractGridItemDecorationBuilder {
        verticalDividerColor = color
        return this
    }

    override fun divider(width: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        verticalDividerInDp = width.toFloat()
        return this
    }

    override fun divider(width: Float): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        verticalDividerInDp = width
        return this
    }

    override fun dividerColor(color: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerColor = color
        verticalDividerColor = color
        return this
    }

    override fun dividerAndFrame(width: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        verticalDividerInDp = width.toFloat()
        frameInDp = width.toFloat()
        return this
    }

    override fun dividerAndFrame(width: Float): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        verticalDividerInDp = width
        frameInDp = width
        return this
    }

    override fun dividerAndFrameColor(color: Int): AbstractGridItemDecorationBuilder {
        horizontalDividerColor = color
        verticalDividerColor = color
        frameColor = color
        return this
    }

    override fun frame(width: Int): AbstractGridItemDecorationBuilder {
        frameInDp = width.toFloat()
        return this
    }

    override fun frame(width: Float): AbstractGridItemDecorationBuilder {
        frameInDp = width
        return this
    }

    override fun frameColor(color: Int): AbstractGridItemDecorationBuilder {
        frameColor = color
        return this
    }

    override fun cornerRadius(radius: Int): AbstractGridItemDecorationBuilder {
        cornerRadius = radius.toFloat()
        return this
    }

    override fun cornerRadius(radius: Float): AbstractGridItemDecorationBuilder {
        cornerRadius = radius
        return this
    }

    override fun titleBackGroundColor(color: Int): AbstractGridItemDecorationBuilder {
        titleBackGroundColor = color
        return this
    }

    override fun gridBackGroundColorList(colorList: List<Int>): AbstractGridItemDecorationBuilder {
        gridBackGroundColorList = colorList
        return this
    }

    override fun build(): AbstractGridItemDecorationBuilder {
        gridItemDecoration = GridItemDecoration(
            context,
            horizontalDividerInDp,
            horizontalDividerColor,
            verticalDividerInDp,
            verticalDividerColor,
            frameInDp,
            frameColor,
            cornerRadius,
            titleBackGroundColor,
            gridBackGroundColorList,
        )
        return this
    }

    override fun into(recyclerView: RecyclerView) {
        gridItemDecoration?.let {
            recyclerView.addItemDecoration(it)
        }
    }

    override fun get(): GridItemDecoration? = gridItemDecoration
}