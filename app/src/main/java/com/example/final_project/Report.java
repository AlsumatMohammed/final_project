package com.example.final_project;


public class Report {
    public String userEmail;

    public Report() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getReportedAdKey() {
        return reportedAdKey;
    }

    public void setReportedAdKey(String reportedAdKey) {
        this.reportedAdKey = reportedAdKey;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public String complaint;
    public String reportedAdKey;
    public String reportMessage;
}
