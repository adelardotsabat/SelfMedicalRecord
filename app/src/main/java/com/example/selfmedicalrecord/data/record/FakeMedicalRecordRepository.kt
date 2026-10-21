package com.example.selfmedicalrecord.data.record

import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone

/** Data contoh dari desain, dengan jeda kecil untuk meniru jaringan. Ganti saat API siap. */
class FakeMedicalRecordRepository : MedicalRecordRepository {

    override suspend fun getRecords(): Result<List<MedicalRecord>> {
        delay(600)
        return Result.success(
            listOf(
                MedicalRecord(
                    id = "1",
                    facilityName = "Klinik Panasea",
                    visitDateMillis = utcMillis(2026, Calendar.SEPTEMBER, 20),
                    diagnosis = "Hipertensi Ringan",
                    complaint = "Pusing berputar, leher bagian belakang terasa kaku dan tegang, insomnia beberapa malam",
                    medications = listOf("Amlodipin", "Vitamin B Complex")
                ),
                MedicalRecord(
                    id = "2",
                    facilityName = "Klinik Husada",
                    visitDateMillis = utcMillis(2026, Calendar.JULY, 18),
                    diagnosis = "Maag",
                    complaint = "Nyeri ulu hati terasa melilit, mual, nafsu makan menurun",
                    medications = listOf("Omeprazole")
                ),
                MedicalRecord(
                    id = "3",
                    facilityName = "Medical Center",
                    visitDateMillis = utcMillis(2026, Calendar.JANUARY, 10),
                    diagnosis = "Alergi",
                    complaint = "Gatal-gatal merah dan bentol di daerah lengan dan leher setelah mengonsumsi makanan laut",
                    medications = listOf("Cetirizine", "Salep")
                )
            )
        )
    }

    private fun utcMillis(year: Int, month: Int, day: Int): Long =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month, day)
        }.timeInMillis
}
