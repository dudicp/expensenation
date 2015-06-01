package com.test.application.controller;

import com.test.application.TestHelper;
import com.test.application.dto.UserDTOBuilder;
import com.test.application.service.UserService;
import com.test.application.dto.UserDTO;
import com.test.application.service.exceptions.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * This test using Spring's full initialized Web-Application Context in order to reuse
 * the same configurations files as mentioned in the @ContextConfiguration annotation.
 *
 * The tests in this layer are testing the Controller behavior only using mock service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml", "classpath:test-servlet.xml"})
@WebAppConfiguration
public class UserControllerTest
{
    private MockMvc mockMvc;

    @Autowired
    private UserService mockUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp()
    {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(mockUserService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //******************************************************************************************************************
    //** FindAll
    //******************************************************************************************************************

    @Test
    public void testFindAll() throws Exception
    {
        Date yesterdayDate = TestHelper.getYesterday();

        // 1. Create the data to return when the method is called.
        UserDTO userDTO1 =
            new UserDTOBuilder().
                withId(1L).
                withUsername("test1").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        UserDTO userDTO2 =
            new UserDTOBuilder().
                withId(2L).
                withUsername("test2").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        // 2. Configure our mock object to return the created test data when the method invoked.
        Mockito.when(mockUserService.findAll()).thenReturn(Arrays.asList(userDTO1, userDTO2));

        // 3. Perform the action.
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/"));

        // 4. Perform find all basic verifications.
        performFindAllCommonVerifications(resultActions, 2);

        // 5. Verify the user entries by using JsonPath expressions.
        verifyUsersInJsonResponse(resultActions, Arrays.asList(userDTO1, userDTO2));
    }

    @Test
    public void testFindAllWithEmptyResults() throws Exception
    {
        // 1. Configure our mock object to return the configured data when the method invoked.
        Mockito.when(mockUserService.findAll()).thenReturn(new ArrayList<UserDTO>(0));

        // 2. Perform the action.
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/"));

        // 3. Perform find all basic verifications.
        performFindAllCommonVerifications(resultActions, 0);
    }

    @Test
    public void testFindAllWithNullResults() throws Exception
    {
        // 1. Configure our mock object to return the configured data when the method invoked.
        Mockito.when(mockUserService.findAll()).thenReturn(null);

        // 2. Perform the action.
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/"));

        // 3. Perform find all basic verifications.
        performFindAllCommonVerifications(resultActions, 0);
    }


    private void performFindAllCommonVerifications(ResultActions resultActions, int size) throws Exception
    {
        // 1. Verify the result code is OK.
        TestHelper.performCommonVerifications(resultActions);

        // 3. Get the collection of user entries by using the JsonPath expression $ and ensure that that
        // no user entries are returned.
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(size)));

        // 4. Verify that the findAll() method of the UserService interface is called only once.
        Mockito.verify(mockUserService, Mockito.times(1)).findAll();

        // 5. Ensure that no other methods of our mock object are called during the test.
        Mockito.verifyNoMoreInteractions(mockUserService);
    }


    //******************************************************************************************************************
    //** GetById
    //******************************************************************************************************************

    @Test
    public void testGetById() throws Exception
    {
        Date yesterdayDate = TestHelper.getYesterday();

        // 1. Create the data to return when the method is called.
        UserDTO found =
            new UserDTOBuilder().
                withId(1L).
                withUsername("test1").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        // 2. Configure our mock object to return the created test data when the method invoked.
        Mockito.when(mockUserService.getById(1L)).thenReturn(found);

        // 3. Perform the action.
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users/{id}", 1L).accept(TestHelper.APPLICATION_JSON_UTF8)
            );

        // 4. Perform common verifications.
        TestHelper.performCommonVerifications(resultActions);

        // 5. Verify the user entry by using JsonPath expressions.
        verifyUserInJsonResponse(resultActions, found, null);

