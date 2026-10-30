package com.example.selfmedicalrecord.data.profile

/** Data akun dan diri pengguna yang tampil di Profil dan Kelola Profil. */
data class UserProfile(
    val fullName: String = "",
    /** Null jika belum dipilih. */
    val gender: String? = null,
    /** Tanggal lahir sebagai epoch millis UTC tengah malam (apa adanya dari date picker). */
    val birthDateMillis: Long? = null,
    val nik: String = "",
    val address: String = "",
    val email: String = ""
)
