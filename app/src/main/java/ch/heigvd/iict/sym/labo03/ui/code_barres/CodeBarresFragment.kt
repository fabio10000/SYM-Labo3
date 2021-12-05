package ch.heigvd.iict.sym.labo03.ui.code_barres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.sym.labo03.databinding.FragmentCodeBarresBinding

class CodeBarresFragment : Fragment() {

    private lateinit var codeBarresViewModel: CodeBarresViewModel
    private var _binding: FragmentCodeBarresBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        codeBarresViewModel =
            ViewModelProvider(this).get(CodeBarresViewModel::class.java)

        _binding = FragmentCodeBarresBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCodeBarres
        codeBarresViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}