        // 6. Verify behavior.
        performGetByIdCommonBehaviorVerifications(1L);
    }

    @Test
    public void testGetByIdOfUnknownId() throws Exception
    {
        // 1. Configure our mock object to return the created test data when the method invoked.
        Mockito.when(mockUserService.getById(1L)).thenThrow(new ResourceNotFoundException("User with id '1' not found."));

        // 2. Perform the action.
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L));

        // 3. Verify the result code is HTTP NOT FOUND.
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());

        // 4. Verify behavior.
        performGetByIdCommonBehaviorVerifications(1L);
    }

    private void performGetByIdCommonBehaviorVerifications(long id)
    {
        // 1. Verify that the getById() method of the UserService interface is called only once.
        Mockito.verify(mockUserService, Mockito.times(1)).getById(id);

        // 2. Ensure that no other methods of our mock object are called during the test.
        Mockito.verifyNoMoreInteractions(mockUserService);
    }

    //******************************************************************************************************************
    //** Create
    //******************************************************************************************************************

    @Test
    public void testCreateUser() throws Exception
    {
        Date yesterdayDate = TestHelper.getYesterday();

        // 1. Create the data to return when the method is called.
        UserDTO userDTOParameter =
            new UserDTOBuilder().
                withId(null).
                withUsername("test1").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        UserDTO userDTOToReturn =
            new UserDTOBuilder().
                withId(1L).
                withUsername("test1").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        // 2. Configure our mock object to return the created test data when the method invoked.
        Mockito.when(mockUserService.create(userDTOParameter)).thenReturn(userDTOToReturn);

        // 3. Perform the action.
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/users/").
                    accept(TestHelper.APPLICATION_JSON_UTF8).
                    contentType(TestHelper.APPLICATION_JSON_UTF8).
                    content(TestHelper.convertObjectToJsonBytes(userDTOParameter)));

        // 4. Perform common verifications
        TestHelper.performCommonVerifications(resultActions);

        // 5. Verify the user entry by using JsonPath expressions.
        verifyUserInJsonResponse(resultActions, userDTOToReturn, null);

        // 6. Perform behavior verifications.
        performCreateCommonBehaviorVerifications(userDTOParameter);
    }

    @Test
    public void testCreateInvalidUser() throws Exception
    {
        // 1. Create the data to return when the method is called.
        UserDTO user =
            new UserDTOBuilder().
                withId(1L).
                withUsername("").
                withFirstName("test").
                withLastName("test").
                withEmailAddress("test.local").
                withDateOfBirth(TestHelper.getYesterday()).
                build();

        // 3. Perform the action.
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/users/").
                    accept(TestHelper.APPLICATION_JSON_UTF8).
                    contentType(TestHelper.APPLICATION_JSON_UTF8).
                    content(TestHelper.convertObjectToJsonBytes(user)));

        // 4. Verify the result code is HTTP BAD REQUEST.
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 5. Ensure that no other methods of our mock object are called during the test.
        Mockito.verifyNoMoreInteractions(mockUserService);
    }

    private void performCreateCommonBehaviorVerifications(UserDTO userDTO)
    {
        // 1. Verify that the create() method of the UserService interface is called only once.
        Mockito.verify(mockUserService, Mockito.times(1)).create(userDTO);

        // 2. Ensure that no other methods of our mock object are called during the test.
        Mockito.verifyNoMoreInteractions(mockUserService);
    }


    // TODO: test update
    // TODO: test delete
    // TODO: test findByName
    // TODO: test findByIds
    // TODO: test findPaginated

    //******************************************************************************************************************
    //** Common
    //******************************************************************************************************************

    private void verifyUserInJsonResponse(ResultActions resultActions, UserDTO userDTO, Integer jsonLocation) throws Exception
    {
        String jsonLocationStr = "$";

        if(jsonLocation != null)
        {
            jsonLocationStr = jsonLocationStr + "[" + jsonLocation + "]";
        }

        //FIXME: the matcher fails to compare the json value to Long?
        Integer id = null;
        if(userDTO.getId() != null)
            id = (int)((long)userDTO.getId());

        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".id", Matchers.is(id)));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".userName", Matchers.is(userDTO.getUserName())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".firstName", Matchers.is(userDTO.getFirstName())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".lastName", Matchers.is(userDTO.getLastName())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".emailAddress", Matchers.is(userDTO.getEmailAddress())));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath(jsonLocationStr + ".dateOfBirth", Matchers.is(userDTO.getDateOfBirth().getTime())));
    }

    private void verifyUsersInJsonResponse(ResultActions resultActions, List<UserDTO> users) throws Exception
    {
        if(users == null)
            return;

        int index = 0;
        for(UserDTO user : users)
        {
            verifyUserInJsonResponse(resultActions, user, index);
            index++;
        }
    }



}
