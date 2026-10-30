package com.example.selfmedicalrecord.data

import com.example.selfmedicalrecord.data.food.FakeFoodCalorieRepository
import com.example.selfmedicalrecord.data.food.FoodCalorieRepository
import com.example.selfmedicalrecord.data.health.FakeHealthRepository
import com.example.selfmedicalrecord.data.health.HealthRepository

/**
 * Penyedia dependensi sederhana (satu instance dipakai bersama seluruh app).
 * Cukup untuk saat ini; bisa diganti Hilt nanti tanpa mengubah ViewModel.
 */
object ServiceLocator {
    val healthRepository: HealthRepository by lazy { FakeHealthRepository(seedDemoData = true) }
    val foodCalorieRepository: FoodCalorieRepository by lazy { FakeFoodCalorieRepository() }
}
