package ru.kpfu.itis.gureva.homeworks_android.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemDataBinding
import java.text.SimpleDateFormat
import java.util.Date

class DataViewHolder(
    private val binding: ItemDataBinding
) : ViewHolder(binding.root) {

    fun onBind() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.tvData.text = currentDate
    }
}
