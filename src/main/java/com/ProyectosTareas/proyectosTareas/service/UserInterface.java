package com.ProyectosTareas.proyectosTareas.service;

import com.ProyectosTareas.proyectosTareas.domain.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public interface UserInterface {

    public List<User> listUser();

    public void saveUser(User user);

    public void deleteUser(User user);

    public User findUser(User user);

    public User findByIdUser(BigInteger id);

    public boolean updateUser(User user);

    public Optional<User> findByEmailAndPassword(String Email, String Password);
}
