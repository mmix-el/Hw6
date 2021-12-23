package xyz.mmixel.hw6.ui.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.mmixel.hw6.R

class ContactDecoration : RecyclerView.ItemDecoration() {

    private var paint = Paint().apply {
        style = Paint.Style.FILL
        isFakeBoldText = true
        isAntiAlias = true
        color = Color.GRAY
        textSize = 48F
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val pixelOffset = parent.resources.getDimensionPixelOffset(R.dimen.list_item_margin)
        val viewCount = parent.childCount
        val paddingEnd = 20F
        val paddingTop = 64F

        for (i in 0 until viewCount) {
            val child = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)
            val top = child.top - pixelOffset / 2F
            child.bottom + pixelOffset / 2F
            val charWidth = 28F
            val text = childPos.toString()
            val textWidth = charWidth * text.length
            val x = parent.width - textWidth - pixelOffset - paddingEnd
            val y = paddingTop + top
            c.drawText(text, x, y, paint)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pixelOffset = view.resources.getDimensionPixelOffset(R.dimen.list_item_margin)

        outRect.apply {
            left = pixelOffset
            right = pixelOffset
            top = pixelOffset / 2
            bottom = pixelOffset / 2
        }
    }
}