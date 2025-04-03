package com.example.mycinemamanagerkotlinmvc.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinemamanagerkotlinmvc.databinding.ItemCategoryAdminBinding
import com.example.mycinemamanagerkotlinmvc.model.Category
import com.example.mycinemamanagerkotlinmvc.util.GlideUtils

class AdminCategoryAdapter(private val mListCategory: List<Category>?,
                           private val iManagerCategoryListener: IManagerCategoryListener) : RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder>()  {

    interface IManagerCategoryListener {
        fun editCategory(category: Category)
        fun deleteCategory(category: Category)
    }

    class CategoryViewHolder(val mItemCategoryAdminBinding: ItemCategoryAdminBinding) : RecyclerView.ViewHolder(mItemCategoryAdminBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemCategoryAdminBinding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemCategoryAdminBinding)
    }
    override fun getItemCount(): Int {
        return mListCategory?.size ?: 0
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = mListCategory!![position]
        GlideUtils.loadUrl(category.image, holder.mItemCategoryAdminBinding.imgCategory)
        holder.mItemCategoryAdminBinding.tvCategoryName.text = category.name
        holder.mItemCategoryAdminBinding.imgEdit.setOnClickListener { iManagerCategoryListener.editCategory(category) }
        holder.mItemCategoryAdminBinding.imgDelete.setOnClickListener { iManagerCategoryListener.deleteCategory(category) }
    }
}
