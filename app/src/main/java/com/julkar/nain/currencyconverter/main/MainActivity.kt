package com.julkar.nain.currencyconverter.main

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.julkar.nain.currencyconverter.R
import com.julkar.nain.currencyconverter.databinding.MainActivityBinding
import com.julkar.nain.currencyconverter.main.ui.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewBinding: MainActivityBinding
    private lateinit var textChangedListenerTo: TextWatcher
    private lateinit var textChangedListenerFrom: TextWatcher
    private lateinit var countryNameFrom: String
    private lateinit var countryNameTo: String

    private val list1 = listOf("USD", "BDT", "INR", "AUD")
    private val list2 = listOf("USD", "BDT", "INR", "AUD")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this,
            R.layout.main_activity
        )
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bindRatesSpinnerTo(list1)
        bindRatesSpinnerFrom(list2)

        textChangedListenerTo = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertRates(edtFrom = editTextTo, edtTo = editTextFrom, listener = this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        textChangedListenerFrom = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertRates(edtFrom = editTextFrom, edtTo = editTextTo, listener = this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        viewBinding.editTextFrom.addTextChangedListener(textChangedListenerFrom)
        viewBinding.editTextTo.addTextChangedListener(textChangedListenerTo)
    }

    fun bindRatesSpinnerTo(list: List<String>) {
        viewBinding.spinnerTo.adapter = getSpinnerAdapter(list)
        viewBinding.spinnerTo.onItemSelectedListener = this
    }

    fun bindRatesSpinnerFrom(list: List<String>) {
        viewBinding.spinnerFrom.adapter = getSpinnerAdapter(list)
        viewBinding.spinnerFrom.onItemSelectedListener = this
    }

    private fun convertRates(edtFrom: EditText, edtTo: EditText, listener: TextWatcher) {
        val amountFrom = edtFrom.text.toString().toDoubleOrNull()

        amountFrom?.let {
            edtTo.removeTextChangedListener(getOtherListener(listener))
            //            edtTo.text = viewModel.getConvertedRates(from, to, amount)
            edtTo.setText(amountFrom.toString())
            edtTo.setSelection(edtTo.text.length) //moves the pointer to end
            edtTo.addTextChangedListener(getOtherListener(listener))
        }?: if(!TextUtils.isEmpty(editTextTo.text)){edtTo.setText("")}
    }

    private fun getOtherListener(listener: TextWatcher): TextWatcher {
        if (listener == textChangedListenerTo) {
            return textChangedListenerFrom
        }

        return textChangedListenerTo
    }

    private fun getSpinnerAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(
            this
            , android.R.layout.simple_spinner_item
            , list
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return adapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent?.id == R.id.spinnerTo) {
            countryNameTo = list1[position]
        } else if (parent?.id == R.id.spinnerFrom) {
            countryNameFrom = list2[position]
        }
        resetInput()
    }

    fun resetInput(){
        viewBinding.editTextTo.text = null
        viewBinding.editTextFrom.text = null
    }
}