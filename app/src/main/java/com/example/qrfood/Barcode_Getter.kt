package com.example.qrfood

import android.content.Context

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.qrfood.databinding.AddFromBarcodeLayoutBinding
import com.example.qrfood.databinding.AddNewBarcodeBinding
import com.example.qrfood.databinding.SettingsBinding
import com.example.samsungfouth.MainActivityViewModel

class Barcode_getter(val context: Context) {


    fun stop_loading(
        barcode: Barcode,
        viewModel: MainActivityViewModel,
        recycleOfToday: RecyclerView
    ) {
        val binding = AddFromBarcodeLayoutBinding.inflate(LayoutInflater.from(context))
        binding.shugarTextView.text = barcode.shugar.toString()
        binding.squirrelsTextView.text = barcode.squirrels.toString()
        binding.carbohydratesTextView.text = barcode.carbohydrates.toString()
        binding.fatsTextView.text = barcode.fats.toString()
        binding.nameTextView.text = barcode.name

        val builder = AlertDialog.Builder(context)
        val alertDialog = builder.setView(binding.root).create()

        binding.acceptButton.setOnClickListener() {
            if (binding.inputWeight.text.toString() == ""){
                show_wrong_input_alert()
            }
            else{
                viewModel.FoodArray.value?.add(
                    Food(
                        barcode.name,
                        (barcode.shugar.toFloat() * binding.inputWeight.text.toString()
                            .toInt() / 100).toInt(),
                        (barcode.fats.toFloat() * binding.inputWeight.text.toString()
                            .toInt() / 100).toInt(),
                        (barcode.squirrels.toFloat() * binding.inputWeight.text.toString()
                            .toInt() / 100).toInt(),
                        (barcode.carbohydrates.toFloat() * binding.inputWeight.text.toString()
                            .toInt() / 100).toInt()
                    )
                )
                viewModel.change_cycles()
                recycleOfToday.adapter =
                    RecycleVievAdapter(viewModel.FoodArray.value ?: listOf(), viewModel)
            }

            alertDialog.dismiss()
        }



        alertDialog.show()
    }

    fun error_loading(viewModel: MainActivityViewModel,
                      recycleOfToday: RecyclerView) {
        val builder = AlertDialog.Builder(context).setMessage("Ошибка подключения, добавьте продукт вручную").setTitle("Ошибка").setPositiveButton("Ok"){
            a, b -> a.dismiss()
        }.create().show()

    }

    fun no_barcode(
        viewModel: MainActivityViewModel,
        recycleOfToday: RecyclerView, barcode: String
    ) {
        val binding = AddNewBarcodeBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        val alertDialog = builder.create()



        binding.addButton.setOnClickListener() {

            if (binding.nameField.text.toString() != "" &&
                binding.p1Input.text.toString() != "" &&
                binding.p2Input.text.toString() != "" &&
                binding.p3Input.text.toString() != "" &&
                binding.p4Input.text.toString() != ""
            ) {


                viewModel.add_food(
                    binding.nameField.text.toString(),
                    binding.p1Input.text.toString().toInt(),
                    binding.p2Input.text.toString().toInt(),
                    binding.p3Input.text.toString().toInt(),
                    binding.p4Input.text.toString().toInt()
                )
                recycleOfToday.adapter =
                    RecycleVievAdapter(viewModel.FoodArray.value ?: listOf(), viewModel)


                val requester = HTTPRequester()
                requester.AddBarcode(
                    context, Barcode(
                        barcode,
                        binding.nameField.text.toString(),
                        binding.p1Input.text.toString().toInt(),
                        binding.p2Input.text.toString().toInt(),
                        binding.p3Input.text.toString().toInt(),
                        binding.p4Input.text.toString().toInt()
                    )
                )


            } else {
                show_wrong_input_alert()

            }
            alertDialog.dismiss()
        }
        binding.noButton.setOnClickListener() {
            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    fun show_wrong_input_alert() {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Ошибка")
        builder.setMessage("Заполните все окна")
        builder.setPositiveButton("Ок") { dialog, which ->
            dialog.dismiss()
        }
        builder.setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()
    }


}