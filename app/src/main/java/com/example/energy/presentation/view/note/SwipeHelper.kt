package com.example.energy.presentation.view.note

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.energy.R
import kotlin.math.max
import kotlin.math.min

class SwipeHelper:ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        Log.d("AppTest", "getMovementFlags")


        val view = getView(viewHolder)
        clamp = view.width.toFloat() / 10*3

        
        //(Drag, Swipe) 오른쪽에서 왼쪽으로 스와이프 동작
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))

        previousPosition = viewHolder.adapterPosition


    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        //super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.let {

            // 현재 스와이프 한 아이템 위치
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            Log.d("AppTest", "onChildDraw")

            val view = getView(viewHolder)

            val isClamped = getClamped(viewHolder as NoteAdapter.NoteViewHolder)


            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)


            currentDx = x
            Log.d("AppTest", "x : ${x}")


            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                x,
                dY,
                actionState,
                isCurrentlyActive
            )
        } else {
            // 스와이프가 아닌 경우, 뷰를 원래 상태로 복원
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                getView(viewHolder),
                0f,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }


    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // View의 가로 길이의 30% 만 스와이프 되도록
        val maxSwipe: Float = -view.width.toFloat() / 10 * 3

        // RIGHT 방향으로 swipe 막기
        val right: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp

        } else {
            dX
        }

        return min(max(maxSwipe, x), right)
    }


    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {

        return defaultValue * 10
    }




    private fun getView(viewHolder: ViewHolder):View {
        return (viewHolder as NoteAdapter.NoteViewHolder).itemView.findViewById(R.id.swipe_view)
    }



    private fun setClamped(viewHolder: NoteAdapter.NoteViewHolder, isClamped: Boolean){
        return viewHolder.setClamped(isClamped)
    }


    private fun getClamped(viewHolder: NoteAdapter.NoteViewHolder) : Boolean {
        return viewHolder.getClamped()
    }





    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }




    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {

        Log.d("AppTest", "getSwipeThreshold")

        Log.d("AppTest", "isClamped = ${currentDx <= -clamp}")

        setClamped(viewHolder as NoteAdapter.NoteViewHolder, currentDx<= -clamp)
        return 2f
    }


    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }


    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        // isClamped를 view의 tag로 관리
        return viewHolder.itemView.tag as? Boolean ?: false
    }




}