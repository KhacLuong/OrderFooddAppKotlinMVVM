package com.example.mycinemamanagerkotlinmvc.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.databinding.ItemFoodBinding
import com.example.mycinemamanagerkotlinmvc.model.Food

class AdminFoodAdapter(private val mListFood: List<Food>?,
                       private val iManagerFoodListener: IManagerFoodListener) : RecyclerView.Adapter<AdminFoodAdapter.FoodViewHolder>() {
    interface IManagerFoodListener {
        fun editFood(food: Food)
        fun deleteFood(food: Food)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(itemFoodBinding)
    }
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = mListFood!![position]
        holder.mItemFoodBinding.tvName.text = food.name
        val strPrice = food.price.toString() + ConstantKey.UNIT_CURRENCY
        holder.mItemFoodBinding.tvPrice.text = strPrice
        holder.mItemFoodBinding.tvQuantity.text = food.quantity.toString()
        holder.mItemFoodBinding.imgEdit.setOnClickListener { iManagerFoodListener.editFood(food) }
        holder.mItemFoodBinding.imgDelete.setOnClickListener { iManagerFoodListener.deleteFood(food) }
    }

    override fun getItemCount(): Int {
        return mListFood?.size ?: 0
    }

    class FoodViewHolder(val mItemFoodBinding: ItemFoodBinding) : RecyclerView.ViewHolder(mItemFoodBinding.root)

}
