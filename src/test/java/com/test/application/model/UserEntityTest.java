package com.test.application.model;

import com.test.application.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;


public class UserEntityTest
{
    private Validator validator;

    @org.junit.Before
    public void setUp()
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void testValidMandatoryData()
    {
        Date yesterdayDate = TestHelper.getYesterday();

        UserEntity userEntity =
            new UserEntityBuilder().
                withUsername("username").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        Assert.assertNull(userEntity.getId());
        assertNotEmpty(userEntity.getUserName());
        assertNotEmpty(userEntity.getFirstName());
        assertNotEmpty(userEntity.getLastName());
        assertNotEmpty(userEntity.getEmailAddress());
        Assert.assertNotNull(userEntity.getDateOfBirth());
    }

    @Test
    public void testEmptyUsername()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withUsername("").
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(2, constraintViolations.size());
    }

    @Test
    public void testInvalidShortUsername()
    {
        UserEntity userEntity =
                new UserEntityBuilder().
                        withUsername("a").
                        build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testInvalidLongUsername()
    {
        UserEntity userEntity =
                new UserEntityBuilder().
                        withUsername(TestHelper.createStringWithLength(21)).
                        build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testEmptyFirstName()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withFirstName("").
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testEmptyLastName()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withLastName("").
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testEmptyEmailAddress()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withEmailAddress("").
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testInvalidEmailAddress()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withEmailAddress("test.com").
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testNullDateOfBirth()
    {
        UserEntity userEntity =
            new UserEntityBuilder().
                withDateOfBirth(null).
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    @Test
    public void testFutureDateOfBirth()
    {
        Date tomorrowDate = TestHelper.getTomorrow();

        UserEntity userEntity =
            new UserEntityBuilder().
                withDateOfBirth(tomorrowDate).
                build();

        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

        Assert.assertEquals(1, constraintViolations.size());
    }

    private void assertNotEmpty(String value)
    {
        Assert.assertNotNull(value);
        Assert.assertTrue(!value.isEmpty());
    }

}
