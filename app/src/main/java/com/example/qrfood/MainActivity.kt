package com.example.qrfood

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrfood.databinding.LoadingBinding
import com.example.qrfood.databinding.MainLayoutBinding
import com.example.qrfood.databinding.NewFoodFormBinding
import com.example.qrfood.databinding.SettingsBinding
import com.example.samsungfouth.MainActivityViewModel
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : ComponentActivity() {
    lateinit var database: Database
    lateinit var viewModel: MainActivityViewModel
    val requester = HTTPRequester()
    lateinit var binding: MainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)



        database = Database(applicationContext)
        val provider = ViewModelProvider(this)
        viewModel = provider.get(MainActivityViewModel::class.java)

        viewModel.binding = binding
        viewModel.database = database




        viewModel.FoodArray.value = database.get_food_array()


        viewModel.change_cycles()

        val recycleOfTodaysFood = binding.recycleOfTodaysFood


        recycleOfTodaysFood.layoutManager = LinearLayoutManager(this)
        recycleOfTodaysFood.adapter =  RecycleVievAdapter(viewModel.FoodArray.value?: listOf(), viewModel)

        initView(binding)


        binding.settingsButton.setOnClickListener(){
            settings_dialog()
        }

        binding.qrScanButton.setOnClickListener(){
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setPrompt("Scan a barcode or QR Code")
// Указываем кастомную активность с фиксированной ориентацией
            intentIntegrator.setCaptureActivity(PortraitCaptureActivity::class.java)
            intentIntegrator.initiateScan()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                val binding_form = LoadingBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setView(binding_form.root)
                val alertDialog = builder.create()
                alertDialog.show()
                val barcode_obs = Barcode_getter(this)
                Log.d("aaabbb", intentResult.contents.toString())
                requester.sendGetRequest(alertDialog, this, barcode_obs, viewModel, binding.recycleOfTodaysFood, intentResult.contents)


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun initView(binding: MainLayoutBinding){

        binding.addFood.setOnClickListener(){

            val builder = AlertDialog.Builder(this@MainActivity)
            val bind_form = NewFoodFormBinding.inflate(layoutInflater)
            builder.setView(bind_form.root)


            val alertDialog = builder.create()

            bind_form.addButton.setOnClickListener(){
                if (bind_form.nameField.text.toString() != "" &&
                    bind_form.p1Input.text.toString() != "" &&
                    bind_form.p2Input.text.toString() != "" &&
                    bind_form.p3Input.text.toString() != "" &&
                    bind_form.p4Input.text.toString() != ""){
                    viewModel.add_food(bind_form.nameField.text.toString(), bind_form.p1Input.text.toString().toInt(), bind_form.p2Input.text.toString().toInt(), bind_form.p3Input.text.toString().toInt(),bind_form.p4Input.text.toString().toInt())
                    binding.recycleOfTodaysFood.adapter = RecycleVievAdapter(viewModel.FoodArray.value?: listOf(), viewModel)
                }
                else{
                    show_wrong_input_alert()

                }



                alertDialog.dismiss()
            }
            // Show the Alert Dialog box
            alertDialog.show()
//            viewModel.add_food("Название", 2, 1, 1,3)
//            binding.recycleOfTodaysFood.adapter = RecycleVievAdapter(viewModel.FoodArray.value?: listOf(), viewModel)
        }

    }


    fun settings_dialog(){
        val builder = AlertDialog.Builder(this@MainActivity)
        val bind_form = SettingsBinding.inflate(layoutInflater)
        builder.setView(bind_form.root)


        val alertDialog = builder.create()


        bind_form.addButton.setOnClickListener(){
            if (bind_form.p1Input.text.toString() != "" &&
                bind_form.p2Input.text.toString() != "" &&
                bind_form.p3Input.text.toString() != "" &&
                bind_form.p4Input.text.toString() != ""){
                database.max_shugar = bind_form.p1Input.text.toString().toInt()
                database.max_fats = bind_form.p2Input.text.toString().toInt()
                database.max_squirrels = bind_form.p3Input.text.toString().toInt()
                database.max_carbohydrates = bind_form.p4Input.text.toString().toInt()
                viewModel.change_cycles()
            }
            else{
                show_wrong_input_alert()
            }



            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    fun show_wrong_input_alert(){
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Ошибка")
        builder.setMessage("Заполните все окна")
        builder.setPositiveButton("Ок"){
                dialog, which -> dialog.dismiss()
        }
        builder.setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onStop() {
        super.onStop()
        database.save_food_array(viewModel.FoodArray.value!!)
    }
}


