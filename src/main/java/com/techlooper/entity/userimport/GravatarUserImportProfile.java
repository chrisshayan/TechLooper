package com.techlooper.entity.userimport;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 2/27/15.
 */
public class GravatarUserImportProfile implements UserImportProfile {

    private String profileUrl;

    private String thumbnailUrl;

    private String preferredUsername;

    private String displayName;

    private String aboutMe;

    private List<PhotoModel> photos;

    private ProfileBackgroundModel profileBackground;

    private NameModel name;

    private List<PhoneNumberModel> phoneNumbers;

    private List<EmailModel> emails;

    private List<IMModel> ims;

    private List<AccountModel> accounts;

    private List<UrlModel> urls;

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public List<PhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoModel> photos) {
        this.photos = photos;
    }

    public ProfileBackgroundModel getProfileBackground() {
        return profileBackground;
    }

    public void setProfileBackground(ProfileBackgroundModel profileBackground) {
        this.profileBackground = profileBackground;
    }

    public NameModel getName() {
        return name;
    }

    public void setName(NameModel name) {
        this.name = name;
    }

    public List<PhoneNumberModel> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberModel> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<EmailModel> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailModel> emails) {
        this.emails = emails;
    }

    public List<IMModel> getIms() {
        return ims;
    }

    public void setIms(List<IMModel> ims) {
        this.ims = ims;
    }

    public List<AccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountModel> accounts) {
        this.accounts = accounts;
    }

    public List<UrlModel> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlModel> urls) {
        this.urls = urls;
    }

}
