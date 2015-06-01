package com.test.application.service;


import com.test.application.model.UserEntity;
import com.test.application.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter
{
    public List<UserDTO> convert(Iterable<UserEntity> userEntities)
    {
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();

        if(userEntities != null)
        {
            for(UserEntity userEntity : userEntities)
            {
                UserDTO userDTO = convertToUserDTO(userEntity);
                userDTOList.add(userDTO);
            }
        }

        return userDTOList;
    }

    public UserDTO convertToUserDTO(UserEntity userEntity)
    {
        UserDTO userDTO = null;

        if(userEntity != null)
        {
            userDTO =
                new UserDTO(
                    userEntity.getId(),
                    userEntity.getUserName(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getEmailAddress(),
                    userEntity.getDateOfBirth()
                );
        }

        return userDTO;
    }
}
