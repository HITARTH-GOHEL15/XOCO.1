package com.app.movieexplorer2.ui.auth.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movieexplorer2.databinding.ItemLoginBinding
import com.app.movieexplorer2.databinding.ItemRegisterBinding
import com.app.xoco2.data.models.RegisterRequest

class RegisterAdapter(
    private val onLoginClick: () -> Unit,
    val onVerifyClick: () -> Unit,
    val onRegisterClick: (RegisterRequest) -> Unit
) : RecyclerView.Adapter<RegisterAdapter.RegisterHolder>() {

    private var item: RegisterRequest? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegisterHolder {
        val binding = ItemRegisterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RegisterHolder(
            binding,
            onLoginClick,
            onRegisterClick,
            onVerifyClick
        )
    }

    override fun onBindViewHolder(
        holder: RegisterHolder,
        position: Int
    ) {
        item?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
       return 1
    }


    class RegisterHolder(
        val binding: ItemRegisterBinding,
        private val onLoginClick: () -> Unit,
        val onRegisterClick: (RegisterRequest) -> Unit,
        val onVerifyClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.RegisterButton.setOnClickListener {
                val fullNameText = binding.fullNameTextField.editText?.text.toString().trim()
                val emailText = binding.emailTextField.editText?.text.toString().trim()
                val passwordText = binding.passwordTextField.editText?.text.toString().trim()

                // 2. Simple validation check before passing data
                if (emailText.isEmpty()) {
                    binding.emailTextField.error = "Email is required"
                    return@setOnClickListener
                }

                // 3. Construct the request entity and trigger the callback
                val requestPayload = RegisterRequest(
                    fullName = fullNameText,
                    email = emailText,
                    password = passwordText
                )
                onRegisterClick(requestPayload)
            }
            binding.loginButton.setOnClickListener {
                onLoginClick()
            }
            binding.otpButton.setOnClickListener {
                onVerifyClick()
            }
        }

        fun bind(item: RegisterRequest) {
            binding.fullNameTextField.editText?.setText(item.fullName)
            binding.emailTextField.editText?.setText(item.email)
            binding.passwordTextField.editText?.setText(item.password)
        }
    }
}