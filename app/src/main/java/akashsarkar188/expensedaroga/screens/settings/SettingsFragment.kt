package akashsarkar188.expensedaroga.screens.settings

import akashsarkar188.expensedaroga.R
import akashsarkar188.expensedaroga.databinding.FragmentSettingsBinding
import akashsarkar188.expensedaroga.services.SharedPreferenceHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        initView()

        return binding?.root
    }

    private fun initView() {
        binding?.apply {
            bubbleToggle.isChecked = SharedPreferenceHelper.shouldBubble()

            bubbleToggle.setOnCheckedChangeListener { compoundButton, checked ->
                SharedPreferenceHelper.setShouldBubble(checked)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}