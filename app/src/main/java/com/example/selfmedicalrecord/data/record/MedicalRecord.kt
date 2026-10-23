package com.example.selfmedicalrecord.data.record

/**
 * Satu catatan pelayanan kesehatan (kunjungan ke klinik/rumah sakit).
 * Kelak diisi dari OpenMRS lewat API Laravel.
 *
 * @param visitDateMillis tanggal kunjungan, epoch milidetik UTC.
 */
data class MedicalRecord(
    val id: String,
    val facilityName: String,
    val visitDateMillis: Long,
    val diagnosis: String,
    val complaint: String,
    val medications: List<String>
)
