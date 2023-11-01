package ru.kpfu.itis.gureva.homeworks_android.holder

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemCatBinding
import ru.kpfu.itis.gureva.homeworks_android.model.Cat

class CatViewHolder(
    private val binding: ItemCatBinding,
    private val onLikeClicked: ((Int, Cat) -> Unit),
    private val onCatClicked: ((CatViewHolder, Int) -> Unit)
) : ViewHolder(binding.root) {
    private var cat: Cat? = null
    var image: ImageView


    init {
        binding.root.setOnClickListener {
            onCatClicked(this, adapterPosition - adapterPosition / 9 - 1)
        }

        binding.ivLike.setOnClickListener {
            this.cat?.let {
                it.like = !it.like
                onLikeClicked(adapterPosition, it)
            }
        }

        image = binding.ivCat
    }

    fun onBind(cat: Cat) {
        this.cat = cat
        binding.run {
            tvCat.text = cat.type
            ivCat.setImageResource(cat.image)
        }
        changeLikeStatus(cat.like)
    }

    fun changeLikeStatus(like: Boolean) {
        val likeDrawable = if (like) R.drawable.full_heart else R.drawable.empty_heart
        binding.ivLike.setImageResource(likeDrawable)
    }
}
