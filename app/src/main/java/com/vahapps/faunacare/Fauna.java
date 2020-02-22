package com.vahapps.faunacare;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Fauna {
    public String description;
    //type
    public String type;
    public String url,treatedUrl;
    public String latitude,longitude,uploaderId,volId,vetId;
    public String status;
    public String location;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String date;
    public String time;
    public String isBlocked,isReport;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Fauna() {}

    public String getTreatedUrl() {
        return treatedUrl;
    }

    public void setTreatedUrl(String treatedUrl) {
        this.treatedUrl = treatedUrl;
    }

    public Fauna(String uploaderId, String description, String type, String url, String latitude, String longitude, String status, String location, String volId, String vetId, String isBlocked, String isReport, String date, String time, String treatedUrl) {
        this.uploaderId = uploaderId;
        this.description = description;
        this.type = type;
        this.url= url;
        this.latitude=latitude;
        this.longitude=longitude;
        this.status=status;
        this.location=location;
        this.volId=volId;
        this.vetId=vetId;

        this.date = date;
        this.time = time;
        this.isReport=isReport;
        this.isBlocked=isBlocked;
        this.treatedUrl=treatedUrl;
    }


    public String getDescription() {return description;}
    public String getType() {return type;}
    public String getUrl() {return url;}
    public String getLatitude() {return latitude;}
    public String getLongitude() {return longitude;}
    public String getUploaderId() {return uploaderId;}
    public String getStatus() {return status;}
    public String getLocation() {return location;}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getVolId() {
        return volId;
    }

    public void setVolId(String volId) {
        this.volId = volId;
    }

    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getIsReport() {
        return isReport;
    }

    public void setIsReport(String isReport) {
        this.isReport = isReport;
    }
}
