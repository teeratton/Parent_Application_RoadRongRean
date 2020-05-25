package com.teerat.parent_map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Parent implements Parcelable {
    private String firstName;
    private String lastName;
    private String contactNo;
    private String username;
    private String password;
    private List<Integer> studentList;

    public Parent() {

    }

    public Parent(String firstName, String lastName, String contactNo, String username, String password, List<Integer> studentList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.username = username;
        this.password = password;
        this.studentList = studentList;
    }

    protected Parent(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        contactNo = in.readString();
        username = in.readString();
        password = in.readString();
        studentList = new ArrayList<>();
        in.readList(studentList, Integer.class.getClassLoader());

    }

    public static final Creator<Parent> CREATOR = new Creator<Parent>() {
        @Override
        public Parent createFromParcel(Parcel in) {
            return new Parent(in);
        }

        @Override
        public Parent[] newArray(int size) {
            return new Parent[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Integer> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(contactNo);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeList(studentList);
    }
}
