package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.*
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class GameFragment : Fragment() {
    // Уровень сложности игры.
    private lateinit var level: Level
    private val viewModelFactory by lazy {
        GameViewModelFactory(level, requireActivity().application)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    // Коллекция TextView с вариантами ответов.
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVIewModel()
        setOnClickListenersToOptions()
    }

    /**
     * Установка слушателей для вариантов ответа.
     *
     */
    private fun setOnClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    /**
     * Подписка на объекты LiveData из ViewModel'и.
     */
    private fun observeVIewModel() {
        // Если изменился вопрос, то меняем тексты соответствующих TextView
        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            tvOptions.forEachIndexed { index, _ ->
                tvOptions[index].text = it.options[index].toString()
            }
        }

        // Процент правильных ответов, установка прогресса игры.
        viewModel.percentOfRightAnswer.observe(viewLifecycleOwner) {
            binding.gameProgress.setProgress(it, true)
        }

        // Достаточное количество ответов.
        viewModel.enoughCount.observe(viewLifecycleOwner) {
            binding.tvAnswerProgress.setTextColor(getColorByState(it))
        }

        // Достаточный процент правильных ответов.
        viewModel.enoughPercent.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.gameProgress.progressTintList = ColorStateList.valueOf(color)
        }

        // Изменение форматированного времени.
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        // Минимально необходимый процент правильных ответов.
        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.gameProgress.secondaryProgress = it
        }

        // Запуск фрагмента GameFinishedFragment если получили результат игры.
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }

        // Прогресс в игре (сводка игры).
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswerProgress.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Получить id ресурса с цветом в зависимости от статуса игры.
     *
     * @param goodState - все хорошо?
     * @return - id ресурса
     */
    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    /**
     * Парсинг аргументов
     */
    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    /**
     * Запуск GameFinishedFragment.
     *
     * @param gameResult - рузультат игры
     */
    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        // Ключ для хранения фрагмента.
        const val NAME = "GameFragment"

        // Ключ для хранения уровеня сложности игры.
        private const val KEY_LEVEL = "level"

        /**
         * Создание экземпляра GameFragment в зависимости от уровня сложности игры.
         *
         * @param level - уровень сложности
         * @return GameFragment
         */
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}