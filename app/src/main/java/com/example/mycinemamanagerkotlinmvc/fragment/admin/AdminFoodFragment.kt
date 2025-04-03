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
import com.example.mycinemamanagerkotlinmvc.activity.admin.AddFoodActivity
import com.example.mycinemamanagerkotlinmvc.adapter.admin.AdminFoodAdapter
import com.example.mycinemamanagerkotlinmvc.constant.ConstantKey
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.getTextSearch
import com.example.mycinemamanagerkotlinmvc.constant.GlobalFunction.hideSoftKeyboard
import com.example.mycinemamanagerkotlinmvc.databinding.FragmentAdminFoodBinding
import com.example.mycinemamanagerkotlinmvc.model.Food
import com.example.mycinemamanagerkotlinmvc.util.StringUtil.isEmpty
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.Locale

class AdminFoodFragment : Fragment() {
    private var mFragmentAdminFoodBinding: FragmentAdminFoodBinding? = null
    private var mListFood: MutableList<Food>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mFragmentAdminFoodBinding = FragmentAdminFoodBinding.inflate(inflater, container, false)
        getListFoods("")
        initListener()
        return mFragmentAdminFoodBinding!!.root
    }

    private fun initListener() {
        mFragmentAdminFoodBinding!!.btnAddFood.setOnClickListener { onClickAddFood() }
        mFragmentAdminFoodBinding!!.imgSearch.setOnClickListener { searchFood() }

        mFragmentAdminFoodBinding!!.edtSearchName.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood()
                return@setOnEditorActionListener true
            }
            false
        }
        mFragmentAdminFoodBinding!!.edtSearchName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val strKey = s.toString().trim { it <= ' ' }
                if (strKey == "" || strKey.isEmpty()) {
                    getListFoods("")
                }
            }
        })

    }

    private fun searchFood() {
        val strKey = mFragmentAdminFoodBinding!!.edtSearchName.text.toString().trim { it <= ' ' }
        getListFoods(strKey)
        hideSoftKeyboard(activity)
    }

    private fun onClickAddFood() {
        GlobalFunction.startActivity(activity, AddFoodActivity::class.java)
    }

    fun getListFoods(key: String?) {
        if (activity == null) {
            return
        }
        MyApplication[activity].getFoodDatabaseReference().addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (mListFood != null) {
                    mListFood!!.clear()
                } else {
                    mListFood = ArrayList()
                }
                for (dataSnapshot in snapshot.children) {
                    val food = dataSnapshot.getValue(Food::class.java)
                    if (food != null) {
                        if (isEmpty(key)) {
                            mListFood!!.add(0, food)
                        } else {
                            if (getTextSearch(food.name).toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                                    .contains(getTextSearch(key).toLowerCase(Locale.getDefault()).trim { it <= ' ' })) {
                                mListFood!!.add(0, food)
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
        mFragmentAdminFoodBinding!!.rcvFood.layoutManager = linearLayoutManager
        val adminFoodAdapter = AdminFoodAdapter(mListFood, object :
            AdminFoodAdapter.IManagerFoodListener {
            override fun editFood(food: Food) {
                onClickEditFood(food)
            }

            override fun deleteFood(food: Food) {
                deleteFoodItem(food)
            }
        })
        mFragmentAdminFoodBinding!!.rcvFood.adapter = adminFoodAdapter
    }

    private fun deleteFoodItem(food: Food) {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.msg_delete_title))
            .setMessage(getString(R.string.msg_confirm_delete))
            .setPositiveButton(getString(R.string.action_ok)) { _: DialogInterface?, _: Int ->
                if (activity == null) {
                    return@setPositiveButton
                }
                MyApplication[activity].getFoodDatabaseReference()
                    .child(food.id.toString()).removeValue { _, _ ->
                        Toast.makeText(activity,
                            getString(R.string.msg_delete_food_successfully), Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(getString(R.string.action_cancel), null)
            .show()
    }
    private fun onClickEditFood(food: Food) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantKey.KEY_INTENT_FOOD_OBJECT, food)
        GlobalFunction.startActivity(activity, AddFoodActivity::class.java, bundle)
    }

}
