package com.ProyectosTareas.proyectosTareas.service;

import com.ProyectosTareas.proyectosTareas.dao.UserDao;
import com.ProyectosTareas.proyectosTareas.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UserImp implements UserInterface{

    private final UserDao userDao;

    @Autowired
    public UserImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> listUser() {
        return (List<User>) userDao.findAll();
    }

    @Override
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    @Override
    public User findUser(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return userDao.findById(user.getId()).orElse(null);
    }

    @Override
    public User findByIdUser(BigInteger id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            return false;
        }

        if (userDao.existsById(user.getId())) {
            userDao.save(user);
            return true;
        }

        return false;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String Email, String Password) {
        return Optional.empty();
    }
}
