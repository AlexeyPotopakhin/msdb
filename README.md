# MSDB (MQTT Serial Device Bridge)

MQTT bridge with Raspberry Pi and any other serial (UART) device (e.g Arduino)

## Settings
Put all necessary settings to `settings.json`

## Start application
Execute application with gradle
```bash
gradle run
```
or
1. Create fat jar with gradle
```bash
gradle fatJar
```
2. Execute application 
```bash
java -jar msdb-*.jar
```
or 
1. Download the latest [release](https://github.com/AlexeyPotopakhin/msdb/releases)
2. Execute application 
```bash
java -jar msdb-*.jar
```
