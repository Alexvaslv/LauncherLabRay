package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installed_apps")
data class InstalledAppEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val developer: String,
    val rating: Float,
    val reviewsCount: String,
    val sizeMb: String,
    val downloads: String,
    val description: String,
    val iconKey: String,
    val isInstalled: Boolean = true,
    val installDate: Long = System.currentTimeMillis()
)
