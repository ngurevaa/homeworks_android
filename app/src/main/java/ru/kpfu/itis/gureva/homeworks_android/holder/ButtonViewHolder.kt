package ru.kpfu.itis.gureva.homeworks_android.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemButtonBinding

class ButtonViewHolder(
    private val binding: ItemButtonBinding,
    private val onButtonClicked: ((Int) -> Unit)
) : ViewHolder(binding.root) {

    init {
        binding.btnStart.setOnClickListener {
            onButtonClicked(adapterPosition)
        }
    }
    fun onBind() {
        binding.btnStart.text = "haha"
    }
}
