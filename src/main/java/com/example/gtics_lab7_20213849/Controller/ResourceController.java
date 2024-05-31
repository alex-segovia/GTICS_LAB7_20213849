package com.example.gtics_lab7_20213849.Controller;

import com.example.gtics_lab7_20213849.Repository.ResourceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@CrossOrigin
public class ResourceController {
    final ResourceRepository resourceRepository;

    public ResourceController(ResourceRepository resourceRepository){
        this.resourceRepository = resourceRepository;
    }

    @GetMapping(value = "/resource")
    public ResponseEntity<HashMap<String,Object>> listarRecursos(){
        HashMap<String,Object> responseJson = new HashMap<>();
        responseJson.put("listaRecursos",resourceRepository.findAll());
        return ResponseEntity.ok(responseJson);
    }
}
