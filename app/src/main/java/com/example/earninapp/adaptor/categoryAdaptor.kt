package com.example.earninapp.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.earninapp.QuizActivity
import com.example.earninapp.databinding.CategoryitemBinding
import com.example.earninapp.model.categoryModelClass

class categoryAdaptor(
    var categoryList: ArrayList<categoryModelClass>, var requireActivity: FragmentActivity
) : RecyclerView.Adapter<categoryAdaptor.MyCategoryViewHolder>(){
    class MyCategoryViewHolder(var binding: CategoryitemBinding) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
       return MyCategoryViewHolder(CategoryitemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount() = categoryList.size


    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        var dataList = categoryList[position]
        holder.binding.categoryImage.setImageResource(dataList.catImage)
        holder.binding.category.text = dataList.catText
        holder.binding.categorybtn.setOnClickListener{
            val intent = Intent(requireActivity, QuizActivity::class.java)
            intent.putExtra("categoryimg", dataList.catImage)
            intent.putExtra("questionType", dataList.catText)
            requireActivity.startActivity(intent)
        }
    }
}