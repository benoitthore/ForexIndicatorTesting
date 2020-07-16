package com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests

enum class MT4RequestId {
    GetClosePrice,
    GetEquity,
    GetIndicatorValue,
    GetIndicatorNumberOfParams,
    OpenPosition,
    UpdatePosition,
    ClosePosition;
}