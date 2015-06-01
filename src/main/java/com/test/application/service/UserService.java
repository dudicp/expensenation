package com.test.application.service;

import com.test.application.dto.UserDTO;

import java.util.List;
import java.util.Set;


public interface UserService
{
    public UserDTO create(UserDTO userDTO);

    public UserDTO update(Long id, UserDTO userDTO);

    public boolean delete(Long id);

    public boolean exists(Long id);

    public long count();

    public UserDTO getById(Long id);

    public UserDTO findByName(String name);

    public List<UserDTO> findAll();

    public List<UserDTO> findAll(Set<Long> ids);


}
