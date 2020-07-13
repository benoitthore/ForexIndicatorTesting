//+------------------------------------------------------------------+
//|                                                         VPEA.mq4 |
//|                        Copyright 2020, MetaQuotes Software Corp. |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.00"
#property strict

#include <Orchard\Common\CommonFunctions.mqh>
#include <kotlin.mqh>

input string               InpTradeComment         =  "VPEA";  // Trade comment

enum REQUEST_ID {
   GetClosePrice,
   GetEquity,
   GetIndicatorValue,
   GetIndicatorNumberOfParams,
   OpenPosition,
   UpdatePosition,
   ClosePosition
};



//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick() {

   if (!IsTesting() && !TradeAllowed())  return;        // Try again next time
   if (!IsNewBar()) return;        // This EA only runs at the start of each bar

   doTick();
}

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void doTick() {
   if(!IsNewBar()) return;
   if(!TradeAllowed()) return;

   onNewBar();
   exchange();
   goToActionMode();
   exchange();

}

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
const double DEFAULT_VALUE = -1.0;

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void exchange() {
   bool isRequesting = false;

   int actionPointer;

   double arrayPointer[10];

   do {
      //reset (probably not needed)
      reset(arrayPointer);
      actionPointer = (double) DEFAULT_VALUE;
      isRequesting = request(actionPointer, arrayPointer,ArraySize(arrayPointer));
      if (isRequesting) {
         processData((REQUEST_ID) actionPointer, arrayPointer);
         response(actionPointer, arrayPointer,ArraySize(arrayPointer));
      }
   } while (isRequesting);


}

void processData(REQUEST_ID action,double &array[]) {
   if(action == GetClosePrice){
      array[0] = Close[1];
   }
   else if(action == GetEquity){
      array[0] = AccountEquity();
   }
}

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void reset(double &array[]) {
   for(int i = 0 ; i < ArraySize(array); i++) {
      array[i] = DEFAULT_VALUE;
   }
}



bool  IsNewBar() {

   static datetime   currentTime =  0;                                        // Will retain last value between calls
   bool  result   =  (currentTime!=Time[0]);                                  // returns true at each new bar
   if (result)       currentTime =  Time[0];                                  // Update current at a new bar
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
