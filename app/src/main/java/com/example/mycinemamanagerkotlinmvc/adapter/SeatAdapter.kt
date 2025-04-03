package com.example.mycinemamanagerkotlinmvc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.databinding.ItemSeatBinding
import com.example.mycinemamanagerkotlinmvc.model.SeatLocal

class SeatAdapter(private val mListSeats: List<SeatLocal>?,
                  private val iManagerSeatListener: IManagerSeatListener) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val itemSeatBinding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeatViewHolder(itemSeatBinding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = mListSeats!![position]
        if (seat.isSelected) {
            holder.mItemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_not_avaiable_corner_5)
        } else {
            if (seat.isChecked) {
                holder.mItemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_selected_corner_5)
            } else {
                holder.mItemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_avaiable_corner_5)
            }
        }
        holder.mItemSeatBinding.tvTitle.text = seat.title
        holder.mItemSeatBinding.layoutItem.setOnClickListener { iManagerSeatListener.clickItemSeat(seat) }
    }


    interface IManagerSeatListener {
        fun clickItemSeat(seat: SeatLocal)
    }

    override fun getItemCount(): Int {
        return mListSeats?.size ?: 0
    }
    class SeatViewHolder(val mItemSeatBinding: ItemSeatBinding) : RecyclerView.ViewHolder(mItemSeatBinding.root)
}
