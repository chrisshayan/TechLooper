package com.techlooper.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by phuonghqh on 12/16/14.
 */
public class TwitterProfile extends UserProfile implements Serializable {

  private long id;
  private String screenName;
  private String name;
  private String url;
  private String profileImageUrl;
  private String description;
  private String location;
  private Date createdDate;
  private String language;
  private int statusesCount;
  private int friendsCount;
  private int followersCount;
  private int favoritesCount;
  private int listedCount;
  private boolean following;
  private boolean followRequestSent;
  private boolean isProtected;
  private boolean notificationsEnabled;
  private boolean verified;
  private boolean geoEnabled;
  private boolean contributorsEnabled;
  private boolean translator;
  private String timeZone;
  private int utcOffset;
  private String sidebarBorderColor;
  private String sidebarFillColor;
  private String backgroundColor;
  private boolean useBackgroundImage;
  private String backgroundImageUrl;
  private boolean backgroundImageTiled;
  private String textColor;
  private String linkColor;
  private boolean showAllInlineMedia;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getStatusesCount() {
    return statusesCount;
  }

  public void setStatusesCount(int statusesCount) {
    this.statusesCount = statusesCount;
  }

  public int getFriendsCount() {
    return friendsCount;
  }

  public void setFriendsCount(int friendsCount) {
    this.friendsCount = friendsCount;
  }

  public int getFollowersCount() {
    return followersCount;
  }

  public void setFollowersCount(int followersCount) {
    this.followersCount = followersCount;
  }

  public int getFavoritesCount() {
    return favoritesCount;
  }

  public void setFavoritesCount(int favoritesCount) {
    this.favoritesCount = favoritesCount;
  }

  public int getListedCount() {
    return listedCount;
  }

  public void setListedCount(int listedCount) {
    this.listedCount = listedCount;
  }

  public boolean isFollowing() {
    return following;
  }

  public void setFollowing(boolean following) {
    this.following = following;
  }

  public boolean isFollowRequestSent() {
    return followRequestSent;
  }

  public void setFollowRequestSent(boolean followRequestSent) {
    this.followRequestSent = followRequestSent;
  }

  public boolean isProtected() {
    return isProtected;
  }

  public void setProtected(boolean isProtected) {
    this.isProtected = isProtected;
  }

  public boolean isNotificationsEnabled() {
    return notificationsEnabled;
  }

  public void setNotificationsEnabled(boolean notificationsEnabled) {
    this.notificationsEnabled = notificationsEnabled;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public boolean isGeoEnabled() {
    return geoEnabled;
  }

  public void setGeoEnabled(boolean geoEnabled) {
    this.geoEnabled = geoEnabled;
  }

  public boolean isContributorsEnabled() {
    return contributorsEnabled;
  }

  public void setContributorsEnabled(boolean contributorsEnabled) {
    this.contributorsEnabled = contributorsEnabled;
  }

  public boolean isTranslator() {
    return translator;
  }

  public void setTranslator(boolean translator) {
    this.translator = translator;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public int getUtcOffset() {
    return utcOffset;
  }

  public void setUtcOffset(int utcOffset) {
    this.utcOffset = utcOffset;
  }

  public String getSidebarBorderColor() {
    return sidebarBorderColor;
  }

  public void setSidebarBorderColor(String sidebarBorderColor) {
    this.sidebarBorderColor = sidebarBorderColor;
  }

  public String getSidebarFillColor() {
    return sidebarFillColor;
  }

  public void setSidebarFillColor(String sidebarFillColor) {
    this.sidebarFillColor = sidebarFillColor;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public boolean isUseBackgroundImage() {
    return useBackgroundImage;
  }

  public void setUseBackgroundImage(boolean useBackgroundImage) {
    this.useBackgroundImage = useBackgroundImage;
  }

  public String getBackgroundImageUrl() {
    return backgroundImageUrl;
  }

  public void setBackgroundImageUrl(String backgroundImageUrl) {
    this.backgroundImageUrl = backgroundImageUrl;
  }

  public boolean isBackgroundImageTiled() {
    return backgroundImageTiled;
  }

  public void setBackgroundImageTiled(boolean backgroundImageTiled) {
    this.backgroundImageTiled = backgroundImageTiled;
  }

  public String getTextColor() {
    return textColor;
  }

  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }

  public String getLinkColor() {
    return linkColor;
  }

  public void setLinkColor(String linkColor) {
    this.linkColor = linkColor;
  }

  public boolean isShowAllInlineMedia() {
    return showAllInlineMedia;
  }

  public void setShowAllInlineMedia(boolean showAllInlineMedia) {
    this.showAllInlineMedia = showAllInlineMedia;
  }
}
