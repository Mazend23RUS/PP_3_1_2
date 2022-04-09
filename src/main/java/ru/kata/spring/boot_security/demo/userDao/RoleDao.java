package ru.kata.spring.boot_security.demo.userDao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.awt.*;
import java.util.List;

public interface RoleDao {
    void save(Role role);
    void update(Role updatedRole);
    Role getRoleById(Integer id);
    void delete(Integer id);
    List<Role> getDemandedRoles();
}
