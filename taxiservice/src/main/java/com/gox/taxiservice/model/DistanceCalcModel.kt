package com.gox.taxiservice.model

data class DistanceCalcModel(
    var routes: List<Route> = listOf()
)

data class Route(
    var legs: List<Leg> = listOf()
)

data class Leg(
    var distance: Distance = Distance(),
    var duration: Duration = Duration()
)

data class Duration(
    var text: String = "",
    var value: Int = 0
)

data class Distance(
    var text: String = "",
    var value: Int = 0
)
