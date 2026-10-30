package com.example.selfmedicalrecord.ui.profil

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.selfmedicalrecord.R
import com.example.selfmedicalrecord.databinding.ActivityKataSandiBinding
import com.example.selfmedicalrecord.utils.applySystemBarInsetsAsPadding
import com.example.selfmedicalrecord.utils.hideKeyboard
import com.example.selfmedicalrecord.utils.showMessage
import kotlinx.coroutines.launch

/** Mengganti kata sandi: kata sandi saat ini, baru, dan konfirmasi. */
class KataSandiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKataSandiBinding
    private val viewModel: KataSandiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKataSandiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsetsAsPadding()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { submit() }
        listOf(binding.etCurrent, binding.etNew, binding.etConfirm).forEach {
            it.doAfterTextChanged { viewModel.onInputChanged() }
        }
        binding.etConfirm.setOnEditorActionListener { _, actionId, _ ->
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
                launch {
                    viewModel.saved.collect {
                        Toast.makeText(applicationContext, R.string.security_saved, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun submit() {
        hideKeyboard()
        viewModel.save(
            current = binding.etCurrent.text?.toString().orEmpty(),
            new = binding.etNew.text?.toString().orEmpty(),
            confirm = binding.etConfirm.text?.toString().orEmpty()
        )
    }

    private fun render(state: KataSandiUiState) {
        binding.tilCurrent.error = state.currentError?.let { getString(it) }
        binding.tilNew.error = state.newError?.let { getString(it) }
        binding.tilConfirm.error = state.confirmError?.let { getString(it) }
        binding.tvFormError.showMessage(state.formError)
        binding.btnSave.isEnabled = !state.isLoading
        binding.btnSave.setText(if (state.isLoading) R.string.action_loading else R.string.security_save)
    }
}
