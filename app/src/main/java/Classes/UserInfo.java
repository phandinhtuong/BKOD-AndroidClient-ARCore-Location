package Classes;

import java.util.Date;

// Thong tin user
public class UserInfo {
    // User id
    private int userId;
    // Ten dang nhap (email)
    private String userName;
    // Ten day du
    private String fullName;
    // Ngay sinh
    private Date birthday;
    // Gioi tinh
    private int gender;
    // Truong trung hoc
    private String school;
    // Lop dang hoc
    private String classroom;
    // So dien thoai nguoi dung
    private String phoneNumber;
    // Danh dau co phai tu van vien khong
    private boolean isCounselor;
    // Trang thai dang nhap (0: Offline, 1: Online, 2: Busy, 3: Hidden)
    private int state;
    // Constructor
    public UserInfo(int iUserId, String sUserName, String sFullname, Date dBirthday, int bGender,
                    String sSchool, String sClassroom, String sPhoneNumber, boolean bIsCounselor, int bState){
        this.userId = iUserId;
        this.userName = sUserName;
        this.fullName = sFullname;
        this.birthday = dBirthday;
        this.gender = bGender;
        this.school = sSchool;
        this.classroom = sClassroom;
        this.phoneNumber = sPhoneNumber;
        this.isCounselor = bIsCounselor;
        this.state = bState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCounselor() {
        return isCounselor;
    }

    public void setCounselor(boolean counselor) {
        isCounselor = counselor;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
