package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level
import java.io.Serializable
import java.lang.RuntimeException

class GameFinishedFragment : Fragment() {
    /**
     * Результат игры.
     */
    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        bindViews()
    }

    /**
     * Установка слушателей для кнопки Попробовать снова и при навигации назад.
     */
    private fun setupClickListeners() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, callback
        )
        binding.btnRetry.setOnClickListener {
            retryGame()
        }
    }

    /**
     * Установка значений в текстовые поля, в зависимости от результата игры.
     */
    private fun bindViews() {
        with(binding) {
            // Смайлик.
            smile.setImageResource(getSmileResId())

            // Мин. необходимое кол-во ответов.
            tvRequiredAnswers.text = String.format(
                getString(R.string.tv_required_answers),
                    gameResult.gameSettings.minCountOfRightAnswers
            )

            // Тек. счет игры.
            tvScoreAnswers.text = String.format(
                getString(R.string.tv_score_answers),
                gameResult.countOfRightAnswers
            )

            // Мин. необходимый процент правильных ответов.
            tvRequiredPercentage.text = String.format(
                getString(R.string.tv_required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswers
            )

            // Процент правильных ответов.
            tvScorePercentage.text = String.format(
                getString(R.string.tv_score_percentage),
                getPercentOfRightAnswers()
            )
        }
    }

    /**
     * Получить ресурс для установке
     *
     * @return
     */
    private fun getSmileResId(): Int {
        return if (gameResult.winner)
            R.mipmap.ic_smile_foreground
        else
            R.mipmap.ic_upset_smile_foreground
    }

    /**
     * Получить процент правильных ответов.
     */
    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestions == 0)
            0
        else
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Парсинг аргументов.
     */
    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    /**
     * Перезапуск игры (переход к фрагменту, который идет перед фрагментом GameFragment).
     */
    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        /**
         * Ключ для аргумента: результат игры.
         */
        private const val KEY_GAME_RESULT = "game_result"

        /**
         * Создание экземпляра GameFinishedFragment с установкой аргументов.
         *
         * @param gameResult - результат игры
         * @return - GameFinishedFragment
         */
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}