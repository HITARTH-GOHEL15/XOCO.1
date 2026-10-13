package com.app.movieexplorer2.ui.auth.phone_firebase

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentLoginPhoneBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class PhoneAuthFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginPhoneBinding? = null
    private val binding: FragmentLoginPhoneBinding
        get() = _binding!!

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneloginTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.startVerifyButton.setOnClickListener {
            val phoneNumber =
                binding.phoneTextField.editText?.text?.toString()?.trim().orEmpty()

            if(TextUtils.isEmpty(phoneNumber)) {
                binding.phoneTextField.error = "Cannot be empty."
                return@setOnClickListener
            }
            if (!validatePhoneNumber()) {
                return@setOnClickListener
            }
            startPhoneNumberVerification(phoneNumber)
        }

        binding.verifyButton.setOnClickListener {

            val code =
                binding.otpTextField.editText?.text?.toString()?.trim().orEmpty()

            if (TextUtils.isEmpty(code)) {
                binding.otpTextField.error = "Cannot be empty."
                return@setOnClickListener
            }
            verifyPhoneNumberWithCode(storedVerificationId, code)
        }

        binding.resendButton.setOnClickListener {
            if (!validatePhoneNumber()) {
                return@setOnClickListener
            }
            resendVerificationCode(binding.phoneTextField.editText.toString(), resendToken)
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize phone auth callbacks
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                verificationInProgress = false

                // Update the UI and attempt sign in with the phone credential
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                verificationInProgress = false

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    binding.phoneTextField.error = "Invalid phone number."
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(
                        view,
                        "Quota exceeded.",
                        Snackbar.LENGTH_SHORT,
                    ).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { savedState ->
            verificationInProgress = savedState.getBoolean(KEY_VERIFY_IN_PROGRESS)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    setOf(
                        Toast.makeText(
                            requireContext(),
                            "User Login successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                    findNavController().navigate(
                        R.id.action_phoneAuthFragment_to_homeFragment
                    )
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(
                        requireContext(),
                        "User Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "User Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Update UI
                }
            }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = binding.phoneTextField.editText.toString()
        if(TextUtils.isEmpty(phoneNumber)) {
            binding.phoneTextField.error = "Invalid phone number."
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PhoneAuthFragment"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6
    }

}