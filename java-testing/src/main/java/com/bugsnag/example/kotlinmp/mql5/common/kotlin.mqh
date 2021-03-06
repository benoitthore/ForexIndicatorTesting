//+------------------------------------------------------------------+
//|                                   Wolfgang Flohr-Hochbichler.mq4 |
//|                                       Copyright © 2011, Flohr IT |
//|                                            http://www.flohr-it.de |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2018, Flohr IT"
#property link      "http://www.flohr-it.de"


//+------------------------------------------------------------------+
//| DLL imports                                                      |
//+------------------------------------------------------------------+
/*
#import "native.dll"
   int testFun();
   string testFunString();
#import


typedef double(*getdouble)();
struct MyStruct {
          getdouble get_sl;
          getdouble get_tp;
        } ;


*/

//+------------------------------------------------------------------+
//| DLL imports                                                      |
//+------------------------------------------------------------------+


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
//typedef double(*getdouble)();



#import "native.dll"

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+

int testFun();
void testArray(int &action,double &arr[], int size);

void setTestIndex(int index);

void onStart(int symbol, double pipPrice);
void onNewBar();
bool request(int &action,double &arr[], int size);
void response(int &action,double &arr[], int size);
void goToActionMode();
void close();

#import
//+------------------------------------------------------------------+
