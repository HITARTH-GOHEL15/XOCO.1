package com.app.movieexplorer2.ui.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movieexplorer2.data.remote.models.LoginRequest
import com.app.movieexplorer2.databinding.ItemLoginBinding

class LoginAdapter(
    private val onLoginClick: (LoginRequest) -> Unit,
    private val onGoogleLoginClick: () -> Unit,
    private val onRegisterClick: () -> Unit
) : RecyclerView.Adapter<LoginAdapter.LoginHolder>() {

    private var item: LoginRequest? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoginHolder {
        val binding = ItemLoginBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LoginHolder(
            binding,
            onLoginClick,
            onGoogleLoginClick,
            onRegisterClick
        )
    }


    override fun onBindViewHolder(
        holder: LoginHolder,
        position: Int
    ) {
        item?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }


    class LoginHolder(
        val binding: ItemLoginBinding,
        val onLoginClick: (LoginRequest) -> Unit,
        val onGoogleLoginClick: () -> Unit,
        val onRegisterClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.loginButton.setOnClickListener {
                val emailText = binding.emailTextField.editText?.text.toString().trim()
                val passwordText = binding.passwordTextField.editText?.text.toString().trim()

                if(emailText.isEmpty()) {
                    binding.emailTextField.error = "Email is required"
                    return@setOnClickListener
                }
                if(passwordText.isEmpty()) {
                    binding.passwordTextField.error = "password is required"
                    return@setOnClickListener
                }

                val requestPayload = LoginRequest(
                    emailText,
                    passwordText
                )
                onLoginClick(requestPayload)
            }
            binding.googleSignIn.setOnClickListener {
                onGoogleLoginClick()
            }
            binding.registerButton.setOnClickListener {
                onRegisterClick()
            }
        }

        fun bind(item: LoginRequest) {
            binding.emailTextField.editText?.setText(item.email)
            binding.passwordTextField.editText?.setText(item.password)
        }
    }
}