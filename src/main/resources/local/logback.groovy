import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR

scan()

appender("CONSOLE", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{dd-MM-yyyy HH:mm:ss.SSS} %p [%t] %c{1}: %m%n"
  }
}

logger("org.springframework", DEBUG, ["CONSOLE"])
logger("org.springframework.data", ERROR, ["CONSOLE"])
logger(" org.springframework.messaging", ERROR, ["CONSOLE"])
logger("org.springframework.session", ERROR, ["CONSOLE"])

root(ERROR, ["CONSOLE"])