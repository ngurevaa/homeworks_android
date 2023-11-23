package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentCoroutinesBinding
import ru.kpfu.itis.gureva.homeworks_android.listener.CoroutineListener
import ru.kpfu.itis.gureva.homeworks_android.listener.ReceiveListener
import ru.kpfu.itis.gureva.homeworks_android.util.AirplaneModeUtil

class CoroutinesFragment : Fragment(R.layout.fragment_coroutines) {
    private var binding: FragmentCoroutinesBinding? = null
    private var airplaneModeUtil: AirplaneModeUtil? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoroutinesBinding.bind(view)

        airplaneModeUtil = AirplaneModeUtil(requireContext()).also {
            (activity as? ReceiveListener)?.setOnReceiveListener(it.getIntentAirplaneMode())
        }

        binding?.run {
            updateSettings()

            sliderCount.addOnChangeListener { _, value, _ ->
                count = value.toInt()
            }

            cbAsync.setOnCheckedChangeListener { _, isChecked ->
                async = isChecked
            }

            cbStop.setOnCheckedChangeListener { _, isChecked ->
                stopOnBackground = isChecked
            }

            btnStart.setOnClickListener {
                (activity as? CoroutineListener)?.onCreateCoroutineListener()
            }
        }
    }

    private fun updateSettings() {
        binding?.run {
            sliderCount.value = count.toFloat()
            cbAsync.isChecked = async
            cbStop.isChecked = stopOnBackground
        }
    }

    companion object {
        var count = 0
        var async = false
        var stopOnBackground = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        airplaneModeUtil = null
    }
}
