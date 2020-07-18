//+------------------------------------------------------------------+
//|                                                         test.mq5 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"

#include <kotlin.mqh>


enum INDICATOR
  {
   ATR,MA
  };


int indicator[2][10];

// TODO Generate this code programtically
string indicatorName(INDICATOR indicator)
  {
   if(indicator == ATR)
     {
      return "Examples\\ATR";
     }
   if(indicator == MA)
     {
      return "Examples\\Custom Moving Average";
     }
   Alert("invalide indicator " + indicator);
   return NULL;
  }



int indicatorHandles[2];


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int getHandleForIndicator(INDICATOR indicator)
  {
   int index =(int) indicator;
      if(indicatorHandles[index] == 0)
     {
      string name = indicatorName(indicator);
      indicatorHandles[index] = iCustom(Symbol(),Period(),name);
     }
   return indicatorHandles[index];
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void  releaseIndicators()
  {
   for(int i = 0 ; i < ArraySize(indicatorHandles)  ; i++)
     {
      IndicatorRelease(indicatorHandles[i]);
     }
  }

//+------------------------------------------------------------------+
