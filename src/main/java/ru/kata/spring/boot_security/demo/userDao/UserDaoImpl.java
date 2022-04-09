package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.userDao.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User getUserById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public void save(User user) {
        entityManager.persist(user);
    }

    public void update(User updatedUser) {
        entityManager.merge(updatedUser);
    }

    public void delete(Integer id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public List<User> getDemandedUsers() {
        List<User> list1 = entityManager.createQuery("select user from User user", User.class).getResultList();
        return list1;
    }

    public User findByUsername(String usernameReq) {
        User user = null;

        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :userRequest", User.class);
            user = query.setParameter("userRequest", usernameReq).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            System.out.printf("User '%s' not found%n", usernameReq);
        } finally {
            entityManager.close();
        }
        return user;
    }
}
