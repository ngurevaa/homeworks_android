package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import ru.kpfu.itis.gureva.homeworks_android.adapter.ViewPagerAdapter
import ru.kpfu.itis.gureva.homeworks_android.adapter.QuestionnaireAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentViewPagerBinding


class ViewPagerFragment(private val questionsNumber: Int) : Fragment(R.layout.fragment_view_pager) {
    private var binding: FragmentViewPagerBinding? = null
    private var vpAdapter: FragmentStateAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)

        // ----------------------------------------------------------------------------------------
        //обязательная часть задания
        vpAdapter = QuestionnaireAdapter(questionsNumber, parentFragmentManager, lifecycle)
        binding?.vpQuestionnaire?.adapter = vpAdapter

        var count: Int = (vpAdapter as QuestionnaireAdapter).itemCount
        Answers.answers = BooleanArray(count)
        for (i in 0 until count) {
            Answers.answers[i] = false
        }
        // ----------------------------------------------------------------------------------------
        // опциональная часть 2 задание
        Fragments.list = arrayListOf()

        Fragments.list.add(QuestionnaireFragment.newInstance(1, questionsNumber))
        for (i in 1..questionsNumber) {
            Fragments.list.add(QuestionnaireFragment.newInstance(i + 1, questionsNumber))
        }
        Fragments.list.add(QuestionnaireFragment.newInstance(questionsNumber, questionsNumber))

        vpAdapter = ViewPagerAdapter(Fragments.list, parentFragmentManager, lifecycle)
        binding?.vpQuestionnaire?.adapter = vpAdapter

        binding?.run {
            vpQuestionnaire.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                private var myState = 0
                private var currentPosition = 0
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    if (myState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position && currentPosition == 0) {
                        vpQuestionnaire.currentItem = questionsNumber - 1
                    }
                    else if (myState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position && currentPosition == questionsNumber - 1) {
                        vpQuestionnaire.currentItem = 0
                    }
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    currentPosition = position
                    super.onPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    myState = state
                    super.onPageScrollStateChanged(state)
                }
            })
        }

        count = (vpAdapter as ViewPagerAdapter).itemCount
        Answers.answers = BooleanArray(count)
        for (i in 0 until count) {
            Answers.answers[i] = false
        }
        // ----------------------------------------------------------------------------------------
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
