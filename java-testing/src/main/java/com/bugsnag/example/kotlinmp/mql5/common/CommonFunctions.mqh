



 
 
 
 
 
 
 
 
 
 
 
 
 
 
 

// Pip conversion functions described in https://youtu.be/IvYiJP1elxY
double   PipSize(string symbol)  {
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