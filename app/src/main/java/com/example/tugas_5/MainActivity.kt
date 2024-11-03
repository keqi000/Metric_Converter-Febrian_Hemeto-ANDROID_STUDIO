package com.example.tugas_5

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale
import android.text.Editable
import android.text.TextWatcher
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    // Deklarasi view components
    private lateinit var metricAutoCompleteTextView: AutoCompleteTextView
    private lateinit var unitFromAutoCompleteTextView: AutoCompleteTextView
    private lateinit var unitToAutoCompleteTextView: AutoCompleteTextView
    private lateinit var inputValue: EditText
    private lateinit var conversionResult: TextView
    private lateinit var conversionExplanationCard: CardView
    private lateinit var conversionExplanationText: TextView
    private lateinit var metricFactCard: CardView
    private lateinit var metricFactText: TextView

    private var selectedMetric: String? = null
    private var selectedUnitFrom: String? = null
    private var selectedUnitTo: String? = null

    // Data statis
    private val metricFacts = mapOf(
        "Panjang" to "Satuan panjang dalam Sistem Internasional (SI) adalah meter (m), yang digunakan sebagai satuan dasar untuk mengukur panjang atau jarak antara dua titik. Satu meter didefinisikan sebagai jarak yang ditempuh oleh cahaya dalam vakum selama selang waktu 1/299,792,458 detik.",
        "Massa" to "Kilogram (kg) adalah satuan dasar SI yang digunakan untuk mengukur massa, yang merupakan ukuran jumlah materi dalam sebuah objek. Satu kilogram saat ini didefinisikan berdasarkan konstanta Planck, dengan nilai tetap 6.62607015×10⁻³⁴ J·s.",
        "Volume" to "Satuan liter (L) adalah satuan turunan SI yang umum digunakan untuk mengukur volume, atau ruang yang ditempati oleh suatu benda. Meskipun liter bukan satuan dasar SI, satu liter setara dengan satu desimeter kubik (dm³), atau 1.000 mililiter (mL).",
        "Waktu" to "Detik (s) adalah satuan dasar dalam SI yang digunakan untuk mengukur waktu, yaitu interval antara dua kejadian. Definisi satu detik adalah durasi dari 9,192,631,770 periode radiasi yang sesuai dengan transisi antara dua tingkat energi dalam atom cesium-133.",
        "Suhu" to "Kelvin (K) adalah satuan dasar SI untuk mengukur suhu termodinamika. Satu kelvin didefinisikan berdasarkan nilai tetap dari titik tripel air, yang diukur pada 273,16 K. Skala Kelvin dimulai dari nol mutlak, yaitu titik di mana molekul-molekul berhenti bergerak.",
        "Arus Listrik" to "Ampere (A) adalah satuan dasar SI untuk mengukur arus listrik, yang menunjukkan laju aliran muatan listrik. Satu ampere didefinisikan sebagai arus yang dihasilkan dari satu coulomb muatan yang melewati suatu titik dalam satu detik. Definisi modern ampere didasarkan pada muatan elemen fundamental, yaitu 1,602176634×10⁻¹⁹ coulomb.",
        "Intensitas Cahaya" to "Candela (cd) adalah satuan dasar SI untuk intensitas cahaya. Satu candela setara dengan intensitas cahaya yang dipancarkan oleh sumber cahaya monokromatik dengan frekuensi 540×10¹² Hz dan intensitas radiasi 1/683 watt per steradian. Candela menunjukkan kemampuan sumber cahaya untuk memancarkan cahaya yang terlihat oleh mata manusia dalam arah tertentu.",
        "Jumlah Zat" to "Mol (mol) adalah satuan dasar SI yang digunakan untuk mengukur jumlah zat. Satu mol didefinisikan sebagai jumlah zat yang mengandung jumlah entitas dasar (seperti atom, molekul, atau ion) setara dengan jumlah atom dalam 12 gram isotop karbon-12. Satu mol mengandung sekitar 6,022×10²³ entitas dasar, yang dikenal sebagai bilangan Avogadro."
    )

    private val metricUnits = mapOf(
        "Panjang" to arrayOf(
            "Kilometer (km)", 
            "Hektometer (hm)", 
            "Dekameter (dam)", 
            "Meter (m)", 
            "Desimeter (dm)", 
            "Centimeter (cm)", 
            "Millimeter (mm)"
        ),
        "Massa" to arrayOf(
            "Kilogram (kg)",
            "Hektogram (hg)",
            "Dekagram (dag)",
            "Gram (g)",
            "Desigram (dg)",
            "Centigram (cg)",
            "Milligram (mg)"
        ),
        "Volume" to arrayOf(
            "Kiloliter (kL)",
            "Hektoliter (hL)",
            "Dekaliter (daL)",
            "Liter (L)",
            "Desiliter (dL)",
            "Centiliter (cL)",
            "Milliliter (mL)"
        ),
        "Waktu" to arrayOf(
            "Hari",
            "Jam",
            "Menit",
            "Detik",
            "Millidetik"
        ),
        "Suhu" to arrayOf(
            "Celsius (°C)",
            "Fahrenheit (°F)",
            "Kelvin (K)"
        ),
        "Arus Listrik" to arrayOf(
            "Kiloampere (kA)",
            "Hektoampere (hA)",
            "Dekaampere (daA)",
            "Ampere (A)",
            "Desiampere (dA)",
            "Centiampere (cA)",
            "Milliampere (mA)"
        ),
        "Intensitas Cahaya" to arrayOf(
            "Candela (cd)",
            "Lumen (lm)",
            "Lux (lx)"
        ),
        "Jumlah Zat" to arrayOf(
            "Mol (mol)",
            "Kilomol (kmol)",
            "Millimol (mmol)"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)

        initializeViews()
        setupDropdowns()
        setupInputWatcher()
    }

    private fun initializeViews() {
        metricAutoCompleteTextView = findViewById(R.id.metricAutoCompleteTextView)
        unitFromAutoCompleteTextView = findViewById(R.id.unitFromAutoCompleteTextView)
        unitToAutoCompleteTextView = findViewById(R.id.unitToAutoCompleteTextView)
        inputValue = findViewById(R.id.inputValue)
        conversionResult = findViewById(R.id.conversionResult)
        conversionExplanationCard = findViewById(R.id.conversionExplanationCard)
        conversionExplanationText = findViewById(R.id.conversionExplanationText)
        metricFactCard = findViewById(R.id.metricFactCard)
        metricFactText = findViewById(R.id.metricFactText)
    }

    private fun setupDropdowns() {
        // Setup metric dropdown
        val metrics = metricUnits.keys.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metrics)
        metricAutoCompleteTextView.setAdapter(adapter)

        // Setup listeners
        metricAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
            selectedMetric = metricAutoCompleteTextView.text.toString()
            updateUnitDropdowns(selectedMetric)
            updateMetricFact(selectedMetric)
            resetDropdowns()
        }

        setupUnitDropdownListeners()
    }

    private fun setupUnitDropdownListeners() {
        unitFromAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
            selectedUnitFrom = unitFromAutoCompleteTextView.text.toString()
            performConversion()
        }

        unitToAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
            selectedUnitTo = unitToAutoCompleteTextView.text.toString()
            performConversion()
        }
    }

    private fun setupInputWatcher() {
        inputValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                performConversion()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateUnitDropdowns(metric: String?) {
        val units = metricUnits[metric] ?: arrayOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, units)
        
        unitFromAutoCompleteTextView.setAdapter(adapter)
        unitToAutoCompleteTextView.setAdapter(adapter)
        
        unitFromAutoCompleteTextView.isEnabled = true
        unitToAutoCompleteTextView.isEnabled = true
        
        resetDropdowns()
    }

    private fun resetDropdowns() {
        unitFromAutoCompleteTextView.setText("", false)
        unitToAutoCompleteTextView.setText("", false)
        selectedUnitFrom = null
        selectedUnitTo = null
        // Reset input value
        inputValue.setText("")
        
        // Reset conversion result
        conversionResult.text = ""
        
        // Reset conversion explanation
        conversionExplanationText.text = ""
    }

    private fun updateMetricFact(metric: String?) {
        val fact = metricFacts[metric]
        if (fact != null) {
            metricFactText.text = fact
            metricFactCard.visibility = View.VISIBLE
        } else {
            metricFactText.text = getString(R.string.no_metric_fact)
        }
    }

    private fun performConversion() {
        val value = inputValue.text.toString().toDoubleOrNull()
        if (value != null && selectedUnitFrom != null && selectedUnitTo != null) {
            val result = convertUnits(selectedUnitFrom!!, selectedUnitTo!!, value)
            displayResult(value, result)
        } else {
            resetConversionResult()
        }
    }

    private fun displayResult(value: Double, result: Double) {
        val formattedResult = formatNumber(result)
        conversionResult.text = formattedResult
        
        val explanation = getConversionExplanation(selectedUnitFrom!!, selectedUnitTo!!, value, result)
        conversionExplanationText.text = explanation
        conversionExplanationCard.visibility = View.VISIBLE
    }

    private fun resetConversionResult() {
        conversionResult.text = ""
    }

    private fun formatNumber(number: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))
        numberFormat.maximumFractionDigits = 0 // Hilangkan desimal jika bilangan bulat
        numberFormat.minimumFractionDigits = 0
        
        // Jika number adalah bilangan bulat, hilangkan desimal
        return if (number % 1 == 0.0) {
            numberFormat.format(number.toLong())
        } else {
            // Jika ada desimal, tampilkan maksimal 4 angka di belakang koma
            numberFormat.maximumFractionDigits = 4
            numberFormat.format(number)
        }
    }

    private fun getConversionExplanation(fromUnit: String, toUnit: String, value: Double, result: Double): String {
        val formattedValue = formatNumber(value)
        val formattedResult = formatNumber(result)

        // Untuk konversi suhu
        if (fromUnit.contains("Celsius") || fromUnit.contains("Fahrenheit") || fromUnit.contains("Kelvin")) {
            return when {
                fromUnit.contains("Celsius") && toUnit.contains("Fahrenheit") -> 
                    String.format("Rumus: (°C × 9/5) + 32 = °F\nLangkah-langkah:\n1. (%s°C × 9/5) = %s\n2. %s + 32 = %s°F\n\nJadi, %s°C = %s°F", 
                        formattedValue, formatNumber(value * 9.0/5.0), formatNumber(value * 9.0/5.0), formattedResult, formattedValue, formattedResult)
                fromUnit.contains("Celsius") && toUnit.contains("Kelvin") ->
                    String.format("Rumus: °C + 273.15 = K\nLangkah-langkah:\n1. %s°C + 273.15 = %sK\n\nJadi, %s°C = %sK", 
                        formattedValue, formattedResult, formattedValue, formattedResult)
                fromUnit.contains("Fahrenheit") && toUnit.contains("Celsius") ->
                    String.format("Rumus: (°F - 32) × 5/9 = °C\nLangkah-langkah:\n1. (%s°F - 32) = %s\n2. %s × 5/9 = %s°C\n\nJadi, %s°F = %s°C", 
                        formattedValue, formatNumber(value - 32), formatNumber(value - 32), formattedResult, formattedValue, formattedResult)
                fromUnit.contains("Fahrenheit") && toUnit.contains("Kelvin") ->
                    String.format("Rumus: (°F - 32) × 5/9 + 273.15 = K\nLangkah-langkah:\n1. (%s°F - 32) = %s\n2. %s × 5/9 = %s\n3. %s + 273.15 = %sK\n\nJadi, %s°F = %sK", 
                        formattedValue, formatNumber(value - 32), formatNumber(value - 32), formatNumber((value - 32) * 5/9), formatNumber((value - 32) * 5/9), formattedResult, formattedValue, formattedResult)
                fromUnit.contains("Kelvin") && toUnit.contains("Celsius") ->
                    String.format("Rumus: K - 273.15 = °C\nLangkah-langkah:\n1. %sK - 273.15 = %s°C\n\nJadi, %sK = %s°C", 
                        formattedValue, formattedResult, formattedValue, formattedResult)
                fromUnit.contains("Kelvin") && toUnit.contains("Fahrenheit") ->
                    String.format("Rumus: (K - 273.15) × 9/5 + 32 = °F\nLangkah-langkah:\n1. (%sK - 273.15) = %s\n2. %s × 9/5 = %s\n3. %s + 32 = %s°F\n\nJadi, %sK = %s°F", 
                        formattedValue, formatNumber(value - 273.15), formatNumber(value - 273.15), formatNumber((value - 273.15) * 9/5), formatNumber((value - 273.15) * 9/5), formattedResult, formattedValue, formattedResult)
                else -> "$formattedValue $fromUnit = $formattedResult $toUnit"
            }
        }

        // Untuk konversi intensitas cahaya
        if (fromUnit.contains("Candela") || fromUnit.contains("Lumen") || fromUnit.contains("Lux")) {
            return when {
                fromUnit.contains("Candela") && toUnit.contains("Lumen") ->
                    "Rumus: 1 cd = 4π lm (asumsi sudut ruang 1 steradian)\nLangkah-langkah:\n1. $formattedValue cd × 4π ≈ $formattedResult lm\n\nJadi, $formattedValue cd = $formattedResult lm"
                fromUnit.contains("Lumen") && toUnit.contains("Candela") ->
                    "Rumus: 1 lm = 1/(4π) cd (asumsi sudut ruang 1 steradian)\nLangkah-langkah:\n1. $formattedValue lm ÷ 4π ≈ $formattedResult cd\n\nJadi, $formattedValue lm = $formattedResult cd"
                fromUnit.contains("Lumen") && toUnit.contains("Lux") ->
                    "Rumus: 1 lm = 1 lx (asumsi area 1 m²)\nLangkah-langkah:\n1. $formattedValue lm = $formattedResult lx\n\nJadi, $formattedValue lm = $formattedResult lx"
                fromUnit.contains("Lux") && toUnit.contains("Lumen") ->
                    "Rumus: 1 lx = 1 lm (asumsi area 1 m²)\nLangkah-langkah:\n1. $formattedValue lx = $formattedResult lm\n\nJadi, $formattedValue lx = $formattedResult lm"
                fromUnit.contains("Candela") && toUnit.contains("Lux") ->
                    "Rumus: 1 cd = 4π lx (asumsi jarak 1 meter)\nLangkah-langkah:\n1. $formattedValue cd × 4π ≈ $formattedResult lx\n\nJadi, $formattedValue cd = $formattedResult lx"
                fromUnit.contains("Lux") && toUnit.contains("Candela") ->
                    "Rumus: 1 lx = 1/(4π) cd (asumsi jarak 1 meter)\nLangkah-langkah:\n1. $formattedValue lx ÷ 4π ≈ $formattedResult cd\n\nJadi, $formattedValue lx = $formattedResult cd"
                else -> "$formattedValue $fromUnit = $formattedResult $toUnit"
            }
        }

        // Untuk konversi jumlah zat
        if (fromUnit.contains("Mol") || toUnit.contains("Mol")) {
            val fromFactor = when {
                fromUnit.contains("Kilomol") -> 1000.0
                fromUnit.contains("Millimol") -> 0.001
                else -> 1.0
            }
            val toFactor = when {
                toUnit.contains("Kilomol") -> 0.001
                toUnit.contains("Millimol") -> 1000.0
                else -> 1.0
            }
            return "Langkah-langkah konversi:\n1. Identifikasi faktor konversi:\n" +
                "   • 1 ${fromUnit.split(" ")[0]} = ${formatNumber(fromFactor)} mol\n" +
                "   • 1 ${toUnit.split(" ")[0]} = ${formatNumber(toFactor)} mol\n" +
                "2. Perhitungan:\n" +
                "   • $formattedValue × ${formatNumber(fromFactor)} = ${formatNumber(value * fromFactor)} mol\n" +
                "   • ${formatNumber(result * toFactor)} ÷ ${formatNumber(toFactor)} = $formattedResult ${toUnit.split(" ")[0]}\n" +
                "\nJadi, $formattedValue $fromUnit = $formattedResult $toUnit"
        }

        // Untuk konversi waktu
        if (fromUnit in listOf("Hari", "Jam", "Menit", "Detik", "Millidetik") &&
            toUnit in listOf("Hari", "Jam", "Menit", "Detik", "Millidetik")) {
            val explanation = StringBuilder()
            explanation.append("Langkah-langkah konversi:\n")
            explanation.append("1. Identifikasi faktor konversi:\n")
            
            // Menambahkan penjelasan faktor waktu
            when (fromUnit) {
                "Hari" -> explanation.append("   • 1 Hari = 86.400 Detik\n")
                "Jam" -> explanation.append("   • 1 Jam = 3.600 Detik\n")
                "Menit" -> explanation.append("   • 1 Menit = 60 Detik\n")
                "Detik" -> explanation.append("   • 1 Detik = 1 Detik\n")
                "Millidetik" -> explanation.append("   • 1 Millidetik = 0,001 Detik\n")
            }
            
            when (toUnit) {
                "Hari" -> explanation.append("   • 1 Hari = 86.400 Detik\n")
                "Jam" -> explanation.append("   • 1 Jam = 3.600 Detik\n")
                "Menit" -> explanation.append("   • 1 Menit = 60 Detik\n")
                "Detik" -> explanation.append("   • 1 Detik = 1 Detik\n")
                "Millidetik" -> explanation.append("   • 1 Millidetik = 0,001 Detik\n")
            }

            explanation.append("\n2. Perhitungan:\n")
            
            // Konversi ke detik terlebih dahulu
            val fromFactor = getTimeFactor(fromUnit)
            val intermediateValue = value * fromFactor
            explanation.append("   • $formattedValue $fromUnit = ${formatNumber(intermediateValue)} Detik\n")
            
            // Konversi dari detik ke unit tujuan
            val toFactor = getTimeFactor(toUnit)
            explanation.append("   • ${formatNumber(intermediateValue)} Detik ÷ ${formatNumber(toFactor)} = $formattedResult $toUnit\n")
            
            explanation.append("\nJadi, $formattedValue $fromUnit = $formattedResult $toUnit")
            
            return explanation.toString()
        }

        // Untuk konversi metrik lainnya
        val explanation = StringBuilder()
        val fromFactor = when {
            fromUnit.contains("Kilo") -> 1000.0
            fromUnit.contains("Hekto") -> 100.0
            fromUnit.contains("Deka") -> 10.0
            fromUnit.contains("Desi") -> 0.1
            fromUnit.contains("Centi") -> 0.01
            fromUnit.contains("Milli") -> 0.001
            else -> 1.0
        }

        val toFactor = when {
            toUnit.contains("Kilo") -> 1000.0
            toUnit.contains("Hekto") -> 100.0
            toUnit.contains("Deka") -> 10.0
            toUnit.contains("Desi") -> 0.1
            toUnit.contains("Centi") -> 0.01
            toUnit.contains("Milli") -> 0.001
            else -> 1.0
        }

        explanation.append("Langkah-langkah konversi:\n")
        explanation.append("1. Identifikasi faktor konversi:\n")
        
        // Menambahkan penjelasan faktor
        if (fromFactor != 1.0) {
            explanation.append("   • 1 ${fromUnit.split(" ")[0]} = ${formatNumber(fromFactor)} ${getBaseUnit(fromUnit)}\n")
        }
        if (toFactor != 1.0) {
            explanation.append("   • 1 ${toUnit.split(" ")[0]} = ${formatNumber(toFactor)} ${getBaseUnit(toUnit)}\n")
        }

        explanation.append("\n2. Perhitungan:\n")
        if (fromFactor != 1.0) {
            val intermediate = value * fromFactor
            explanation.append("   • $formattedValue × ${formatNumber(fromFactor)} = ${formatNumber(intermediate)} ${getBaseUnit(fromUnit)}\n")
        }
        if (toFactor != 1.0) {
            explanation.append("   • ${formatNumber(result * toFactor)} ÷ ${formatNumber(toFactor)} = $formattedResult ${toUnit.split(" ")[0]}\n")
        }

        explanation.append("\nJadi, $formattedValue $fromUnit = $formattedResult $toUnit")

        return explanation.toString()
    }

    private fun getBaseUnit(unit: String): String {
        return when {
            unit.contains("meter") -> "meter"
            unit.contains("gram") -> "gram"
            unit.contains("liter") -> "liter"
            unit.contains("ampere") -> "ampere"
            else -> "satuan dasar"
        }
    }

    private fun convertUnits(from: String, to: String, value: Double): Double {
        // Ekstrak unit dengan lebih tepat menggunakan regex untuk mengambil bagian dalam kurung
        val fromUnit = from.substringBefore(" (")  // Mengambil bagian sebelum " ("
        val toUnit = to.substringBefore(" (")      // Mengambil bagian sebelum " ("

        return when {
            // Konversi Panjang
            fromUnit.endsWith("meter") || fromUnit in listOf("Kilometer", "Hektometer", "Dekameter", "Meter", "Desimeter", "Centimeter", "Millimeter") -> {
                val fromFactor = getLengthFactor(fromUnit)
                val toFactor = getLengthFactor(toUnit)
                value * (fromFactor / toFactor)
            }
            
            // Konversi Massa
            fromUnit.endsWith("gram") || fromUnit in listOf("Kilogram", "Hektogram", "Dekagram", "Gram", "Desigram", "Centigram", "Milligram") -> {
                val fromFactor = getMassFactor(fromUnit)
                val toFactor = getMassFactor(toUnit)
                value * (fromFactor / toFactor)
            }

            // Konversi Volume
            fromUnit.endsWith("liter") || fromUnit in listOf("Kiloliter", "Hektoliter", "Dekaliter", "Liter", "Desiliter", "Centiliter", "Milliliter") -> {
                val fromFactor = getVolumeFactor(fromUnit)
                val toFactor = getVolumeFactor(toUnit)
                value * (fromFactor / toFactor)
            }

            // Konversi Waktu
            fromUnit in listOf("Hari", "Jam", "Menit", "Detik", "Millidetik") &&
            toUnit in listOf("Hari", "Jam", "Menit", "Detik", "Millidetik") -> {
                val fromFactor = getTimeFactor(fromUnit)
                val toFactor = getTimeFactor(toUnit)
                value * (fromFactor / toFactor)
            }

            // Konversi Suhu
            fromUnit == "Celsius" && toUnit == "Fahrenheit" -> (value * 9/5) + 32
            fromUnit == "Celsius" && toUnit == "Kelvin" -> value + 273.15
            fromUnit == "Fahrenheit" && toUnit == "Celsius" -> (value - 32) * 5/9
            fromUnit == "Fahrenheit" && toUnit == "Kelvin" -> (value - 32) * 5/9 + 273.15
            fromUnit == "Kelvin" && toUnit == "Celsius" -> value - 273.15
            fromUnit == "Kelvin" && toUnit == "Fahrenheit" -> (value - 273.15) * 9/5 + 32

            // Konversi Arus Listrik
            fromUnit.endsWith("ampere") || fromUnit in listOf("Kiloampere", "Hektoampere", "Dekaampere", "Ampere", "Desiampere", "Centiampere", "Milliampere") -> {
                val fromFactor = getCurrentFactor(fromUnit)
                val toFactor = getCurrentFactor(toUnit)
                value * (fromFactor / toFactor)
            }

            // Konversi Intensitas Cahaya
            from.contains("Candela") && to.contains("Lumen") -> value * 4 * Math.PI // Asumsi sudut ruang 1 steradian
            from.contains("Lumen") && to.contains("Candela") -> value / (4 * Math.PI)
            from.contains("Lumen") && to.contains("Lux") -> value // Asumsi area 1 m²
            from.contains("Lux") && to.contains("Lumen") -> value // Asumsi area 1 m²
            from.contains("Candela") && to.contains("Lux") -> value * 4 * Math.PI // Asumsi jarak 1 meter
            from.contains("Lux") && to.contains("Candela") -> value / (4 * Math.PI)
            
            // Konversi Jumlah Zat
            from.contains("Mol") || to.contains("Mol") -> {
                val fromFactor = when {
                    from.contains("Kilomol") -> 1000.0
                    from.contains("Millimol") -> 0.001
                    else -> 1.0
                }
                val toFactor = when {
                    to.contains("Kilomol") -> 0.001
                    to.contains("Millimol") -> 1000.0
                    else -> 1.0
                }
                value * (fromFactor / toFactor)
            }

            else -> value
        }
    }

    private fun getLengthFactor(unit: String): Double {
        return when (unit) {
            "Kilometer" -> 1000.0         // 1 km = 1000 m
            "Hektometer" -> 100.0         // 1 hm = 100 m
            "Dekameter" -> 10.0           // 1 dam = 10 m
            "Meter" -> 1.0                // 1 m = 1 m (satuan dasar)
            "Desimeter" -> 0.1            // 1 dm = 0.1 m
            "Centimeter" -> 0.01          // 1 cm = 0.01 m
            "Millimeter" -> 0.001         // 1 mm = 0.001 m
            else -> 1.0
        }
    }

    private fun getMassFactor(unit: String): Double {
        return when (unit) {
            "Kilogram" -> 1000.0          // 1 kg = 1000 g
            "Hektogram" -> 100.0          // 1 hg = 100 g
            "Dekagram" -> 10.0            // 1 dag = 10 g
            "Gram" -> 1.0                 // 1 g = 1 g (satuan dasar)
            "Desigram" -> 0.1             // 1 dg = 0.1 g
            "Centigram" ->  0.01           // 1 cg = 0.01 g
            "Milligram" -> 0.001          // 1 mg = 0.001 g
            else -> 1.0
        }
    }

    private fun getVolumeFactor(unit: String): Double {
        return when (unit) {
            "Kiloliter" -> 1000.0         // 1 kL = 1000 L
            "Hektoliter" -> 100.0         // 1 hL = 100 L
            "Dekaliter" -> 10.0           // 1 daL = 10 L
            "Liter" -> 1.0                // 1 L = 1 L (satuan dasar)
            "Desiliter" -> 0.1            // 1 dL = 0.1 L
            "Centiliter" -> 0.01          // 1 cL = 0.01 L
            "Milliliter" -> 0.001         // 1 mL = 0.001 L
            else -> 1.0
        }
    }

    private fun getTimeFactor(unit: String): Double {
        return when (unit) {
            "Hari" -> 86400.0             // 1 hari = 86400 detik
            "Jam" -> 3600.0               // 1 jam = 3600 detik
            "Menit" -> 60.0               // 1 menit = 60 detik
            "Detik" -> 1.0                // 1 detik = 1 detik (satuan dasar)
            "Millidetik" -> 0.001         // 1 ms = 0.001 detik
            else -> 1.0
        }
    }

    private fun getCurrentFactor(unit: String): Double {
        return when (unit) {
            "Kiloampere" -> 1000.0        // 1 kA = 1000 A
            "Hektoampere" -> 100.0        // 1 hA = 100 A
            "Dekaampere" -> 10.0          // 1 daA = 10 A
            "Ampere" -> 1.0               // 1 A = 1 A (satuan dasar)
            "Desiampere" -> 0.1           // 1 dA = 0.1 A
            "Centiampere" -> 0.01         // 1 cA = 0.01 A
            "Milliampere" -> 0.001        // 1 mA = 0.001 A
            else -> 1.0
        }
    }
}