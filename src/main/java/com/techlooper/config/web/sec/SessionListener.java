package com.techlooper.config.web.sec;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by phuonghqh on 10/12/15.
 */
public class SessionListener implements HttpSessionListener {

  public void sessionCreated(HttpSessionEvent event) {
    event.getSession().setMaxInactiveInterval(15770000);
  }

  public void sessionDestroyed(HttpSessionEvent se) {
  }
}
