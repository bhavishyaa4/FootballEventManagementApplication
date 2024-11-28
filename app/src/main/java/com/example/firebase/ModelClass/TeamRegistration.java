package com.example.firebase.ModelClass;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TeamRegistration {

    private String teamName;
    private String teamAddress;
    private String coachName;
    private String contactNumber;
    private String email;
    private String teamId;

    // Default constructor required for Firestore deserialization
    public TeamRegistration() {
    }

    // Constructor to initialize the fields
    public TeamRegistration(String teamName, String teamAddress, String coachName, String contactNumber, String email, String teamId) {
        this.teamName = teamName;
        this.teamAddress = teamAddress;
        this.coachName = coachName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.teamId = teamId;
    }

    // Getter and Setter methods for Firestore serialization/deserialization
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamAddress() {
        return teamAddress;
    }

    public void setTeamAddress(String teamAddress) {
        this.teamAddress = teamAddress;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
//package com.example.firebase.ModelClass;
//
//public class TeamRegistration {
//
//    private String teamName;
//    private String teamAddress;
//    private String coachName;
//    private String contactNumber;
//    private String email;
//    private String teamId;
//    private String tournamentId; // Add tournamentId field
//
//    // Default constructor for Firestore serialization
//    public TeamRegistration() {}
//
//    // Constructor with all fields
//    public TeamRegistration(String teamName, String teamAddress, String coachName, String contactNumber,
//                            String email, String teamId, String tournamentId) {
//        this.teamName = teamName;
//        this.teamAddress = teamAddress;
//        this.coachName = coachName;
//        this.contactNumber = contactNumber;
//        this.email = email;
//        this.teamId = teamId;
//        this.tournamentId = tournamentId; // Initialize tournamentId
//    }
//
//    // Getters and setters for all fields
//    public String getTeamName() {
//        return teamName;
//    }
//
//    public void setTeamName(String teamName) {
//        this.teamName = teamName;
//    }
//
//    public String getTeamAddress() {
//        return teamAddress;
//    }
//
//    public void setTeamAddress(String teamAddress) {
//        this.teamAddress = teamAddress;
//    }
//
//    public String getCoachName() {
//        return coachName;
//    }
//
//    public void setCoachName(String coachName) {
//        this.coachName = coachName;
//    }
//
//    public String getContactNumber() {
//        return contactNumber;
//    }
//
//    public void setContactNumber(String contactNumber) {
//        this.contactNumber = contactNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }
//
//    public String getTournamentId() {
//        return tournamentId;
//    }
//
//    public void setTournamentId(String tournamentId) {
//        this.tournamentId = tournamentId;
//    }
//}
