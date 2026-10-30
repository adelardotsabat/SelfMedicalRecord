package com.example.selfmedicalrecord.data.profile

import kotlinx.coroutines.flow.StateFlow

/** Sumber data profil. UI hanya tahu interface ini, bukan dari mana datanya. */
interface ProfileRepository {
    /** Profil terkini. Berubah otomatis setelah [updateProfile] berhasil. */
    val profile: StateFlow<UserProfile>

    suspend fun updateProfile(profile: UserProfile): Result<Unit>
}
