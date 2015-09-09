package production

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import java.text.SimpleDateFormat

import static ch.qos.logback.classic.Level.*

scan()

def LOG_FOLDER = "/data/www-logs/techlooper/" + (new SimpleDateFormat("yyyy-MM-dd")).format(new Date())

new File(LOG_FOLDER).mkdirs()

appender("ALL", RollingFileAppender) {
  file = "${LOG_FOLDER}/techlooper-all.log"
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "techlooper-all_%i.log"
    minIndex = 1
    maxIndex = 100
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "500MB"
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

appender("SPRING", RollingFileAppender) {
  file = "${LOG_FOLDER}/techlooper-spring-all.log"
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "techlooper-spring-all_%i.log"
    minIndex = 1
    maxIndex = 100
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "500MB"
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

appender("ERROR", RollingFileAppender) {
  file = "${LOG_FOLDER}/techlooper-error.log"
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "techlooper-error_%i.log"
    minIndex = 1
    maxIndex = 100
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "500MB"
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

logger("com.techlooper", ALL, ["ALL"], Boolean.FALSE)
logger("org.springframework", ALL, ["SPRING"], Boolean.FALSE)

root(ERROR, ["ERROR"])