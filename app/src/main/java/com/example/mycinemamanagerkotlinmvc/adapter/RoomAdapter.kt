package com.example.mycinemamanagerkotlinmvc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.databinding.ItemRoomBinding
import com.example.mycinemamanagerkotlinmvc.model.Room

class RoomAdapter(private val mListRooms: List<Room>?,
                  private val iManagerRoomListener: IManagerRoomListener) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>()  {

    private var onBind = false

    interface IManagerRoomListener {
        fun clickItemRoom(room: Room)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemRoomBinding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(itemRoomBinding)
    }


    override fun getItemCount(): Int {
        return mListRooms?.size ?: 0
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = mListRooms!![position]
        holder.mItemRoomBinding.tvTitle.text = room.title
        onBind = true
        holder.mItemRoomBinding.chbSelected.isChecked = room.isSelected
        onBind = false
        holder.mItemRoomBinding.chbSelected.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean ->
            if (!onBind) {
                iManagerRoomListener.clickItemRoom(room)
            }
        }
    }

    class RoomViewHolder(val mItemRoomBinding: ItemRoomBinding) : RecyclerView.ViewHolder(mItemRoomBinding.root)

}
