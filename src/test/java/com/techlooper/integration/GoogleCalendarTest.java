package com.techlooper.integration;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by phuonghqh on 8/17/15.
 */
public class GoogleCalendarTest {

//  @Test
  public void testMe() {
    System.out.println(org.joda.time.DateTime.now().toString());
  }

//  @Test
  public static void main(String[] args) throws IOException, GeneralSecurityException {

    JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory,
      "339114452335-57uq0b7djh0u478igqj5g3cub376urgp.apps.googleusercontent.com", "qEr_e0VkkMUg4PmCuGGMumjj",
      Collections.singleton(CalendarScopes.CALENDAR))
      .setDataStoreFactory(new FileDataStoreFactory(new File(".credentials")))
      .setAccessType("offline").build();

    AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
    String redirectUri = "http://localhost:8080/login/social/GOOGLE";
    authorizationUrl.setRedirectUri(redirectUri);
    System.out.println("Go to the following address:");
    System.out.println(authorizationUrl);

    System.out.println("What is the 'code' url parameter?");
    String code = new Scanner(System.in).nextLine();
    AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
    tokenRequest.setRedirectUri(redirectUri);
    TokenResponse tokenResponse = tokenRequest.execute();

    Credential credential = flow.createAndStoreCredential(tokenResponse, "techlooper");//.loadCredential("techlooper");//.createAndStoreCredential(tokenResponse, "techlooper");

    Calendar service = new Calendar.Builder(transport, jsonFactory, credential)
      .setApplicationName("Techlooper").build();


    Event event = new Event()
      .setSummary("Google I/O 2015")
      .setLocation("800 Howard St., San Francisco, CA 94103")
      .setDescription("A chance to hear more about Google's developer products.");

//    DateTime startDateTime = new DateTime("2015-09-28T09:00:00-07:00");
//    EventDateTime start = new EventDateTime()
//      .setDateTime(startDateTime)
//      .setTimeZone("America/Los_Angeles");
//    event.setStart(start);
//
//    DateTime endDateTime = new DateTime("2015-09-28T17:00:00-07:00");
//    EventDateTime end = new EventDateTime()
//      .setDateTime(endDateTime)
//      .setTimeZone("America/Los_Angeles");
//    event.setEnd(end);

    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm a");
    org.joda.time.DateTime startDate = dateTimeFormatter.parseDateTime("25/08/2015 02:15 PM");
    org.joda.time.DateTime endDate = dateTimeFormatter.parseDateTime("27/08/2015 02:15 PM");

    String start = startDate.toString();
    event.setStart(new EventDateTime().setDateTime(new DateTime(start)));

    String end = endDate.toString();
    event.setEnd(new EventDateTime().setDateTime(new DateTime(end)));

    EventAttendee[] attendees = new EventAttendee[]{
      new EventAttendee().setEmail("phuonghqh@gmail.com"),
      new EventAttendee().setEmail("thu.hoang@navigosgroup.com"),
    };
    event.setAttendees(Arrays.asList(attendees));

    String calendarId = "techlooperawesome@gmail.com";
    event = service.events().insert(calendarId, event).setSendNotifications(true).execute();
    System.out.printf("Event created: %s\n", event.getHtmlLink());
  }
}
