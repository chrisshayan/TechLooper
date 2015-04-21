import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import static ch.qos.logback.classic.Level.*

scan()

appender("CONSOLE", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

appender("FILE", RollingFileAppender) {
  file = "techlooper.log"
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "techlooper_%i.log"
    minIndex = 1
    maxIndex = 12
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "10MB"
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

appender("CRONF", RollingFileAppender) {
  file = "techlooper_cron.log"
  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "techlooper_cron_%i.log"
    minIndex = 1
    maxIndex = 12
  }
  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "10MB"
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

logger("com.techlooper.service.impl.LooperPointServiceImpl", ALL, ["CRONF"])

root(ERROR, ["CONSOLE", "FILE"])