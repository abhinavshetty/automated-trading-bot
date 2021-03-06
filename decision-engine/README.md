# decision-engine
## This provides decision making capability to all bots.

Each decision engine is represented as a service implementation of the DecisionEngine class in client project (specify the code name as returned from getStrategies)

For any new engine, users must add the following properties in the respective initialize methods: (you can add multiple properties where {#} is specified)

1. botConfigurationConstants: INITIAL_TRADE_MARGIN, EXTENSION_TRADE_MARGIN, MINIMUM_ITEM_TRADE_HISTORY_DAYS, ENTRY_MONITORING_KLINE_{#}, ENTRY_MONITORING_WINDOW_HOURS, EXTENSION_MONITORING_KLINE_{#}, EXTENSION_MONITORING_WINDOW_HOURS

2. engineConfigurationConstants: LONG_PROFIT, LONG_EXTENSION_THRESHOLD, LONG_STOP_LOSS_THRESHOLD, SHORT_PROFIT, SHORT_EXTENSION_THRESHOLD, SHORT_STOP_LOSS_THRESHOLD

New constants can be added where needed.
