package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.gureva.homeworks_android.adapter.QuestionnaireAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentViewPagerBinding

class ViewPagerFragment(private val questionsNumber: Int) : Fragment(R.layout.fragment_view_pager) {
    private var binding: FragmentViewPagerBinding? = null
    private var vpAdapter: FragmentStateAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)

        vpAdapter = QuestionnaireAdapter(questionsNumber, parentFragmentManager, lifecycle)
        binding?.vpQuestionnaire?.adapter = vpAdapter

       val count: Int = (vpAdapter as QuestionnaireAdapter).itemCount
        Answers.answers = BooleanArray(count)
        for (i in 0 until count) {
            Answers.answers[i] = false
        }
    }
}
