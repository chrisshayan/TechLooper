package local

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.ALL
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.OFF

scan()

def LOG_FOLDER = "./log/"

new File(LOG_FOLDER).mkdirs()

appender("ROOT_FILE", RollingFileAppender) {
  file = "${LOG_FOLDER}techlooper-all.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_FOLDER}%d{yyyyMMdd}-techlooper.log"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

logger("org.elasticsearch", ERROR)
logger("org.hibernate", ERROR)
logger("org.dozer", ERROR)

root(ALL, ["ROOT_FILE"])