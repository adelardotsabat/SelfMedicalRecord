package com.example.selfmedicalrecord.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.selfmedicalrecord.R
import com.example.selfmedicalrecord.data.profile.UserProfile
import com.example.selfmedicalrecord.databinding.FragmentProfilBinding
import com.example.selfmedicalrecord.ui.auth.WelcomeActivity
import com.example.selfmedicalrecord.ui.profil.KataSandiActivity
import com.example.selfmedicalrecord.ui.profil.KelolaProfilActivity
import com.example.selfmedicalrecord.ui.profil.PusatBantuanActivity
import com.example.selfmedicalrecord.ui.profil.ProfilViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/** Tab Profil: ringkasan akun dan menu ke layar Kelola Profil, Kata Sandi, Pusat Bantuan, serta Keluar. */
class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfilViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rowManage.setOnClickListener { open(KelolaProfilActivity::class.java) }
        binding.rowSecurity.setOnClickListener { open(KataSandiActivity::class.java) }
        binding.rowHelp.setOnClickListener { open(PusatBantuanActivity::class.java) }
        binding.btnLogout.setOnClickListener { confirmLogout() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.profile.collect { render(it) } }
                launch { viewModel.loggedOut.collect { openWelcome() } }
            }
        }
    }

    private fun render(profile: UserProfile) {
        binding.tvName.text = profile.fullName.ifBlank { getString(R.string.profil_default_name) }
        binding.tvEmail.text = profile.email
    }

    private fun open(target: Class<*>) {
        startActivity(Intent(requireContext(), target))
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.profil_logout_title)
            .setMessage(R.string.profil_logout_message)
            .setNegativeButton(R.string.profil_cancel, null)
            .setPositiveButton(R.string.profil_logout) { _, _ -> viewModel.logout() }
            .show()
    }

    /** Kembali ke layar Welcome dan buang semua layar sebelumnya dari back stack. */
    private fun openWelcome() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
