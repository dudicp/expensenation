package com.test.application.repository;


import com.test.application.model.UserEntity;
import com.test.application.model.UserEntityBuilder;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml", "classpath:test-servlet.xml", "classpath:jpaContext.xml"})
@Transactional
public class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;

    //******************************************************************************************************************
    //** save
    //******************************************************************************************************************

    @Test
    public void testSaveNewEntity()
    {
        UserEntity user1 = new UserEntityBuilder().withId(null).build();
        UserEntity result = userRepository.save(user1);
        userRepository.flush();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId()); // verify Id was generated properly.
        Assert.assertEquals(user1, result);
    }

    @Test
    public void testSaveExistingEntity()
    {
        UserEntity user1 = new UserEntityBuilder().withId(null).build();
        UserEntity createResult = userRepository.save(user1);
        userRepository.flush();

        UserEntity updatedUser1 = new UserEntityBuilder().withId(createResult.getId()).withFirstName("UpdatedName").build();
        UserEntity updateResult = userRepository.save(updatedUser1);
        userRepository.flush();

        Assert.assertNotNull(updateResult);
        Assert.assertEquals(createResult.getId(), updateResult.getId()); // verify the Id wasn't changed.
        Assert.assertEquals("UpdatedName", updateResult.getFirstName());
        Assert.assertEquals(updatedUser1, updateResult);
    }


    //******************************************************************************************************************
    //** FindOne
    //******************************************************************************************************************

    @Test
    public void testFindOne()
    {
        UserEntity user = new UserEntityBuilder().withId(null).build();
        UserEntity createdUser = userRepository.save(user);
        userRepository.flush();

        UserEntity result = userRepository.findOne(createdUser.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(user, result);
    }

    @Test
    public void testFindOneWithUnknownId()
    {
        UserEntity result = userRepository.findOne(1L);
        Assert.assertNull(result);
    }

    //******************************************************************************************************************
    //** FindByName
    //******************************************************************************************************************

    @Test
    public void testFindByName()
    {
        UserEntity user = new UserEntityBuilder().withId(null).withUsername("Dennis").build();
        UserEntity createdUser = userRepository.save(user);
        userRepository.flush();

        UserEntity result1 = userRepository.findByName("Dennis");
        Assert.assertNotNull(result1);
        Assert.assertEquals(user, result1);

        UserEntity result2 = userRepository.findByName("DENNIS");
        Assert.assertNotNull(result2);
        Assert.assertEquals(user, result2);
    }

    @Test
    public void testFindByNameWithUnknownName()
    {
        UserEntity result = userRepository.findByName("Unknown");
        Assert.assertNull(result);
    }

    // TODO: test findAll
    // TODO: test findAll(paginated)
    // TODO: test exists
    // TODO: test delete
    // TODO: test count
}
