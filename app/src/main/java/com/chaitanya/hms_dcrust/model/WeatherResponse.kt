package com.chaitanya.hms_dcrust.model

import com.chaitanya.hms_dcrust.model.Current
import com.chaitanya.hms_dcrust.model.Location

data class WeatherResponse(
    val current: Current,
    val location: Location
)