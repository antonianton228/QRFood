package com.example.qrfood

import android.content.Context
import android.util.Log

class Database(val context: Context) {
    val shared_prefs = context.getSharedPreferences("database", Context.MODE_PRIVATE)
    var max_shugar = 0
    var max_fats = 0
    var max_squirrels = 0
    var max_carbohydrates = 0



    fun get_food_array(): ArrayList<Food>{

        val count = shared_prefs.getInt("count_of_foods", 0)
        Log.d("Save", "Load")
        Log.d("Save", count.toString())
        val result: ArrayList<Food> = arrayListOf()

        max_shugar = shared_prefs.getInt("max_shugar", 100)
        max_fats = shared_prefs.getInt("max_fats", 100)
        max_squirrels = shared_prefs.getInt("max_squirrels", 100)
        max_carbohydrates = shared_prefs.getInt("max_carbohydrates", 100)

        for(i in 0..count - 1){
            val name: String = shared_prefs.getString("name_$i", "none").toString()
            val shugar: Int = shared_prefs.getInt("shugar_$i", 0)
            val fats: Int = shared_prefs.getInt("fats_$i", 0)
            val squirrels: Int = shared_prefs.getInt("squirrels_$i", 0)
            val carbohydrates: Int = shared_prefs.getInt("carbohydrates_$i", 0)
            result.add(Food(name, shugar, fats, squirrels, carbohydrates))
        }
        return result

    }
    fun save_food_array(arr: ArrayList<Food>){
        Log.d("Save", "Save")
        shared_prefs.edit().putInt("count_of_foods", arr.size).apply()
        shared_prefs.edit().putInt("max_shugar", max_shugar).apply()
        shared_prefs.edit().putInt("max_fats", max_fats).apply()
        shared_prefs.edit().putInt("max_squirrels",max_squirrels).apply()
        shared_prefs.edit().putInt("max_carbohydrates", max_carbohydrates).apply()




        for(i in 0..arr.size - 1){
            shared_prefs.edit().putString("name_$i", arr.get(i).name).apply()
            shared_prefs.edit().putInt("shugar_$i", arr.get(i).p1).apply()
            shared_prefs.edit().putInt("fats_$i", arr.get(i).p2).apply()
            shared_prefs.edit().putInt("squirrels_$i", arr.get(i).p3).apply()
            shared_prefs.edit().putInt("carbohydrates_$i", arr.get(i).p4).apply()
        }
    }

}