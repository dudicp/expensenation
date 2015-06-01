package com.test.application.dto;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * This class represents the Data Transfer Object (DTO) of User in the system.
 * DTO is used to transfer objects between the UI/External API to the data model.
 *
 * TODO: should it perform validation?
 */
@XmlRootElement(name = "user")
@XmlType(propOrder={"id", "userName", "firstName", "lastName", "emailAddress", "dateOfBirth"})
public class UserDTO
{
    private Long id;

    @NotEmpty
    @Size(min=4, max=20)
    private String userName;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String emailAddress;

    @NotNull
    @Past
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dateOfBirth;


    public UserDTO(Long id, String userName, String firstName, String lastName, String emailAddress, Date dateOfBirth)
    {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
    }

    // required for JAXB
    private UserDTO(){}

    @XmlElement(name="id")
    public Long getId()
    {
        return id;
    }

    // required for JAXB
    private void setId(Long id)
    {
        this.id = id;
    }

    @XmlElement(name="userName")
    public String getUserName()
    {
        return userName;
    }

    // required for JAXB
    private void setUserName(String userName)
    {
        this.userName = userName;
    }

    @XmlElement(name="firstName")
    public String getFirstName()
    {
        return firstName;
    }

    // required for JAXB
    private void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @XmlElement(name="lastName")
    public String getLastName()
    {
        return lastName;
    }

    // required for JAXB
    private void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @XmlElement(name="emailAddress")
    public String getEmailAddress()
    {
        return emailAddress;
    }

    // required for JAXB
    private void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    @XmlElement(name="dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    // required for JAXB
    private void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (!dateOfBirth.equals(userDTO.dateOfBirth)) return false;
        if (!emailAddress.equals(userDTO.emailAddress)) return false;
        if (!firstName.equals(userDTO.firstName)) return false;
        if (id != null ? !id.equals(userDTO.id) : userDTO.id != null) return false;
        if (!lastName.equals(userDTO.lastName)) return false;
        if (!userName.equals(userDTO.userName)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + userName.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + emailAddress.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        return result;
    }
}
