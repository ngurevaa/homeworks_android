package ru.kpfu.itis.gureva.homeworks_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.gureva.homeworks_android.QuestionnaireFragment

class ViewPagerAdapter(private val list: ArrayList<QuestionnaireFragment>, manager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount(): Int {
        return list.size - 2
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}
