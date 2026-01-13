package com.estudo.course.services;

import com.estudo.course.entities.User;
import com.estudo.course.repositories.UserRepository;
import com.estudo.course.services.exceptions.DatabaseException;
import com.estudo.course.services.exceptions.ResouceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //Ou @Component
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll(){
        return repository.findAll();
    }

    public User findById(Long id){
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new ResouceNotFoundException(id));
    }

    public User insert(User user){
        return repository.save(user);
    }

    public void delete(Long id){
        try {
            repository.delete(findById(id));
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update(Long id, User user){
        try {
            User entity = repository.getReferenceById(id);

            updateData(entity, user);
            return repository.save(entity);
        } catch (EntityNotFoundException e){
            throw new ResouceNotFoundException(id);
        }
    }

    private void updateData(User entity, User user) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
    }
}
