package production

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import java.text.SimpleDateFormat

import static ch.qos.logback.classic.Level.*

scan()

def LOG_FOLDER = "./log/"

new File(LOG_FOLDER).mkdirs()

appender("ALL", RollingFileAppender) {
  file = "${LOG_FOLDER}techlooper-all.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_FOLDER}%d{yyyyMMdd}-techlooper.log"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

appender("SPRING", RollingFileAppender) {
  file = "${LOG_FOLDER}techlooper-spring-all.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_FOLDER}%d{yyyyMMdd}-techlooper-spring.log"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

logger("org.springframework", ALL, ["SPRING"], Boolean.FALSE)

root(ALL, ["ALL"])