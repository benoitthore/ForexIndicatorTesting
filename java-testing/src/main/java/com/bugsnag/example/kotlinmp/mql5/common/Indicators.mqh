//+------------------------------------------------------------------+
//|                                                      ProjectName |
//|                                      Copyright 2020, CompanyName |
//|                                       http://www.companyname.net |
//+------------------------------------------------------------------+
#include <kotlin.mqh>

int indicator[85][10];
int indicatorHandles[85];

enum INDICATOR
  {
   _3D_OSCILLATOR_2,
   ABSOLUTE_STRENGTH_INDICATOR,
   ALF_OF_OSCILLATOR,
   ALTERNATIVE_CCI_INDICATOR,
   AMKA_INDICATOR,
   AROON_INDICATOR,
   ASCTREND_INDICATOR,
   ATR,
   AVERAGES_RAINBOW_INDICATOR,
   BALANCE_OF_MARKET_POWER,
   BEARS_VS_BULLS_INDICATOR,
   BLAU_JURIK_ECO_INDICATOR,
   BLAU_T3_ERGODIC_CANDLESTICK_OSCILLATOR,
   CANDLE_RATIO_INDICATOR,
   CCI_OF_AVERAGE_INDICATOR,
   CCI_T3_INDICADOR,
   CENTER_OF_GRAVITY_EXTENDED_INDICATOR,
   CHAIKIN_VOLATILITY_INDICATOR,
   CHANDELIER_EXIT_INDICATOR,
   CHANDES_DMI_INDICATOR,
   CHANELLON_PARABOLIC_INDICATOR,
   CHOPPY_MARKET_INDEX,
   CMO_INDICATOR,
   COLOR_PARABOLIC_INDICATOR,
   COMPARATIVE_ADX_INDICATOR,
   COPPOCK_INDICATOR,
   CRONEX_T_RSI_BBSW_INDICADOR,
   CUMULATIVE_VOLUME_INDICATOR,
   CUSTOM_MOVING_AVERAGE,
   CYBER_CYCLE_INDICATOR,
   D_INDEX_INDICATOR,
   DERIVATIVE_OSCILLATOR,
   DIRECTIONAL_MOMENTUM_OF_EMA,
   DISPARITY_INDEX_INDICATOR,
   DPO_BAR_INDICATOR,
   DRUNKARD_WALK_INDICATOR,
   DSL_DMI_OSCILLATOR,
   DSL_SYNTHETIC_EMA_MOMENTUM,
   DSS_BRESSERT_INDICADOR,
   DSS_RATIO_INDICATOR,
   DYNAMIC_TREND_INDICATOR,
   ELLIOT_OSCILLATOR,
   EMA_ANGLE_INDICATOR,
   FINITE_VOLUME_ELEMENTS,
   FISH_INDICATOR,
   FISHER_ORG_INDICATOR,
   FISHER_RVI_INDICADOR,
   FLAT_INDIKATOR,
   GANN_HIGH_LOW_ACTIVATOR_MTF,
   HURST_OSCILLATOR,
   IS7N_TREND_INDICATOR,
   IVAR_INDICATOR,
   KDJ_INDICATOR,
   LAGUERRE_RSI_WITH_LAGUERRE_FILTER_2,
   MAMA_INDICATOR,
   MFI_INDICATOR,
   NONLAGDOT_INDICATOR,
   NOTIS_INDICATOR,
   OBOS_INDICATOR,
   ORACLE_INDICATOR,
   PFE2_INDICATOR,
   POWER_TREND_INDICATOR,
   PREVAILING_TREND_INDICATOR,
   PRICE_CHANNEL_STOP,
   QQE_OF_VELOCITY_MTF_INDICATOR,
   RAVI_IFISH_INDICATEUR,
   REPULSE_INDICATOR,
   ROC_INDICATEUR,
   SCHAFF_TREND_CCI_INDIKATOR,
   SPEARMAN_RANK_AUTO_CORRELATION,
   SPLIT_MA_INDICATOR,
   STD_TREND_ENVELOPES_OF_AVERAGES,
   SUPER_SAR_INDICATOR,
   SUPER_TREND_DOT_INDICADOR,
   TIM_MORRIS_MA_INDICATOR,
   TMA_INDICATOR,
   TREND_CONFIRMATION_INDEX_INDICATOR,
   TREND_SCORE_OSCILLATOR,
   TRENDMAGIC_INDICATOR,
   TSI_INDICATOR,
   TTM_SQUEEZE_INDICATOR,
   VALUE_CHARTS_INDICATOR,
   VIDYA_INDICATEUR,
   VSA_CANDLE_INDICATOR,
   WATR_INDIKATOR,
  };
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
string indicatorName(INDICATOR indicator)
  {

   if(indicator == _3D_OSCILLATOR_2)
     {
      return "Externals\\3d-oscillator-2";
     }
   if(indicator == ABSOLUTE_STRENGTH_INDICATOR)
     {
      return "Externals\\absolute-strength-indicator";
     }
   if(indicator == ALF_OF_OSCILLATOR)
     {
      return "Externals\\alf-of-oscillator";
     }
   if(indicator == ALTERNATIVE_CCI_INDICATOR)
     {
      return "Externals\\alternative-cci-indicator";
     }
   if(indicator == AMKA_INDICATOR)
     {
      return "Externals\\amka-indicator";
     }
   if(indicator == AROON_INDICATOR)
     {
      return "Externals\\aroon-indicator";
     }
   if(indicator == ASCTREND_INDICATOR)
     {
      return "Externals\\asctrend-indicator";
     }
   if(indicator == ATR)
     {
      return "Externals\\atr";
     }
   if(indicator == AVERAGES_RAINBOW_INDICATOR)
     {
      return "Externals\\averages-rainbow-indicator";
     }
   if(indicator == BALANCE_OF_MARKET_POWER)
     {
      return "Externals\\balance-of-market-power";
     }
   if(indicator == BEARS_VS_BULLS_INDICATOR)
     {
      return "Externals\\bears-vs-bulls-indicator";
     }
   if(indicator == BLAU_JURIK_ECO_INDICATOR)
     {
      return "Externals\\blau-jurik-eco-indicator";
     }
   if(indicator == BLAU_T3_ERGODIC_CANDLESTICK_OSCILLATOR)
     {
      return "Externals\\blau-t3-ergodic-candlestick-oscillator";
     }
   if(indicator == CANDLE_RATIO_INDICATOR)
     {
      return "Externals\\candle-ratio-indicator";
     }
   if(indicator == CCI_OF_AVERAGE_INDICATOR)
     {
      return "Externals\\cci-of-average-indicator";
     }
   if(indicator == CCI_T3_INDICADOR)
     {
      return "Externals\\cci-t3-indicador";
     }
   if(indicator == CENTER_OF_GRAVITY_EXTENDED_INDICATOR)
     {
      return "Externals\\center-of-gravity-extended-indicator";
     }
   if(indicator == CHAIKIN_VOLATILITY_INDICATOR)
     {
      return "Externals\\chaikin-volatility-indicator";
     }
   if(indicator == CHANDELIER_EXIT_INDICATOR)
     {
      return "Externals\\chandelier-exit-indicator";
     }
   if(indicator == CHANDES_DMI_INDICATOR)
     {
      return "Externals\\chandes-dmi-indicator";
     }
   if(indicator == CHANELLON_PARABOLIC_INDICATOR)
     {
      return "Externals\\chanellon-parabolic-indicator";
     }
   if(indicator == CHOPPY_MARKET_INDEX)
     {
      return "Externals\\choppy-market-index";
     }
   if(indicator == CMO_INDICATOR)
     {
      return "Externals\\cmo-indicator";
     }
   if(indicator == COLOR_PARABOLIC_INDICATOR)
     {
      return "Externals\\color-parabolic-indicator";
     }
   if(indicator == COMPARATIVE_ADX_INDICATOR)
     {
      return "Externals\\comparative-adx-indicator";
     }
   if(indicator == COPPOCK_INDICATOR)
     {
      return "Externals\\coppock-indicator";
     }
   if(indicator == CRONEX_T_RSI_BBSW_INDICADOR)
     {
      return "Externals\\cronex-t-rsi-bbsw-indicador";
     }
   if(indicator == CUMULATIVE_VOLUME_INDICATOR)
     {
      return "Externals\\cumulative-volume-indicator";
     }
   if(indicator == CUSTOM_MOVING_AVERAGE)
     {
      return "Externals\\custom-moving-average";
     }
   if(indicator == CYBER_CYCLE_INDICATOR)
     {
      return "Externals\\cyber-cycle-indicator";
     }
   if(indicator == D_INDEX_INDICATOR)
     {
      return "Externals\\d-index-indicator";
     }
   if(indicator == DERIVATIVE_OSCILLATOR)
     {
      return "Externals\\derivative-oscillator";
     }
   if(indicator == DIRECTIONAL_MOMENTUM_OF_EMA)
     {
      return "Externals\\directional-momentum-of-ema";
     }
   if(indicator == DISPARITY_INDEX_INDICATOR)
     {
      return "Externals\\disparity-index-indicator";
     }
   if(indicator == DPO_BAR_INDICATOR)
     {
      return "Externals\\dpo-bar-indicator";
     }
   if(indicator == DRUNKARD_WALK_INDICATOR)
     {
      return "Externals\\drunkard-walk-indicator";
     }
   if(indicator == DSL_DMI_OSCILLATOR)
     {
      return "Externals\\dsl-dmi-oscillator";
     }
   if(indicator == DSL_SYNTHETIC_EMA_MOMENTUM)
     {
      return "Externals\\dsl-synthetic-ema-momentum";
     }
   if(indicator == DSS_BRESSERT_INDICADOR)
     {
      return "Externals\\dss-bressert-indicador";
     }
   if(indicator == DSS_RATIO_INDICATOR)
     {
      return "Externals\\dss-ratio-indicator";
     }
   if(indicator == DYNAMIC_TREND_INDICATOR)
     {
      return "Externals\\dynamic-trend-indicator";
     }
   if(indicator == ELLIOT_OSCILLATOR)
     {
      return "Externals\\elliot-oscillator";
     }
   if(indicator == EMA_ANGLE_INDICATOR)
     {
      return "Externals\\ema-angle-indicator";
     }
   if(indicator == FINITE_VOLUME_ELEMENTS)
     {
      return "Externals\\finite-volume-elements";
     }
   if(indicator == FISH_INDICATOR)
     {
      return "Externals\\fish-indicator";
     }
   if(indicator == FISHER_ORG_INDICATOR)
     {
      return "Externals\\fisher-org-indicator";
     }
   if(indicator == FISHER_RVI_INDICADOR)
     {
      return "Externals\\fisher-rvi-indicador";
     }
   if(indicator == FLAT_INDIKATOR)
     {
      return "Externals\\flat-indikator";
     }
   if(indicator == GANN_HIGH_LOW_ACTIVATOR_MTF)
     {
      return "Externals\\gann-high-low-activator-mtf";
     }
   if(indicator == HURST_OSCILLATOR)
     {
      return "Externals\\hurst-oscillator";
     }
   if(indicator == IS7N_TREND_INDICATOR)
     {
      return "Externals\\is7n-trend-indicator";
     }
   if(indicator == IVAR_INDICATOR)
     {
      return "Externals\\ivar-indicator";
     }
   if(indicator == KDJ_INDICATOR)
     {
      return "Externals\\kdj-indicator";
     }
   if(indicator == LAGUERRE_RSI_WITH_LAGUERRE_FILTER_2)
     {
      return "Externals\\laguerre-rsi-with-laguerre-filter-2";
     }
   if(indicator == MAMA_INDICATOR)
     {
      return "Externals\\mama-indicator";
     }
   if(indicator == MFI_INDICATOR)
     {
      return "Externals\\mfi-indicator";
     }
   if(indicator == NONLAGDOT_INDICATOR)
     {
      return "Externals\\nonlagdot-indicator";
     }
   if(indicator == NOTIS_INDICATOR)
     {
      return "Externals\\notis-indicator";
     }
   if(indicator == OBOS_INDICATOR)
     {
      return "Externals\\obos-indicator";
     }
   if(indicator == ORACLE_INDICATOR)
     {
      return "Externals\\oracle-indicator";
     }
   if(indicator == PFE2_INDICATOR)
     {
      return "Externals\\pfe2-indicator";
     }
   if(indicator == POWER_TREND_INDICATOR)
     {
      return "Externals\\power-trend-indicator";
     }
   if(indicator == PREVAILING_TREND_INDICATOR)
     {
      return "Externals\\prevailing-trend-indicator";
     }
   if(indicator == PRICE_CHANNEL_STOP)
     {
      return "Externals\\price-channel-stop";
     }
   if(indicator == QQE_OF_VELOCITY_MTF_INDICATOR)
     {
      return "Externals\\qqe-of-velocity-mtf-indicator";
     }
   if(indicator == RAVI_IFISH_INDICATEUR)
     {
      return "Externals\\ravi-ifish-indicateur";
     }
   if(indicator == REPULSE_INDICATOR)
     {
      return "Externals\\repulse-indicator";
     }
   if(indicator == ROC_INDICATEUR)
     {
      return "Externals\\roc-indicateur";
     }
   if(indicator == SCHAFF_TREND_CCI_INDIKATOR)
     {
      return "Externals\\schaff-trend-cci-indikator";
     }
   if(indicator == SPEARMAN_RANK_AUTO_CORRELATION)
     {
      return "Externals\\spearman-rank-auto-correlation";
     }
   if(indicator == SPLIT_MA_INDICATOR)
     {
      return "Externals\\split-ma-indicator";
     }
   if(indicator == STD_TREND_ENVELOPES_OF_AVERAGES)
     {
      return "Externals\\std-trend-envelopes-of-averages";
     }
   if(indicator == SUPER_SAR_INDICATOR)
     {
      return "Externals\\super-sar-indicator";
     }
   if(indicator == SUPER_TREND_DOT_INDICADOR)
     {
      return "Externals\\super-trend-dot-indicador";
     }
   if(indicator == TIM_MORRIS_MA_INDICATOR)
     {
      return "Externals\\tim-morris-ma-indicator";
     }
   if(indicator == TMA_INDICATOR)
     {
      return "Externals\\tma-indicator";
     }
   if(indicator == TREND_CONFIRMATION_INDEX_INDICATOR)
     {
      return "Externals\\trend-confirmation-index-indicator";
     }
   if(indicator == TREND_SCORE_OSCILLATOR)
     {
      return "Externals\\trend-score-oscillator";
     }
   if(indicator == TRENDMAGIC_INDICATOR)
     {
      return "Externals\\trendmagic-indicator";
     }
   if(indicator == TSI_INDICATOR)
     {
      return "Externals\\tsi-indicator";
     }
   if(indicator == TTM_SQUEEZE_INDICATOR)
     {
      return "Externals\\ttm-squeeze-indicator";
     }
   if(indicator == VALUE_CHARTS_INDICATOR)
     {
      return "Externals\\value-charts-indicator";
     }
   if(indicator == VIDYA_INDICATEUR)
     {
      return "Externals\\vidya-indicateur";
     }
   if(indicator == VSA_CANDLE_INDICATOR)
     {
      return "Externals\\vsa-candle-indicator";
     }
   if(indicator == WATR_INDIKATOR)
     {
      return "Externals\\watr-indikator";
     }
   Alert("Indicator not found");
   return NULL;
  }
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
