package com.ProyectosTareas.proyectosTareas.dao;

import com.ProyectosTareas.proyectosTareas.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;

@Repository
public interface UserDao extends CrudRepository<User, BigInteger> {
}
