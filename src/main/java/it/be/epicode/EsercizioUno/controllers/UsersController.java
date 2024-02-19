package it.be.epicode.EsercizioUno.controllers;



/* -------------------------------------------------------- USERS CRUD -----------------------------------------------

1. GET http://localhost:3001/users
2. POST http://localhost:3001/users (+ body)
3. GET http://localhost:3001/users/{id}
4. PUT http://localhost:3001/users/{id} (+ body)
5. DELETE http://localhost:3001/users/{id}

*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import it.be.epicode.EsercizioUno.entities.User;
import it.be.epicode.EsercizioUno.service.UsersService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;


    @GetMapping
    public Page<User> getAllUsers(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.usersService.getUsers(page, size, orderBy);
    }


    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return this.usersService.findById(id);
    }



    @PutMapping("/{id}")
    public User findByIdAndUpdate(@PathVariable UUID id, @RequestBody User updatedUser) {

        return this.usersService.findByIdAndUpdate(id, updatedUser);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.usersService.findByIdAndDelete(id);
    }

}
