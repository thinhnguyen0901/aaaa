package com.example.viewpager.ui.man2

import android.Manifest
import android.app.Application
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewpager.R
import com.example.viewpager.databinding.FragmentMan2Binding
import com.example.viewpager.databinding.ItemPageBinding
import com.example.viewpager.models.Document
import com.example.viewpager.ui.DocumentAdapter
import com.example.viewpager.ui.DocumentListener
import com.example.viewpager.ui.DocumentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [Man2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Man2Fragment : Fragment() {


    lateinit var binding: FragmentMan2Binding
    var list = mutableListOf<Document>()
    lateinit var viewModel: DocumentViewModel
    private lateinit var _application: Application


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMan2Binding.inflate(layoutInflater)
        _application = requireActivity().application
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        requestRead()
        initData()

    }

    private fun requestRead() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            }
        }
    }

    private fun initData() {
        viewModel.getAllFiles(list, _application)
    }

    private fun initView() {
        val man2Adapter = DocumentAdapter(list, documentListener)
        binding.rvMan2.adapter = man2Adapter
        binding.rvMan2.layoutManager =
            GridLayoutManager(requireContext(), 3)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(_application)
        )[DocumentViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)=== PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = Man2Fragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val documentListener by lazy {
        object : DocumentListener {
            override fun onDetail(document: Document) {
                val mDialog = Dialog(requireContext())
                val binding = ItemPageBinding.inflate(layoutInflater)
                val lp = WindowManager.LayoutParams()

                mDialog.setContentView(binding.root)
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                mDialog.window?.attributes = lp

                with(binding) {
                    tvName.text = document._name
                    tvFormat.text = document._format
                    tvTime.text = document._time
                    tvSize.text = document._size
                    tvPath.text = document._path
                    if (tvFormat.text.toString() == "3") {
                        ivIcon.setImageResource(R.drawable.ic_baseline_all_inbox_24)
                    } else {
                        ivIcon.setImageResource(R.drawable.ic_baseline_airline_seat_recline_extra_24)
                    }
                }
                mDialog.show()
            }
        }
    }
}
