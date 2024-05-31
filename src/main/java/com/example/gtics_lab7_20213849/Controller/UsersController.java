package com.example.gtics_lab7_20213849.Controller;

import com.example.gtics_lab7_20213849.Entity.Users;
import com.example.gtics_lab7_20213849.Repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
public class UsersController {
    final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<HashMap<String,Object>> agregar(@RequestBody Users user){
        HashMap<String,Object> responseMap = new HashMap<>();

    }

    @GetMapping(value = "/users/{resource}")
    public ResponseEntity<HashMap<String,Object>> listar(@PathVariable("resource") String resource){
        HashMap<String,Object> responseMap = new HashMap<>();

    }
}
