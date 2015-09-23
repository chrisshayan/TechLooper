package staging

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import java.text.SimpleDateFormat

import static ch.qos.logback.classic.Level.*

scan()

def LOG_FOLDER = "/data/www-logs/techlooper/"

new File(LOG_FOLDER).mkdirs()

appender("ROOT_FILE", RollingFileAppender) {
  file = "${LOG_FOLDER}techlooper-all.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_FOLDER}techlooper-%d{yyyyMMdd}.log"
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