package com.test.application.model;

import com.test.application.TestHelper;

import java.util.Date;

public class UserEntityBuilder
{
    private Long id;
    private String username = "test";
    private String firstName = "test";
    private String lastName = "test";
    private String emailAddress = "test@test.local";
    private Date dateOfBirth = TestHelper.getYesterday();

    public UserEntityBuilder withId(Long id)
    {
        this.id = id;
        return this;
    }

    public UserEntityBuilder withUsername(String username)
    {
        this.username = username;
        return this;
    }

    public UserEntityBuilder withFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public UserEntityBuilder withLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public UserEntityBuilder withEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserEntityBuilder withDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserEntity build()
    {
        return new UserEntity(id, username, firstName, lastName, emailAddress, dateOfBirth);
    }

}
