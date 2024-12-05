package com.example.samsungfouth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.qrfood.Database
import com.example.qrfood.Food
import com.example.qrfood.RecycleVievAdapter
import com.example.qrfood.databinding.MainLayoutBinding

class MainActivityViewModel: ViewModel() {
    var FoodArray = MutableLiveData<ArrayList<Food>>();
    lateinit var binding: MainLayoutBinding
    lateinit var database: Database


    fun delete_item(id:Int){
        FoodArray.value?.removeAt(id)
        binding.recycleOfTodaysFood.adapter = RecycleVievAdapter(FoodArray.value!!, this)
        change_cycles()
    }

    fun add_food(name: String, p1: Int, p2: Int, p3: Int, p4: Int){
        val arr = FoodArray.value
        if (arr != null) {
            arr.add(Food(name, p1, p2, p3, p4))
        }
        else{
            FoodArray.value = arrayListOf(Food(name, p1, p2, p3, p4))

        }

        change_cycles()
    }

    fun change_cycles(){
        var sum_shugar = 0
        var sum_fats = 0
        var sum_squirrels = 0
        var sum_carbohydrates = 0
        FoodArray.value?.forEach {
            sum_shugar += it.p1
            sum_fats += it.p2
            sum_squirrels += it.p3
            sum_carbohydrates += it.p4
        }
        binding.p1ProgressBar.progress = (100 - (sum_shugar.toFloat() / database.max_shugar.toFloat()) * 100).toInt()
        binding.p2ProgressBar.progress = (100 - (sum_fats.toFloat() / database.max_fats.toFloat()) * 100).toInt()
        binding.p3ProgressBar.progress = (100 - (sum_squirrels.toFloat() / database.max_squirrels.toFloat()) * 100).toInt()
        binding.p4ProgressBar.progress = (100 - (sum_carbohydrates.toFloat() / database.max_carbohydrates.toFloat()) * 100).toInt()

        binding.p1Text.text = "${sum_shugar}г/${database.max_shugar}г"
        binding.p2Text.text = "${sum_fats}г/${database.max_fats}г"
        binding.p3Text.text = "${sum_squirrels}г/${database.max_squirrels}г"
        binding.p4Text.text = "${sum_carbohydrates}г/${database.max_carbohydrates}г"

    }
}
