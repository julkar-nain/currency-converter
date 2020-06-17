package com.julkar.nain.currencyconverter.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.julkar.nain.currencyconverter.R
import com.julkar.nain.currencyconverter.adapter.CurrencyRatesAdapter
import com.julkar.nain.currencyconverter.application.MainApplication
import com.julkar.nain.currencyconverter.databinding.MainActivityBinding
import com.julkar.nain.currencyconverter.main.vm.MainViewModel
import com.julkar.nain.currencyconverter.util.Action
import com.julkar.nain.currencyconverter.util.view.TextChangeWatcher
import kotlinx.android.synthetic.main.main_activity.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = application as MainApplication
        appComponent.getAppComponent()?.getMainSubComponent()?.create()?.inject(this)

        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.main_activity
        )

        viewModel = ViewModelProvider(this, modelFactory).get(MainViewModel::class.java)

        viewModel.registerScheduler()

        viewModel.fetchCurrencyData().observe(this,
            Observer { list ->
                list?.let {
                    val list = it.keys.toList()
                    bindRatesSpinnerTo(list)
                    bindRatesSpinnerFrom(list)
                    adapter.updateList(viewModel.getExchangeRates())
                }
            })

        viewModel.textFrom.observe(this, Observer {
            updateCurrencyToInputField(editTextFrom, it)
        })

        viewModel.textTo.observe(this, Observer {
            updateCurrencyToInputField(editTextTo, it)
        })

        textChangedListenerTo = object : TextWatcher by TextChangeWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.convertCurrency(Action.TO, s.toString())
            }
        }

        textChangedListenerFrom = object : TextWatcher by TextChangeWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.convertCurrency(Action.FROM, s.toString())
            }
        }
        editTextFrom.addTextChangedListener(textChangedListenerFrom)
        editTextTo.addTextChangedListener(textChangedListenerTo)

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

    private fun updateCurrencyToInputField(editText: EditText, text: String) {
        val listener = getListener(editText)
        editText.removeTextChangedListener(listener)
        editText.setText(text)
        editText.setSelection(editText.text.length)
        editText.addTextChangedListener(listener)
    }

    private fun getListener(editText: EditText): TextWatcher {
        if (editText == viewBinding.editTextFrom) {
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
            viewModel.setCurrencyRate(Action.TO, position)
        } else if (parent?.id == R.id.spinnerFrom) {
            viewModel.setCurrencyRate(Action.FROM, position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }
}