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
#include <utils.mqh>

#include <Trade\Trade.mqh>



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

CTrade   *Trade;
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

            // + 1 because iCustom returns the last bar which should be really small
            int shift = (int) array[1] + 1;

            int handle =  getHandleForIndicator((INDICATOR)array[0]);

            if(handle == -1)
              {
               1/(handle + 1);
              }

            double tmp[] = { -1.0};
            for(int i = 0 ; i < 7 ; i++)
              {
               CopyBuffer(handle, i,shift,1,tmp);
               array[i] =tmp[0];
              }

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
                  double accountPercentage = array[2];
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

                  int stopLossPips = MathAbs((openPrice - stopLoss) * MathPow(10,_Digits));

                  double volume = CalculateLotSize(stopLossPips,accountPercentage);

                  Trade.SetExpertMagicNumber(magicNumber);

                  int   ticket   =  Trade.PositionOpen(
                                       Symbol(), // symbol
                                       orderType, // operation
                                       volume, // volume
                                       openPrice, // price
                                       NormalizeDouble(stopLoss,_Digits), // stop loss
                                       NormalizeDouble(takeProfit,_Digits), // take profit
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
                     //Trade.SetExpertMagicNumber(magicNumber);
                     //TODO

                    }
                  else
                     if(action == ClosePosition)
                       {
                        //Trade.SetExpertMagicNumber(magicNumber);
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
