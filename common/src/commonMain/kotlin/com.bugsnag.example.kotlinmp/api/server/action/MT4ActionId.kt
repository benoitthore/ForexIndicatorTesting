package com.bugsnag.example.kotlinmp.api.server.action

enum class MT4ActionId {
    Close,
    GetClosePrice,
    GetIndicatorValue,
    GetIndicatorNumberOfParams,
    OpenPosition,
    UpdatePosition,
    ClosePosition;
}