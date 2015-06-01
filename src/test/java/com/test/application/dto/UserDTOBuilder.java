package com.test.application.dto;


import com.test.application.TestHelper;

import java.util.Date;

public class UserDTOBuilder
{
    private Long id = 1L;
    private String username = "test";
    private String firstName = "test";
    private String lastName = "test";
    private String emailAddress = "test@test.local";
    private Date dateOfBirth = TestHelper.getYesterday();

    public UserDTOBuilder withId(Long id)
    {
        this.id = id;
        return this;
    }

    public UserDTOBuilder withUsername(String username)
    {
        this.username = username;
        return this;
    }

    public UserDTOBuilder withFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public UserDTOBuilder withLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public UserDTOBuilder withEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserDTOBuilder withDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserDTO build()
    {
        return new UserDTO(id, username, firstName, lastName, emailAddress, dateOfBirth);
    }
}
