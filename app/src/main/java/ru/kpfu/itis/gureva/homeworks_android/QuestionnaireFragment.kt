package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentQuestionnaireBinding

class QuestionnaireFragment : Fragment(R.layout.fragment_questionnaire) {
    private var binding: FragmentQuestionnaireBinding? = null
    private var itemCount: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionnaireBinding.bind(view)

        itemCount = arguments?.getInt(ARG_ITEM_COUNT)
        val questionNumber = arguments?.getInt(ARG_QUESTION_NUMBER)

        binding?.run {

            if (itemCount == questionNumber) {
                btnSave.isVisible = true
                checkAnswers()
            }

            val questions = questionNumber!! % Questions.list.size
            val question = Questions.list[questions]

            tvQuestionNumber.text = questionNumber.toString()
            tvQuestion.text = question.question

            val listOfAns = listOf(question.answer1, question.answer2, question.answer3, question.answer4, question.answer5)
            val listOfAnswers = listOf(tvFirst, tvSecond, tvThird, tvFourth, tvFifth)
            for (i in 0..4) {
                listOfAnswers[i].text = listOfAns[i]
            }

            val listOfButtons = listOf(rbFirst, rbSecond, rbThird, rbFourth, rbFifth)
            if (savedInstanceState != null) {
                val index = savedInstanceState.getInt("indexCheck")
                listOfButtons[index].isChecked = true
            }

            for (i in 0..4) {
                listOfButtons[i].setOnClickListener {
                    listOfButtons.forEach {
                        it.isChecked = false
                    }
                    listOfButtons[i].isChecked = true

                    Answers.answers[questionNumber - 1] = true

                    if (itemCount == questionNumber) {
                        checkAnswers()
                    }

                    //-----------------------------------------------------------------------------
                    // дополнение к опциональному заданию 2
                    var answers = 0
                    Answers.answers.forEach {
                        if (it) {
                            answers++
                        }
                    }
                    if (itemCount == answers) {
                        Fragments.list.forEach {
                            it.binding?.btnSave?.isVisible = true
                            it.binding?.btnSave?.isEnabled = true
                        }
                    }
                    //-----------------------------------------------------------------------------
                }
            }

            btnSave.setOnClickListener {
                Snackbar.make(root, "Successful completion of the test", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAnswers() {
        var answers = 0
        Answers.answers.forEach {
            if (it) {
                answers++
            }
        }

        if (answers == itemCount) {
            binding?.btnSave?.isEnabled = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding?.run {
            val listOfButtons = arrayListOf(rbFirst, rbSecond, rbThird, rbFourth, rbFifth)
            for (i in 0..4) {
                if (listOfButtons[i].isChecked) {
                    outState.putInt("indexCheck", i)
                    break
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_QUESTION_NUMBER = "arg_question_number"
        private const val ARG_ITEM_COUNT = "arg_item_count"
        fun newInstance(questionNumber: Int, itemCount: Int) = QuestionnaireFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_QUESTION_NUMBER, questionNumber)
                putInt(ARG_ITEM_COUNT, itemCount)
            }
        }
    }
}
