package com.petapp.capybara.data.model.healthDiary

data class HealthDiaryForProfile(
    val bloodPressure: String? = null,
    val pulse: String? = null,
    val bloodGlucose: String? = null,
    val height: String? = null,
    val weight: String? = null
) {
    private constructor(builder: Builder) : this(
        builder.bloodPressure,
        builder.pulse,
        builder.bloodGlucose,
        builder.height,
        builder.weight
    )

    class Builder {
        var bloodPressure: String? = null
            private set

        var pulse: String? = null
            private set

        var bloodGlucose: String? = null
            private set

        var height: String? = null
            private set

        var weight: String? = null
            private set

        fun bloodPressure(bloodPressure: String) = apply { this.bloodPressure = bloodPressure }

        fun pulse(pulse: String) = apply { this.pulse = pulse }

        fun bloodGlucose(pulse: String) = apply { this.bloodGlucose = bloodGlucose }

        fun height(height: String) = apply { this.height = height }

        fun weight(weight: String) = apply { this.weight = weight }

        fun build() = HealthDiaryForProfile(this)
    }
}
