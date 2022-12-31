package com.example.jobsrendu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var country_name = "Country"
var user_name = "Sign in"
/**
 * A simple [Fragment] subclass.
 * Use the [NavigationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationFragment : Fragment(), AdapterView.OnItemSelectedListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvGpsLocation: TextView
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_navigation, container, false)
        tvGpsLocation = view.findViewById<TextView>(R.id.tv_countryName)
        userName = view.findViewById<TextView>(R.id.user_name_sign_in)
        val bundle = arguments
        if (!bundle?.getString("country_name").toString().equals("null")) {
            country_name = bundle?.getString("country_name").toString()
        }
        if (!bundle?.getString("user_name").toString().equals("null")) {
            user_name = bundle?.getString("user_name").toString()
        }
        tvGpsLocation.text = country_name
        userName.text = user_name

        val spinner: Spinner = view.findViewById(R.id.roles_spinner)
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.roles_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavigationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavigationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var formerSelect = 0;
        val intent = Intent(requireContext(), SigninActivity::class.java)

        when (p2) {
            1 -> {
                intent.putExtra("role", "Job seeker")
                Log.d("select","Job seeker")
            }
            2 -> {
                intent.putExtra("role", "Employer")
                Log.d("select","Employer")
            }
            3 -> {
                intent.putExtra("role", "Agency")
                Log.d("select","Agency")
            }
            4 -> {
                intent.putExtra("role", "Administrator")
                Log.d("select","Administrator")
            }
        }
        if (formerSelect != p2) {
            startActivity(intent)
        }
        formerSelect = p2
        Log.d("formerSelect", formerSelect.toString())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}