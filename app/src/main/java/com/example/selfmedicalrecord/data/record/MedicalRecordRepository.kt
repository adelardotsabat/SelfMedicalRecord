package com.example.selfmedicalrecord.data.record

/** Sumber data riwayat kesehatan. UI hanya tahu interface ini, bukan dari mana datanya. */
interface MedicalRecordRepository {
    suspend fun getRecords(): Result<List<MedicalRecord>>
}
