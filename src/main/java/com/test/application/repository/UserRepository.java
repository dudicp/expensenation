package com.test.application.repository;

import com.test.application.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    @Query("select u from UserEntity u where u.userName = :userName")
    public UserEntity findByName(@Param("userName") String userName);

}