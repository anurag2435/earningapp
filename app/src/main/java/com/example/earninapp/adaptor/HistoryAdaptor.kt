package com.example.earninapp.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earninapp.databinding.HistoryitemBinding
import com.example.earninapp.model.HistoryModelClass
import java.sql.Date
import java.sql.Timestamp

class HistoryAdaptor(var listHistory: ArrayList<HistoryModelClass>) :
    RecyclerView.Adapter<HistoryAdaptor.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
        return HistoryCoinViewHolder(
            HistoryitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listHistory.size
    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {

        var timeStamp = Timestamp(listHistory.get(position).timeAndDate.toLong())
        holder.binding.Time.text = Date(timeStamp.time).toString()
        holder.binding.status.text = if(listHistory.get(position).isWithdrawal){"-Money Withdrawal"}else{"+ Money Added"}
        holder.binding.Coin1.text = listHistory[position].coin

    }

}