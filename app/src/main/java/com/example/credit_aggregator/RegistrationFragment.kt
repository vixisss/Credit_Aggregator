import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.credit_aggregator.databinding.FragmentRegistrationBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPhoneMask()
        showMaterialDatePicker()
        setupRegisterButton()
    }

    private fun setupPhoneMask() {
        binding.userPhoneNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digits = s.toString().replace(Regex("[^\\d]"), "")
                val formatted = StringBuilder()

                if (digits.length >= 1) {
                    formatted.append("+7 (")
                    if (digits.length > 1) {
                        formatted.append(digits.substring(1, Math.min(4, digits.length)))
                    }
                    if (digits.length >= 4) {
                        formatted.append(") ")
                        if (digits.length > 4) {
                            formatted.append(digits.substring(4, Math.min(7, digits.length)))
                        }
                    }
                    if (digits.length >= 7) {
                        formatted.append("-")
                        if (digits.length > 7) {
                            formatted.append(digits.substring(7, Math.min(9, digits.length)))
                        }
                    }
                    if (digits.length >= 9) {
                        formatted.append("-")
                        if (digits.length > 9) {
                            formatted.append(digits.substring(9, Math.min(11, digits.length)))
                        }
                    }
                }

                binding.userPhoneNumber.removeTextChangedListener(this)
                binding.userPhoneNumber.setText(formatted.toString())
                binding.userPhoneNumber.setSelection(formatted.length)
                binding.userPhoneNumber.addTextChangedListener(this)

                isFormatting = false
            }
        })
    }


    private fun showMaterialDatePicker() {
        binding.etBirthDate.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder().apply {
                setStart(Calendar.getInstance().apply { add(Calendar.YEAR, -100) }.timeInMillis)
                setEnd(Calendar.getInstance().timeInMillis)
                setOpenAt(Calendar.getInstance().timeInMillis)
            }

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату рождения")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = selection

                val dateStr = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(calendar.time)

                binding.etBirthDate.setText(dateStr)
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
            }
    }

    private fun setupRegisterButton() {
        binding.buttonRegistration.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val phone = binding.userPhoneNumber.text.toString().replace(Regex("[^\\d]"), "")
            val birthDate = binding.etBirthDate.text.toString()

            when {
                name.isEmpty() -> binding.userName.error = "Введите имя"
                phone.length != 11 -> binding.userPhoneNumber.error = "Номер должен содержать 11 цифр"
                birthDate.isEmpty() -> binding.etBirthDate.error = "Укажите дату рождения"
                else -> registerUser(name, phone, birthDate)
            }
        }
    }

    private fun registerUser(name: String, phone: String, birthDate: String) {
        Toast.makeText(
            requireContext(),
            "Регистрация успешна: $name, $phone, $birthDate",
            Toast.LENGTH_LONG
        ).show()

        // Пример навигации:
        // findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}