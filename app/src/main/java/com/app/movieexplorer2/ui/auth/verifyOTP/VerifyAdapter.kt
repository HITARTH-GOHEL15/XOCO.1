package com.app.movieexplorer2.ui.auth.verifyOTP

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movieexplorer2.data.remote.models.OtpVerifyRequest
import com.app.movieexplorer2.databinding.FragmentVerifyBinding
import com.app.movieexplorer2.databinding.ItemRegisterBinding

class VerifyAdapter(
    private val onVerifyClick: (OtpVerifyRequest) -> Unit
) : RecyclerView.Adapter<VerifyAdapter.VerifyHolder>() {

    private var item: OtpVerifyRequest? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerifyHolder {
        val binding = FragmentVerifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VerifyHolder(
            binding,
            onVerifyClick
        )
    }

    override fun onBindViewHolder(
        holder: VerifyHolder,
        position: Int
    ) {
        item?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class VerifyHolder(
        val binding: FragmentVerifyBinding,
        private val onVerifyClick: (OtpVerifyRequest) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.verifyButton.setOnClickListener {
                val email = binding.emailTextField.editText?.text.toString().trim()
                val otp = binding.otpTextField.editText?.text.toString().trim()

                if (email.isEmpty()) {
                    binding.emailTextField.error = "Email is required"
                    return@setOnClickListener
                }

                if (otp.isEmpty()) {
                    binding.otpTextField.error = "Otp is required"
                    return@setOnClickListener
                }

                val requestPayload = OtpVerifyRequest(
                    email,
                    otp
                )
                onVerifyClick(requestPayload)
            }
        }

        fun bind(item: OtpVerifyRequest) {
            binding.emailTextField.editText?.setText(item.email)
            binding.otpTextField.editText?.setText(item.otp)
        }
    }
}