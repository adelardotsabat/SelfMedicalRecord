package com.example.selfmedicalrecord.ui.auth

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.selfmedicalrecord.R
import com.example.selfmedicalrecord.databinding.ActivityForgotPasswordBinding
import com.example.selfmedicalrecord.utils.applySystemBarInsetsAsPadding
import com.example.selfmedicalrecord.utils.hideKeyboard
import com.example.selfmedicalrecord.utils.showMessage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/** Meminta tautan pengaturan ulang kata sandi lewat email. Dibuka dari LoginActivity. */
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsetsAsPadding()

        // Isi otomatis dengan email yang sudah diketik di layar Login (jika ada).
        if (savedInstanceState == null) {
            binding.etEmail.setText(intent.getStringExtra(EXTRA_EMAIL).orEmpty().trim())
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.tvBackToLogin.setOnClickListener { finish() }
        binding.btnSend.setOnClickListener { submit() }
        binding.etEmail.doAfterTextChanged { viewModel.onInputChanged() }
        binding.etEmail.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect { render(it) } }
                launch { viewModel.events.collect { showSentDialog(it.email) } }
            }
        }
    }

    private fun submit() {
        hideKeyboard()
        viewModel.sendLink(binding.etEmail.text?.toString().orEmpty())
    }

    private fun render(state: ForgotPasswordUiState) {
        binding.tilEmail.error = state.emailError?.let { getString(it) }
        binding.tvFormError.showMessage(state.formError)
        binding.btnSend.isEnabled = !state.isLoading
        binding.btnSend.setText(if (state.isLoading) R.string.action_loading else R.string.forgot_send)
    }

    private fun showSentDialog(email: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.forgot_sent_title)
            .setMessage(getString(R.string.forgot_sent_message, email))
            .setCancelable(false)
            .setPositiveButton(R.string.action_ok) { _, _ -> finish() }
            .show()
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}
