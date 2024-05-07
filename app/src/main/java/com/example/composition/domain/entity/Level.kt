package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Уровень сложности игры.
 *
 * @constructor Create empty Level
 */
@Parcelize
enum class Level: Parcelable {
    TEST, EASY, NORMAL, HARD
}