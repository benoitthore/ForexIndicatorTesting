//+------------------------------------------------------------------+
//|                                                         test.mq5 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"



#include <kotlin.mqh>
#include <CommonFunctions.mqh>
#include <Indicators.mqh>
#include <baseAlgo.mqh>
#include <utils.mqh>




//+------------------------------------------------------------------+
//| REMOVE ME ON EA                           |
//+------------------------------------------------------------------+
void OnStart()
  {

   onStart(0,PipSize(Symbol()));
   doTick();
   doTick();
   doTick();
   doTick();
   doTick();

  }


