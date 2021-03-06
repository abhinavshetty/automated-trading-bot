# bot warehouse
## This provides environment that runs all bots.

Each bot is represented as an implementation of the Bot class (specify the code name as returned from getBotName)

Each bot uses a specific LifeCycle (service implementation of LifeCycle class) whose methods must be implemented before use.
