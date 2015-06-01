package com.test.application.controller;

import com.test.application.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.Set;


public interface UserController
{
    public UserDTO create(UserDTO userDTO);

    public UserDTO update(long id, UserDTO userDTO);

    public boolean delete(long id);

    public UserDTO getById(long id);

    public UserDTO findByName(String name);

    public Iterable<UserDTO> findByIds(Set<Long> ids);

    public Iterable<UserDTO> findAll();

    public Page<UserDTO> findPaginated(Integer page, Integer size, String direction, String sortByProperties);

}
