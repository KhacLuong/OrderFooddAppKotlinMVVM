package com.example.mycinemamanagerkotlinmvc.fragment.admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycinemamanagerkotlinmvc.MyApplication
import com.example.mycinemamanagerkotlinmvc.R
import com.example.mycinemamanagerkotlinmvc.activity.admin.AddCategoryActivity
import com.example.mycinemamanagerkotlinmvc.adapter.admin.AdminCategoryAdapter
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.getTextSearch
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.hideSoftKeyboard
import com.example.mycinemamanagerkotlinmvc.databinding.FragmentAdminCategoryBinding
import com.example.mycinemamanagerkotlinmvc.model.Category
import com.example.mycinemamanagerkotlinmvc.util.StringUtil.isEmpty
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.Locale

class AdminCategoryFragment : Fragment() {
    private var mFragmentAdminCategoryBinding: FragmentAdminCategoryBinding? = null
    private var mListCategory: MutableList<Category>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mFragmentAdminCategoryBinding = FragmentAdminCategoryBinding.inflate(inflater, container, false)
        initListener()
        getListCategory("")
        return mFragmentAdminCategoryBinding!!.root
    }

    fun getListCategory(key: String?) {
        if (activity == null) {
            return
        }
        MyApplication[activity].getCategoryDatabaseReference().addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListCategory != null) {
                    mListCategory!!.clear()
                } else {
                    mListCategory = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val category = dataSnapshot.getValue(Category::class.java)
                    if (category != null) {
                        if (isEmpty(key)) {
                            mListCategory!!.add(0, category)
                        } else {
                            if (getTextSearch(category.name).toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                                    .contains(getTextSearch(key).toLowerCase(Locale.getDefault()).trim { it <= ' ' })) {
                                mListCategory!!.add(0, category)
                            }
                        }
                    }
                }
                loadListData()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadListData() {
        if (activity == null) {
            return
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        mFragmentAdminCategoryBinding!!.rcvCategory.layoutManager = linearLayoutManager
        val adminCategoryAdapter = AdminCategoryAdapter(mListCategory,
            object : AdminCategoryAdapter.IManagerCategoryListener {
                override fun editCategory(category: Category) {
                    onClickEditCategory(category)
                }

                override fun deleteCategory(category: Category) {
                    deleteCategoryItem(category)
                }
            })
        mFragmentAdminCategoryBinding!!.rcvCategory.adapter = adminCategoryAdapter
    }

    private fun deleteCategoryItem(category: Category) {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.msg_delete_title))
            .setMessage(getString(R.string.msg_confirm_delete))
            .setPositiveButton(getString(R.string.action_ok)) { _: DialogInterface?, _: Int ->
                if (activity == null) {
                    return@setPositiveButton
                }
                MyApplication[activity].getCategoryDatabaseReference()
                    .child(category.id.toString()).removeValue { _, _ ->
                        Toast.makeText(activity,
                            getString(R.string.msg_delete_category_successfully), Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(getString(R.string.action_cancel), null)
            .show()
    }

    private fun onClickEditCategory(category: Category) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantKey.KEY_INTENT_CATEGORY_OBJECT, category)
        GlobalFunction.startActivity(activity, AddCategoryActivity::class.java, bundle)
    }


    private fun initListener() {
        mFragmentAdminCategoryBinding!!.btnAddCategory.setOnClickListener { onClickAddCategory() }
        mFragmentAdminCategoryBinding!!.imgSearch.setOnClickListener { searchCategory() }
        mFragmentAdminCategoryBinding!!.edtSearchName.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCategory()
                return@setOnEditorActionListener true
            }
            false
        }
        mFragmentAdminCategoryBinding!!.edtSearchName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val strKey = s.toString().trim { it <= ' ' }
                if (strKey == "" || strKey.isEmpty()) {
                    getListCategory("")
                }
            }
        })
    }

    private fun searchCategory() {
        val strKey = mFragmentAdminCategoryBinding!!.edtSearchName.text.toString().trim { it <= ' ' }
        getListCategory(strKey)
        hideSoftKeyboard(activity)
    }


    private fun onClickAddCategory() {
       GlobalFunction.startActivity(activity, AddCategoryActivity::class.java)
    }

}
