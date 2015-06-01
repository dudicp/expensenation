package com.test.application.service;

import com.test.application.TestHelper;
import com.test.application.dto.UserDTOBuilder;
import com.test.application.model.UserEntity;
import com.test.application.model.UserEntityBuilder;
import com.test.application.repository.UserRepository;
import com.test.application.dto.UserDTO;
import com.test.application.service.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest
{
    @Mock
    private UserRepository mockUserRepository;
    private UserConverter userConverter = new UserConverter();
    private UserServiceImpl userService;

    @Before
    public void setUp()
    {
        userService = new UserServiceImpl(mockUserRepository, userConverter);
    }

    //******************************************************************************************************************
    //** FindAll
    //******************************************************************************************************************

    @Test
    public void testFindAllEmptyResults()
    {
        // given
        List<UserEntity> repositoryResult = new ArrayList<UserEntity>(0);
        Mockito.when(mockUserRepository.findAll()).thenReturn(repositoryResult);

        // when
        List<UserDTO> result =  userService.findAll();

        // then
        // verify behavior
        Mockito.verify(mockUserRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        //  verify returned data
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAllNullResults()
    {
        // given
        Mockito.when(mockUserRepository.findAll()).thenReturn(null);

        // when
        List<UserDTO> result =  userService.findAll();

        // then
        //  verify behavior
        Mockito.verify(mockUserRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        //  verify returned data
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll()
    {
        Date yesterdayDate = TestHelper.getYesterday();

        // given
        UserEntity user1 =
            new UserEntityBuilder().
                withId(1L).
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        UserEntity user2 =
            new UserEntityBuilder().
                withId(2L).
                withUsername("username2").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        Mockito.when(mockUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // when
        List<UserDTO> result =  userService.findAll();

        // then
        //  verify behavior
        Mockito.verify(mockUserRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        //  verify returned data
        Assert.assertTrue(result.size() == 2);
        List<UserDTO> convertedUserEntities = userConverter.convert(Arrays.asList(user1, user2));
        Assert.assertEquals(convertedUserEntities, result);
    }

    //******************************************************************************************************************
    //** GetById
    //******************************************************************************************************************

    @Test
    public void testGetById()
    {
        Date yesterdayDate = TestHelper.getYesterday();

        // given
        UserEntity user1 =
            new UserEntityBuilder().
                withId(1L).
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
        Mockito.when(mockUserRepository.findOne(1L)).thenReturn(user1);

        // when
        UserDTO result = userService.getById(1L);

        // then
        Mockito.verify(mockUserRepository, Mockito.times(1)).exists(1L);
        Mockito.verify(mockUserRepository, Mockito.times(1)).findOne(1L);
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        UserDTO convertedUserEntity = userConverter.convertToUserDTO(user1);
        Assert.assertEquals(convertedUserEntity, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNullId()
    {
        // when
        UserDTO result = userService.getById(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetByIdNotFound()
    {
        // given
        Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);

        // when
        UserDTO result = userService.getById(1L);

        // then
        // FIXME: it's never checked.
        Mockito.verify(mockUserRepository, Mockito.times(1)).exists(1L);
        Mockito.verifyNoMoreInteractions(mockUserRepository);
    }


    //******************************************************************************************************************
    //** Create
    //******************************************************************************************************************

    @Test
    public void testCreate()
    {
        // given
        Date yesterdayDate = TestHelper.getYesterday();
        UserDTO userDTO =
            new UserDTOBuilder().
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        UserEntity userEntity =
            new UserEntityBuilder().
                withId(1L).
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        ArgumentCaptor<UserEntity> userEntityArgument = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.when(mockUserRepository.save(userEntityArgument.capture())).thenReturn(userEntity);

        // when
        UserDTO result = userService.create(userDTO);

        // then

        // assert behavior
        Mockito.verify(mockUserRepository, Mockito.times(1)).save(userEntityArgument.capture());
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        // assert the return entity is the same as the requested and also contains identifier (auto generated).
        Assert.assertNotNull(result.getId());
        Assert.assertEquals((Long) 1L, result.getId());
        Assert.assertEquals(userDTO.getUserName(), result.getUserName());
        Assert.assertEquals(userDTO.getFirstName(), result.getFirstName());
        Assert.assertEquals(userDTO.getLastName(), result.getLastName());
        Assert.assertEquals(userDTO.getEmailAddress(), result.getEmailAddress());
        Assert.assertEquals(userDTO.getDateOfBirth(), result.getDateOfBirth());
    }

    //******************************************************************************************************************
    //** Update
    //******************************************************************************************************************

    @Test
    public void testUpdate()
    {
        // given
        Date yesterdayDate = TestHelper.getYesterday();
        UserDTO userDTO =
            new UserDTOBuilder().
                withId(1L).
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        UserEntity userEntity =
            new UserEntityBuilder().
                withId(1L).
                withUsername("username1").
                withFirstName("firstName").
                withLastName("lastName").
                withEmailAddress("test@test.local").
                withDateOfBirth(yesterdayDate).
                build();

        Mockito.when(mockUserRepository.exists(1L)).thenReturn(true);
        Mockito.when(mockUserRepository.findOne(1L)).thenReturn(userEntity);
        Mockito.when(mockUserRepository.save(userEntity)).thenReturn(userEntity);

        // when
        UserDTO result = userService.update(1L, userDTO);

        // then

        // assert behavior
        Mockito.verify(mockUserRepository, Mockito.times(1)).exists(1L);
        Mockito.verify(mockUserRepository, Mockito.times(1)).findOne(1L);
        Mockito.verify(mockUserRepository, Mockito.times(1)).save(userEntity);
        Mockito.verifyNoMoreInteractions(mockUserRepository);

        // assert the return entity is the same as the requested and also contains identifier (auto generated).
        Assert.assertEquals(userDTO, result);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullId()
    {
        UserDTO userDTO = new UserDTOBuilder().build();
        userService.update(null, userDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullUser()
    {
        userService.update(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIncompatibleId()
    {
        UserDTO userDTO = new UserDTOBuilder().withId(1L).build();
        userService.update(2L, userDTO);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNotFound()
    {
        // given
        UserDTO userDTO = new UserDTOBuilder().withId(1L).build();
        Mockito.when(mockUserRepository.exists(1L)).thenReturn(false);

        // when
        UserDTO result = userService.update(1L, userDTO);
    }

    // TODO: test delete
    // TODO: test exists
    // TODO: test count
    // TODO: test findByName
    // TODO: test findAll(ids)
    // TODO: test findPaginated

}
