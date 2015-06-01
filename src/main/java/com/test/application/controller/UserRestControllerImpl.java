package com.test.application.controller;


import com.test.application.service.UserService;
import com.test.application.dto.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

//TODO: error handling (using HTTP Status Codes)
//TODO: how to handle validations? automatically as in here?

@RestController
@RequestMapping(value = "/users")
public class UserRestControllerImpl implements UserController
{
    private static final Logger LOG = Logger.getLogger(UserRestControllerImpl.class);

    @Autowired
    private UserService userService;


    @RequestMapping(method =  RequestMethod.POST)
    public UserDTO create(@RequestBody @Valid UserDTO userDTO)
    {
        LOG.info("handling create API");
        UserDTO createdUserDTO = userService.create(userDTO);
        return createdUserDTO;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public UserDTO update(@PathVariable("id") long id, @RequestBody @Valid UserDTO userDTO)
    {
        LOG.info("handling update API");
        UserDTO updatedUserDTO = userService.update(id, userDTO);
        return updatedUserDTO;
    }

    @RequestMapping(value="/{id}", method =  RequestMethod.DELETE)
    public boolean delete(@PathVariable long id)
    {
        LOG.info("handling delete API");
        boolean result = userService.delete(id);
        return result;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public UserDTO getById(@PathVariable("id") long id)
    {
        LOG.info("handling getById API");
        UserDTO result = userService.getById(id);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, params = "name")
    public UserDTO findByName(@RequestParam(value="name", required = true) String name)
    {
        LOG.info("handling getByName API");
        UserDTO result = userService.findByName(name);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, params = "ids[]")
    public Iterable<UserDTO> findByIds(@RequestParam(value="ids[]") Set<Long> ids)
    {
        LOG.info("handling findByIds API");
        List<UserDTO> result = userService.findAll(ids);

        if(result == null)
            result = new ArrayList<UserDTO>();

        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<UserDTO> findAll()
    {
        LOG.info("handling list API");
        List<UserDTO> result = userService.findAll();

        if(result == null)
            result = new ArrayList<UserDTO>();

        return result;
    }

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public Page<UserDTO> findPaginated(
        @RequestParam(value="page",required=false,defaultValue="1") Integer page,
        @RequestParam(value="size",required=false,defaultValue="10") Integer size,
        @RequestParam(value="direction",required=false,defaultValue="ASC") String direction,
        @RequestParam(value="sortByProperties",required=false) String sortByProperties)
    {
        LOG.info("handling list API");
        return null;
//        UserDTO userDTO = buildMockUserDTO();
//        List<UserDTO> result = new ArrayList<UserDTO>();
//        result.add(userDTO);
//
//        Page<UserDTO> pageResult = new PageImpl<UserDTO>(result);
//
//        return pageResult;
    }
}
