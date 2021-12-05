package ch.heigvd.iict.sym.labo03.ui.i_beacon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.sym.labo03.databinding.FragmentIBeaconBinding

class IBeaconFragment : Fragment() {

    private lateinit var IBeaconViewModel: IBeaconViewModel
    private var _binding: FragmentIBeaconBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        IBeaconViewModel =
            ViewModelProvider(this).get(IBeaconViewModel::class.java)

        _binding = FragmentIBeaconBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        IBeaconViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}