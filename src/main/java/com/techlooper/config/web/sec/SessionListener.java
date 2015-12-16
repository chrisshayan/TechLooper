package com.techlooper.config.web.sec;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by phuonghqh on 10/12/15.
 */
public class SessionListener implements HttpSessionListener {

  public static final int MAX_INACTIVE_INTERVAL = 15770000;

  public void sessionCreated(HttpSessionEvent event) {
    event.getSession().setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
  }

  public void sessionDestroyed(HttpSessionEvent se) {
  }
}
