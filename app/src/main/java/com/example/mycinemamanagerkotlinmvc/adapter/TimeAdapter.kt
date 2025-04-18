package com.example.mycinemamanagerkotlinmvc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.databinding.ItemTimeBinding
import com.example.mycinemamanagerkotlinmvc.model.SlotTime

class TimeAdapter(private val mListTimes: List<SlotTime>?,
                  private val iManagerTimeListener: IManagerTimeListener) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private var onBind = false

    interface IManagerTimeListener {
        fun clickItemTime(time: SlotTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val itemTimeBinding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(itemTimeBinding)

    }

    override fun getItemCount(): Int {
        return mListTimes?.size ?: 0
    }


    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = mListTimes!![position]
        holder.mItemTimeBinding.tvTitle.text = time.title
        onBind = true
        holder.mItemTimeBinding.chbSelected.isChecked = time.isSelected
        onBind = false
        holder.mItemTimeBinding.chbSelected.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean ->
            if (!onBind) {
                iManagerTimeListener.clickItemTime(time)
            }
        }
    }


    class TimeViewHolder(val mItemTimeBinding: ItemTimeBinding) : RecyclerView.ViewHolder(mItemTimeBinding.root)

}
