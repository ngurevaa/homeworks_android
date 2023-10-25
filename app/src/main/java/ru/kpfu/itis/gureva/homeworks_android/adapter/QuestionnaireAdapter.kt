package ru.kpfu.itis.gureva.homeworks_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.gureva.homeworks_android.QuestionnaireFragment
import ru.kpfu.itis.gureva.homeworks_android.ViewPagerFragment

class QuestionnaireAdapter(private val questionsNumber: Int, manager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount(): Int {
        return questionsNumber
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionnaireFragment.newInstance(position + 1, itemCount)
    }
}
