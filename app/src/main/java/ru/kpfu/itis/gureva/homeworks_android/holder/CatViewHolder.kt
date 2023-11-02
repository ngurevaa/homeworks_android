package ru.kpfu.itis.gureva.homeworks_android.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemCatBinding
import ru.kpfu.itis.gureva.homeworks_android.model.Cat

class CatViewHolder(
    private val binding: ItemCatBinding,
    private val itemCount: Int,
    private val onLikeClicked: ((Int, Cat) -> Unit),
    private val onCatClicked: ((CatViewHolder, Int) -> Unit),
    private val onCatLongClicked: ((ItemCatBinding, Boolean) -> Unit),
    private val onDeleteClicked: ((Int) -> Unit),
    private val glide: RequestManager
) : ViewHolder(binding.root) {
    private var cat: Cat? = null
    private var image: ImageView
    private var repeat = false

    fun getImage() = image

    init {
        binding.root.setOnClickListener {
            onCatClicked(this, adapterPosition - adapterPosition / 9 - 1)
        }

        binding.run {
            ivLike.setOnClickListener {
                cat?.let {
                    it.like = !it.like
                    onLikeClicked(adapterPosition, it)
                }
            }

            image = ivCat

            if (itemCount > 12) {
                root.setOnLongClickListener {
                    repeat = !repeat
                    onCatLongClicked(binding, repeat)
                    return@setOnLongClickListener true
                }

                ivDelete.setOnClickListener {
                    onDeleteClicked(adapterPosition)
                }
            }
        }
    }

    fun onBind(cat: Cat) {
        binding.run {
            ivCat.visibility = View.VISIBLE
            tvCat.visibility = View.VISIBLE
            ivLike.visibility = View.VISIBLE
            ivDelete.visibility = View.INVISIBLE
        }

        this.cat = cat
        binding.run {
            tvCat.text = cat.type
            glide.load(cat.image).into(ivCat)
        }
        changeLikeStatus(cat.like)
    }

    fun changeLikeStatus(like: Boolean) {
        val likeDrawable = if (like) R.drawable.full_heart else R.drawable.empty_heart
        binding.ivLike.setImageResource(likeDrawable)
    }
}
