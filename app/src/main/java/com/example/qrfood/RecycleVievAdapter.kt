package com.example.qrfood

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrfood.databinding.FoodItemBinding
import com.example.samsungfouth.MainActivityViewModel

class RecycleVievAdapter(var list: List<Food>, val viewModel: MainActivityViewModel) :
    RecyclerView.Adapter<RecycleVievAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.food = list.get(pos)
            binding.deleteButton.setOnClickListener(){
                delete_item(pos)
            }
            binding.id = pos + 1
        }
    }


    fun delete_item(id:Int){
        viewModel.delete_item(id)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleVievAdapter.MyViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecycleVievAdapter.MyViewHolder, position: Int) {
        Log.d("asdasd", list.size.toString())
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d("asdasd", list.size.toString())
        return list.size
    }


}