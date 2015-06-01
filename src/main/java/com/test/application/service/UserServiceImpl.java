package com.test.application.service;


import com.test.application.model.UserEntity;
import com.test.application.repository.UserRepository;
import com.test.application.dto.UserDTO;
import com.test.application.service.exceptions.ResourceNotFoundException;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

//TODO: add mapping framework?

@Service("userService")
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    public UserServiceImpl(){}

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter)
    {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }


    @Override
    @Transactional
    public UserDTO create(UserDTO userDTO)
    {
        Validate.notNull(userDTO);

        UserEntity userEntity =
            new UserEntity(
                userDTO.getUserName(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmailAddress(),
                userDTO.getDateOfBirth()
            );

        UserEntity savedEntity = userRepository.save(userEntity);

        return userConverter.convertToUserDTO(savedEntity);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO)
    {
        Validate.notNull(id);
        Validate.notNull(userDTO);
        Validate.isTrue(id.equals(userDTO.getId()));

        validateEntityExists(id);

        // get the UserEntity from Hibernate and merge changes.
        UserEntity userEntity = userRepository.findOne(id);
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmailAddress(userDTO.getEmailAddress());
        userEntity.setDateOfBirth(userDTO.getDateOfBirth());

        UserEntity savedEntity = userRepository.save(userEntity);

        return userConverter.convertToUserDTO(savedEntity);

    }

    @Override
    @Transactional
    public boolean delete(Long id)
    {
        Validate.notNull(id);
        validateEntityExists(id);

        userRepository.delete(id);
        return true;
    }

    @Override
    public boolean exists(Long id)
    {
        Validate.notNull(id);
        return userRepository.exists(id);
    }

    @Override
    public long count()
    {
        return userRepository.count();
    }

    @Override
    public UserDTO getById(Long id)
    {
        Validate.notNull(id);
        validateEntityExists(id);
        UserEntity userEntity = userRepository.findOne(id);
        return userConverter.convertToUserDTO(userEntity);
    }

    @Override
    public UserDTO findByName(String name)
    {
        Validate.notEmpty(name);
        UserEntity userEntity = userRepository.findByName(name);
        return userConverter.convertToUserDTO(userEntity);
    }

    @Override
    public List<UserDTO> findAll()
    {
        List<UserEntity> userEntities = userRepository.findAll();
        return userConverter.convert(userEntities);

    }

    @Override
    public List<UserDTO> findAll(Set<Long> ids)
    {
        Validate.notEmpty(ids);
        Iterable<UserEntity> userEntities = userRepository.findAll(ids);
        return userConverter.convert(userEntities);
    }

    //TODO:paginated

    private void validateEntityExists(Long id)
    {
        boolean exists = exists(id);

        if(exists == false)
        {
            throw new ResourceNotFoundException("User with id '" + id +"' not found.");
        }
    }


}
