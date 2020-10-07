package jobsearchjournal.applicationprocess;

/**
 * Created by Christina Aiello on 3/22/2015.
 */
public class Interview {
    private String date;
    private String interviewID;
    private String companyID;
    private String time;
    private String interviewerNames;
    private String contactEmailAddress;
    private String miscNotes;
    private String interviewCompletedNotes;

    public Interview() {
    }

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

    public String getInterviewerNames() {
        return interviewerNames;
    }

    public void setInterviewerNames(String interviewerNames) {
        this.interviewerNames = interviewerNames;
    }

    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    public void setContactEmailAddress(String contactEmailAddress) {
        this.contactEmailAddress = contactEmailAddress;
    }

    public String getMiscNotes() {
        return miscNotes;
    }

    public void setMiscNotes(String miscNotes) {
        this.miscNotes = miscNotes;
    }

    public String getInterviewCompletedNotes() {
        return interviewCompletedNotes;
    }

    public void setInterviewCompletedNotes(String interviewCompletedNotes) {
        this.interviewCompletedNotes = interviewCompletedNotes;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getInterviewID() {
        return interviewID;
    }

    public void setInterviewID(String interviewID) {
        this.interviewID = interviewID;
    }
}