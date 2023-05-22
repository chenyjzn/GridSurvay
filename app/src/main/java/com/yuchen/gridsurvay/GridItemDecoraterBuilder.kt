package com.yuchen.gridsurvay

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractGridItemDecorationBuilder {
    abstract fun horizontalDivider(width: Int, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun horizontalDivider(width: Float, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun verticalDivider(width: Int, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun verticalDivider(width: Float, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun divider(width: Int, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun divider(width: Float, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun dividerAndFrame(width: Int, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun dividerAndFrame(width: Float, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun frame(width: Int, color: Int? = null): AbstractGridItemDecorationBuilder
    abstract fun frame(width: Float, color: Int? = null): AbstractGridItemDecorationBuilder
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

    override fun horizontalDivider(width: Int, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        horizontalDividerColor = color
        return this
    }

    override fun horizontalDivider(width: Float, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        horizontalDividerColor = color
        return this
    }

    override fun verticalDivider(width: Int, color: Int?): AbstractGridItemDecorationBuilder {
        verticalDividerInDp = width.toFloat()
        verticalDividerColor = color
        return this
    }

    override fun verticalDivider(width: Float, color: Int?): AbstractGridItemDecorationBuilder {
        verticalDividerInDp = width
        verticalDividerColor = color
        return this
    }

    override fun divider(width: Int, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        horizontalDividerColor = color
        verticalDividerInDp = width.toFloat()
        verticalDividerColor = color
        return this
    }

    override fun divider(width: Float, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        horizontalDividerColor = color
        verticalDividerInDp = width
        verticalDividerColor = color
        return this
    }

    override fun dividerAndFrame(width: Int, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width.toFloat()
        horizontalDividerColor = color
        verticalDividerInDp = width.toFloat()
        verticalDividerColor = color
        frameInDp = width.toFloat()
        frameColor = color
        return this
    }

    override fun dividerAndFrame(width: Float, color: Int?): AbstractGridItemDecorationBuilder {
        horizontalDividerInDp = width
        horizontalDividerColor = color
        verticalDividerInDp = width
        verticalDividerColor = color
        frameInDp = width
        frameColor = color
        return this
    }

    override fun frame(width: Int, color: Int?): AbstractGridItemDecorationBuilder {
        frameInDp = width.toFloat()
        frameColor = color
        return this
    }

    override fun frame(width: Float, color: Int?): AbstractGridItemDecorationBuilder {
        frameInDp = width
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