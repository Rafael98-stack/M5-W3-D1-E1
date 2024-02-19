package it.be.epicode.EsercizioUno.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import it.be.epicode.EsercizioUno.entities.User;
import it.be.epicode.EsercizioUno.exceptions.BadRequestException;
import it.be.epicode.EsercizioUno.exceptions.NotFoundException;
import it.be.epicode.EsercizioUno.payloads.usersDto.NewUserDTO;
import it.be.epicode.EsercizioUno.repositories.UsersDAO;

import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersDAO usersDAO;


    public Page<User> getUsers(int pageNumber, int size, String orderBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(orderBy));
        return usersDAO.findAll(pageable);
    }

    public User saveUser(NewUserDTO payload) {

        usersDAO.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
        });

        User newUser = new User(payload.name(), payload.surname(),
                payload.email(), payload.password(),
                "https://ui-avatars.com/api/?name" + payload.name() + "+" + payload.surname());

        return usersDAO.save(newUser);
    }

    public User findById(UUID userId) {
        return usersDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByIdAndUpdate(UUID userId, User modifiedUser) {
        User found = this.findById(userId);
        found.setSurname(modifiedUser.getSurname());
        found.setName(modifiedUser.getName());
        found.setEmail(modifiedUser.getEmail());
        found.setPassword(modifiedUser.getPassword());
        return usersDAO.save(found);
    }

    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        usersDAO.delete(found);
    }

    public User findByEmail(String email) {
        return usersDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Email " + email + " non trovata"));
    }


}