package com.example.composition.domain.usecases

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository

/**
 * Use-case для получения настроек игры.
 *
 * @property repository - репозиторий
 * @constructor Create empty Get game settings use case
 */
class GetGameSettingsUseCase (private val repository: GameRepository)
{
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}