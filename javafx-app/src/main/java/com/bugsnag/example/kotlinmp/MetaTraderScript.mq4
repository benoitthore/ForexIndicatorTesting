//+------------------------------------------------------------------+
//|                                                         VPEA.mq4 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
#property strict

#include <CommonFunctions.mqh>
#include <kotlin.mqh>

input string               InpTradeComment         =  "VPEA";  // Trade comment
input int InpMagicNumber = 0;

enum REQUEST_ID
  {
   GetClosePrice,
   GetEquity,
   GetIndicatorValue,
   GetIndicatorNumberOfParams,
   OpenPosition,
   UpdatePosition,
   ClosePosition
  };

enum INDICATOR
  {
   ATR,MA
  };


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

//
// Use CTrade, easier than doing our own coding
//
#include <Trade\Trade.mqh>
CTrade   *Trade;

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int OnInit()
  {

//
// Create a pointer to a ctrade object
//
   Trade =  new CTrade();
   Trade.SetExpertMagicNumber(InpMagicNumber);

   onStart(0,PipSize(Symbol()));

   Print("log");
   return INIT_SUCCEEDED;
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
   Print("OnDeinit2");
//
// Clean up the trade object
//
   delete   Trade;
   close();
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void OnTick()
  {
   if(!IsNewBar())
     {
      return;
     }
   doTick();
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void doTick()
  {
   onNewBar();
   exchange();
   goToActionMode();
   exchange();

  }

const double DEFAULT_VALUE = -1.0;

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void exchange()
  {
   bool isRequesting = false;

   int actionPointer;

   double arrayPointer[10];

   do
     {

      //reset (probably not needed)
      reset(arrayPointer);
      actionPointer = (double) DEFAULT_VALUE;
      isRequesting = request(actionPointer, arrayPointer,ArraySize(arrayPointer));

      if(isRequesting)
        {
         processData((REQUEST_ID) actionPointer, arrayPointer);
         response(actionPointer, arrayPointer,ArraySize(arrayPointer));
        }

     }
   while(isRequesting);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void processData(REQUEST_ID action,double &array[])
  {
   if(action == GetClosePrice)
     {
      MqlRates BarData[1];
      CopyRates(NULL, 0, 0, 1, BarData);
      array[0] = BarData[0].close;
     }
   else
      if(action == GetEquity)
        {
         array[0] = AccountInfoDouble(ACCOUNT_EQUITY);
        }
      else
         if(action == GetIndicatorValue)
           {

            string indicator = indicatorName((INDICATOR)array[0]);
            // + 1 because iCustom returns the last bar which should be really small
            int shift = (int) array[1] + 1;

            int handle =  iCustom(Symbol(),Period(),indicator);


            double tmp[] = { -1.0};
            for(int i = 0 ; i < 7 ; i++)
              {
               CopyBuffer(handle, i,shift,1,tmp);
               array[i] =tmp[0];
              }

            IndicatorRelease(handle);
           }
         else
            if(action == GetIndicatorNumberOfParams)
              {
               //TODO
              }
            else
               if(action == OpenPosition)
                 {
                  MqlTick Latest_Price; // Structure to get the latest prices
                  SymbolInfoTick(Symbol(),Latest_Price);  // Assign current prices to structure

                  double Bid = Latest_Price.bid;
                  double Ask = Latest_Price.ask;

                  ENUM_ORDER_TYPE orderType = (ENUM_ORDER_TYPE)((int) array[0]);
                  int magicNumber =  array[1];
                  double volume = array[2];
                  double stopLoss = array[3];
                  double takeProfit = array[4];

                  double   openPrice;

                  if(orderType == ORDER_TYPE_BUY)
                    {
                     openPrice         =  Ask;
                    }
                  else
                    {
                     openPrice         =  Bid;
                    }





                                    int   ticket   =  Trade.PositionOpen(
                                                         Symbol(), // symbol
                                                         orderType, // operation
                                                         volume, // volume
                                                         openPrice, // price
                                                         stopLoss, // stop loss
                                                         takeProfit, // take profit
                                                         NULL // comment

                                                      );


                                    if(ticket == -1)
                                      {
                                       array[0] = 0;
                                      }
                                    else
                                      {
                                       array[0] = 1;
                                      }


                 }
               else
                  if(action == UpdatePosition)
                    {
                     //TODO

                    }
                  else
                     if(action == ClosePosition)
                       {
                        //TODO

                       }
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void reset(double &array[])
  {
   for(int i = 0 ; i < ArraySize(array); i++)
     {
      array[i] = DEFAULT_VALUE;
     }
  }

//+------------------------------------------------------------------+



























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
/*
void  OpenTrade(ENUM_ORDER_TYPE  orderType) {

   double   openPrice;

   if (orderType==ORDER_TYPE_BUY) {
      openPrice         =  Ask;
   } else {
      openPrice         =  Bid;
   }

   int   ticket   =  OrderSend(Symbol(), orderType, InpOrderSize, openPrice, 0, 0, 0, InpTradeComment, InpMagicNumber);

}

bool  CloseTrade(ENUM_ORDER_TYPE orderType) {

   bool  result   =  true;

   PrintFormat("Closing %s orders due to signal", EnumToString(orderType));

   int   cnt      =  OrdersTotal();
   for (int i = cnt-1; i>=0; i--) {
      if (OrderSelect(i, SELECT_BY_POS, MODE_TRADES)) {
         if (OrderSymbol()==Symbol() && OrderMagicNumber()==InpMagicNumber && OrderType()==orderType) {
            result   &= OrderClose(OrderTicket(), OrderLots(), OrderClosePrice(), 0, clrYellow);
         }
      }
   }

   return(result);

}
*/
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
