



 
 
 
 
 
 
 
 
 
 
 
 
 

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















