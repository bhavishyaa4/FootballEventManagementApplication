package com.example.firebase.ModelClass;

public class PlayerDetails {
    private String keeper1;
    private String keeper2;
    private String defence1;
    private String defence2;
    private String defence3;
    private String defence4;
    private String defence5;
    private String midfield1;
    private String midfield2;
    private String midfield3;
    private String midfield4;
    private String midfield5;
    private String striker1;
    private String striker2;
    private String striker3;
    private String teamId;

    public PlayerDetails(String keeper1, String keeper2, String defence1, String defence2, String defence3, String defence4, String defence5,
                         String midfield1, String midfield2, String midfield3, String midfield4, String midfield5, String striker1, String striker2,
                         String striker3, String teamId) {
        this.keeper1 = keeper1;
        this.keeper2 = keeper2;
        this.defence1 = defence1;
        this.defence2 = defence2;
        this.defence3 = defence3;
        this.defence4 = defence4;
        this.defence5 = defence5;
        this.midfield1 = midfield1;
        this.midfield2 = midfield2;
        this.midfield3 = midfield3;
        this.midfield4 = midfield4;
        this.midfield5 = midfield5;
        this.striker1 = striker1;
        this.striker2 = striker2;
        this.striker3 = striker3;
        this.teamId = teamId;
    }

    // Getter and Setter methods
    public String getKeeper1() {
        return keeper1;
    }

    public void setKeeper1(String keeper1) {
        this.keeper1 = keeper1;
    }

    public String getKeeper2() {
        return keeper2;
    }

    public void setKeeper2(String keeper2) {
        this.keeper2 = keeper2;
    }

    public String getDefence1() {
        return defence1;
    }

    public void setDefence1(String defence1) {
        this.defence1 = defence1;
    }

    public String getDefence2() {
        return defence2;
    }

    public void setDefence2(String defence2) {
        this.defence2 = defence2;
    }

    public String getDefence3() {
        return defence3;
    }

    public void setDefence3(String defence3) {
        this.defence3 = defence3;
    }

    public String getDefence4() {
        return defence4;
    }

    public void setDefence4(String defence4) {
        this.defence3 = defence4;
    }

    public String getDefence5() {
        return defence5;
    }

    public void setDefence5(String defence5) {
        this.defence5 = defence5;
    }

    public String getMidfield1() {
        return midfield1;
    }

    public void setMidfield1(String midfield1) {
        this.midfield1 = midfield1;
    }

    public String getMidfield2() {
        return midfield2;
    }

    public void setMidfield2(String midfield2) {
        this.midfield2 = midfield2;
    }

    public String getMidfield3() {
        return midfield3;
    }

    public void setMidfield3(String midfield3) {
        this.midfield3 = midfield3;
    }

    public String getMidfield4() {
        return midfield4;
    }

    public void setMidfield4(String midfield4) {
        this.midfield4 = midfield4;
    }

    public String getMidfield5() {
        return midfield5;
    }

    public void setMidfield5(String midfield5) {
        this.midfield5 = midfield5;
    }

    public String getStriker1() {
        return striker1;
    }

    public void setStriker1(String striker1) {
        this.striker1 = striker1;
    }

    public String getStriker2() {
        return striker2;
    }

    public void setStriker2(String striker2) {
        this.striker2 = striker2;
    }

    public String getStriker3() {
        return striker3;
    }

    public void setStriker3(String striker3) {
        this.striker3 = striker3;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

}
