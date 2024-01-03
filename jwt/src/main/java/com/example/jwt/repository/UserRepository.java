package com.example.jwt.repository;

import com.example.jwt.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MyUser,Long> {
    public MyUser findByUsername(String username);
}
