//+------------------------------------------------------------------+
//|                                                         test.mq5 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool  IsNewBar()
  {

   static datetime priorTime   =  0;
   datetime          currentTime =  iTime(Symbol(), Period(), 0);
   bool              result      = (currentTime!=priorTime);
   priorTime                     =  currentTime;
   return(result);

  }
