package com.julkar.nain.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.julkar.nain.currencyconverter.R
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.main_activity.view.*

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */

class CurrencyRatesAdapter : RecyclerView.Adapter<CurrencyRatesAdapter.ViewHolder>(){

    private var list = listOf<String>()

    fun updateList(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.setText(list[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val text = view.rates
    }
}
