package ch.heigvd.iict.sym.labo03.ui.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.sym.labo03.databinding.FragmentNfcBinding

class NfcFragment : Fragment() {

    private lateinit var nfcViewModel: NfcViewModel
    private var _binding: FragmentNfcBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nfcViewModel =
            ViewModelProvider(this).get(NfcViewModel::class.java)

        _binding = FragmentNfcBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNfc
        nfcViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}