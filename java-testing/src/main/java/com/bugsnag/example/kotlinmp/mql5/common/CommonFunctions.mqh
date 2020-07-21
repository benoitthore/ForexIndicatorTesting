



 
 
 
 
 
 
 
 
 
 
 
 
 

// https://mql4tradingautomation.com/mql4-calculate-position-size/
//+------------------------------------------------------------------+
//We define the function to calculate the position size and return the lot to order
//Only parameter the Stop Loss, it will return a double
double CalculateLotSize(int SLPips, double riskPercentage)           //Calculate the size of the position size
  {

   double LotSize=0;
//We get the value of a tick
//double nTickValue=MarketInfo(Symbol(),MODE_TICKVALUE);
   double nTickValue = SymbolInfoDouble(Symbol(),SYMBOL_TRADE_TICK_VALUE); // Or SYMBOL_TRADE_TICK_VALUE_LOSS because its a SL ?


//If the digits are 3 or 5 we normalize multiplying by 10
/*
   if(_Digits % 1 == 1) // converts pipette to pip
     {
      nTickValue=nTickValue*10;
     }
*/
   //double lotStep = MarketInfo(Symbol(),MODE_LOTSTEP);
   double lotStep = SymbolInfoDouble(Symbol(),SYMBOL_VOLUME_STEP);


//We apply the formula to calculate the position size and assign the value to the variable
   LotSize=(AccountInfoDouble(ACCOUNT_EQUITY)*riskPercentage)/(SLPips*nTickValue);
   LotSize=MathRound(LotSize/lotStep)*lotStep ;
   return LotSize;
  }
//+------------------------------------------------------------------+




// Pip conversion functions described in https://youtu.be/IvYiJP1elxY
double   PipSize(string symbol = NULL)  {
   double   point    = SymbolInfoDouble  (symbol, SYMBOL_POINT) ;
   int      digits   =  SymbolInfoInteger(symbol, SYMBOL_DIGITS);
   return( ((digits%2)==1) ? point*10 : point);
}

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   PipsToPrice(double pips, string symbol)      {
   return(pips*PipSize(symbol));
}


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool TradeAllowed() {
   return true;
   //return (MarketInfo(Symbol(), MODE_TRADEALLOWED)>0);
}


/*
//
// Open and close price, for buy open=ask, close=bid, for sell open=bid, close=ask
//
double   OpenPrice(string symbol, int tradeType)               {
   return(OpenPrice(symbol, (ENUM_ORDER_TYPE)tradeType));
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   OpenPrice(string symbol, ENUM_ORDER_TYPE tradeType)   {
   return((tradeType%2==ORDER_TYPE_BUY) ?
          MarketInfo(symbol, MODE_ASK) : MarketInfo(symbol, MODE_BID));
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   ClosePrice(string symbol, int tradeType)              {
   return(ClosePrice(symbol, (ENUM_ORDER_TYPE)tradeType));
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   ClosePrice(string symbol, ENUM_ORDER_TYPE tradeType)  {
   return((tradeType%2==ORDER_TYPE_BUY) ?
          MarketInfo(symbol, MODE_BID) : MarketInfo(symbol, MODE_ASK));
}

// Simple math based on trade direction
double   Add(double v1, double v2, int tradeType)              {
   return( Add(v1, v2, (ENUM_ORDER_TYPE)tradeType) );
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   Add(double v1, double v2, ENUM_ORDER_TYPE tradeType)  {
   return( (tradeType%2==ORDER_TYPE_BUY) ? v1+v2 : v1-v2);
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   Sub(double v1, double v2, int tradeType)              {
   return( Sub(v1, v2, (ENUM_ORDER_TYPE)tradeType) );
}
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double   Sub(double v1, double v2, ENUM_ORDER_TYPE tradeType)  {
   return( (tradeType%2==ORDER_TYPE_BUY) ? v1-v2 : v1+v2);
}

// Simple comparisons based on trade direction
#define  _GT(v1, v2, tradeType)  ( (tradeType%2==ORDER_TYPE_BUY && v1>v2)  || (tradeType%2==ORDER_TYPE_SELL && v1<v2)   )
#define  _GE(v1, v2, tradeType)  ( (tradeType%2==ORDER_TYPE_BUY && v1>=v2) || (tradeType%2==ORDER_TYPE_SELL && v1<=v2)  )
#define  _LT(v1, v2, tradeType)  ( (tradeType%2==ORDER_TYPE_BUY && v1<v2)  || (tradeType%2==ORDER_TYPE_SELL && v1>v2)   )
#define  _LE(v1, v2, tradeType)  ( (tradeType%2==ORDER_TYPE_BUY && v1<v2)  || (tradeType%2==ORDER_TYPE_SELL && v1>v2)   )
*/