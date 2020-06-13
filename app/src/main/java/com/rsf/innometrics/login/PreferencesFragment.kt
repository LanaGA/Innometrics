package com.rsf.innometrics.login
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.RadioButton
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.wattoit.R
//import com.example.wattoit.data.RegistrationResponse
//import com.example.wattoit.data.RestClient
//import com.example.wattoit.domain.entity.DietBody
//import com.example.wattoit.main.FrontActivity
//import com.example.wattoit.utils.isOkResponseCode
//import kotlinx.android.synthetic.main.fragment_preferences.*
//import org.koin.android.ext.android.inject
//import retrofit2.Call
//import retrofit2.Callback
//
//class PreferencesFragment : Fragment() {
//    private val restClient: RestClient by inject()
//    private val diets: Map<String, String> = mapOf(
//        "Balanced" to "balanced",
//        "High-Fiber" to "high_fiber",
//        "High-Protein" to "high_protein",
//        "Low-Carb" to "low_carb",
//        "Low-Fat" to "low_fat",
//        "Low-Sodium" to "low_sodium",
//        "None" to "none"
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_preferences, container, false)
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        savePreferencesButton.setOnClickListener {
//            val selectedDietId: Int = radioGroup.checkedRadioButtonId
//            val dietRadio: RadioButton = requireActivity().findViewById(selectedDietId)
//            val dietText = dietRadio.text.toString()
//
//            restClient.getApiService(requireActivity().applicationContext)
//                .setDiet(DietBody(diets[dietText])).enqueue(
//                    object : Callback<RegistrationResponse> {
//                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
//                            Toast.makeText(
//                                activity,
//                                "Error while setting diet", Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                        override fun onResponse(
//                            call: Call<RegistrationResponse>,
//                            response: retrofit2.Response<RegistrationResponse>
//                        ) {
//                            if (isOkResponseCode(response.code())) {
//                                val intent = Intent(activity, FrontActivity::class.java).apply {}
//                                activity?.startActivity(intent)
//                            } else {
//                                Toast.makeText(
//                                    activity,
//                                    response.message(), Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    }
//                )
//        }
//
//    }
//}