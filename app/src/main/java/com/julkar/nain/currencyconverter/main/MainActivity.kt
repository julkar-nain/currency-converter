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
import androidx.recyclerview.widget.GridLayoutManager
import com.julkar.nain.currencyconverter.R
import com.julkar.nain.currencyconverter.adapter.CurrencyRatesAdapter
import com.julkar.nain.currencyconverter.application.MainApplication
import com.julkar.nain.currencyconverter.databinding.MainActivityBinding
import com.julkar.nain.currencyconverter.main.vm.MainViewModel
import com.julkar.nain.currencyconverter.util.view.TextChangeWatcher
import kotlinx.android.synthetic.main.main_activity.*
import java.text.DecimalFormat
import javax.inject.Inject


class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: CurrencyRatesAdapter

    private lateinit var viewModel: MainViewModel
    private lateinit var viewBinding: MainActivityBinding
    private lateinit var textChangedListenerTo: TextWatcher
    private lateinit var textChangedListenerFrom: TextWatcher
    private var countryNameFrom: String = "USD"
    private var countryNameTo: String = "USD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = application as MainApplication
        appComponent.getAppComponent()?.getMainSubComponent()?.create()?.inject(this)

        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.main_activity
        )

        viewModel = ViewModelProvider(this, modelFactory).get(MainViewModel::class.java)

//        viewBinding.vm = viewModel

        viewModel.fetchCurrencyData().observe(this,
            androidx.lifecycle.Observer { list ->
                list?.let {
                    val list = it.keys.toList()
                    bindRatesSpinnerTo(list)
                    bindRatesSpinnerFrom(list)
                    adapter.updateList(viewModel.getExchangeRates())
                }
            })

        textChangedListenerTo = object : TextWatcher by TextChangeWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertRates(editTextTo, editTextFrom, s.toString(),countryNameTo, countryNameFrom, this)
            }
        }

        textChangedListenerFrom = object : TextWatcher by TextChangeWatcher{
            override fun afterTextChanged(s: Editable?) {
                convertRates(editTextFrom, editTextTo, s.toString(),countryNameFrom, countryNameTo, this)
            }
        }
        viewBinding.editTextFrom.addTextChangedListener(textChangedListenerFrom)
        viewBinding.editTextTo.addTextChangedListener(textChangedListenerTo)

        setupCurrencyRatesRecyclerview()
    }

    fun bindRatesSpinnerTo(list: List<String>) {
        viewBinding.spinnerTo.adapter = getSpinnerAdapter(list)
        viewBinding.spinnerTo.onItemSelectedListener = this
    }

    fun bindRatesSpinnerFrom(list: List<String>) {
        viewBinding.spinnerFrom.adapter = getSpinnerAdapter(list)
        viewBinding.spinnerFrom.onItemSelectedListener = this
    }

    private fun convertRates(edtFrom: EditText, edtTo: EditText, amountText: String,
                             nameFrom: String, nameTo:String, listener: TextWatcher) {
        val amountFrom = amountText.replace(",", "").toDoubleOrNull()

        amountFrom?.let {
            updateCurrencyToInputField(
                edtFrom,
                amountFrom,
                "#,###",
                listener
            )
            updateCurrencyToInputField(
                edtTo, viewModel.getExchangedAmount(
                    amountFrom.toString().toDouble(),
                    from = nameFrom,
                    to = nameTo
                ), "#,###.00", getOtherListener(listener)
            )
        } ?: if (!TextUtils.isEmpty(edtTo.text)) {
            edtTo.setText("")
        }
    }

    private fun updateCurrencyToInputField(editText: EditText, amount: Double, format: String, listener: TextWatcher) {
        editText.removeTextChangedListener(listener)
        editText.setText(DecimalFormat(format).format(amount).toString())
        editText.setSelection(editText.text.length)
        editText.addTextChangedListener(listener)
    }

    private fun getOtherListener(listener: TextWatcher): TextWatcher {
        if (listener == textChangedListenerTo) {
            return textChangedListenerFrom
        }

        return textChangedListenerTo
    }

    private fun setupCurrencyRatesRecyclerview() {
        viewBinding.recyclerviewExchangeRates.layoutManager = GridLayoutManager(this, 2)
        viewBinding.recyclerviewExchangeRates.adapter = adapter
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
            countryNameTo = viewModel.getCurrencyRate(position)
        } else if (parent?.id == R.id.spinnerFrom) {
            countryNameFrom = viewModel.getCurrencyRate(position)
        }
        resetInput()
    }

    private fun resetInput() {
        viewBinding.editTextTo.text = null
        viewBinding.editTextFrom.text = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }
}