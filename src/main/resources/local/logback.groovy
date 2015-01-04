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
logger("com.couchbase", ERROR, ["CONSOLE"])
logger("net.spy.memcached", ERROR, ["CONSOLE"])

root(ERROR, ["CONSOLE"])