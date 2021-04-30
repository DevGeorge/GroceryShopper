package com.fastfoods;

public class Accounts {

    int id;
    String firstName;
    String lastName;
    String name;
    String streetNumber;
    String city;
    String postal;
    String email;
    int account_type;

    public Accounts(int account_number, String first_name, String last_name, String street_number, String city1, String postal_code, String email, int type) {
        setId(account_number);
        setFirstName(first_name);
        setLastName(last_name);
        setStreetNumber(street_number);
        setCity(city1);
        setPostal(postal_code);
        setEmail(email);
        setAccount_type(type);
        setName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = firstName + " " + lastName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }


}
