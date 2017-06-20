package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.User;

import java.util.List;

public interface TeamMemberDao {

    User get(Long id);
    Long save(User user);
    void update(User user);
    void delete(User user);
    void remove(User user);
    List<User> findAll(String username);
    List<User> findAll();
    User findByUserName(String username);
    User findByNewName(String CurrentName,String newName);
}
