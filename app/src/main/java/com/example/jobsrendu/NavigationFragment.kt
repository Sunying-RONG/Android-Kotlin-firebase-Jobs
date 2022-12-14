package com.example.jobsrendu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
/**
 * A simple [Fragment] subclass.
 * Use the [NavigationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationFragment : Fragment(), AdapterView.OnItemSelectedListener{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvGpsLocation: TextView
    private lateinit var userName: TextView
    lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = context?.getSharedPreferences("login_user", 0)!!
        Log.d("fragment login", sharedPreferences.getString("login", "").toString())
        Log.d("fragment user_id", sharedPreferences.getString("user_id", "").toString())
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_navigation, container, false)
        tvGpsLocation = view.findViewById<TextView>(R.id.tv_countryName)

        val to_job_search = view.findViewById<TextView>(R.id.to_job_search)
        to_job_search.setOnClickListener {
            val intentToJobSearch = Intent(requireContext(), MainActivity::class.java)
            startActivity(intentToJobSearch)
        }
        val role = sharedPreferences.getString("role", "").toString()
        val user_name_sign_in = view.findViewById<TextView>(R.id.user_name_sign_in)
        val user_name_default = user_name_sign_in.text.toString()
        user_name_sign_in.setOnClickListener {
            var intentTo : Intent? = null
            when(role) {
                "Job seeker" -> {intentTo = Intent(requireContext(), JobseekerActivity::class.java)}
                "Employer" -> {intentTo = Intent(requireContext(), EmployerActivity::class.java)}
                "Agency" -> {intentTo = Intent(requireContext(), AgencyActivity::class.java)}
                "Administrator" -> {intentTo = Intent(requireContext(), AdminActivity::class.java)}
            }
            startActivity(intentTo)
        }
        if (!arguments?.getString("country_name").toString().equals("null")) {
        country_name = arguments?.getString("country_name").toString()
        }

        var loginValue = sharedPreferences.getString("login", user_name_default)
        if (!loginValue.equals("null")) {
            var loginName = sharedPreferences?.getString("login", user_name_default).toString()
            user_name_sign_in.text = loginName
        } else {
            user_name_sign_in.text = user_name_default
        }

        tvGpsLocation.text = country_name

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
        var editor = sharedPreferences.edit()
        when (p2) {
            1 -> {
                editor.putString("role", "Job seeker")
            }
            2 -> {
                editor.putString("role", "Employer")
            }
            3 -> {
                editor.putString("role", "Agency")
            }
            4 -> {
                editor.putString("role", "Administrator")
            }
        }
        editor.commit()

        if (formerSelect != p2) {
            editor.remove("login")
            editor.remove("user_id")
            editor.commit()
            startActivity(intent)
        }
        formerSelect = p2
        Log.d("formerSelect", formerSelect.toString())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}