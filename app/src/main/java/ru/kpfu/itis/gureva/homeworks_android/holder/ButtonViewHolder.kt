package ru.kpfu.itis.gureva.homeworks_android.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemButtonBinding

class ButtonViewHolder(
    private val binding: ItemButtonBinding,
    private val onButtonClicked: (() -> Unit)
) : ViewHolder(binding.root) {

    init {
        binding.btnStart.setOnClickListener {
            onButtonClicked()
        }
    }
    fun onBind() {
        binding.btnStart.text = binding.root.context.getString(R.string.btn_new_cats)
    }
}
