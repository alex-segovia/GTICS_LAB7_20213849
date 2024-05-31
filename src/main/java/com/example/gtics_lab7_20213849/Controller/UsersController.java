package com.example.gtics_lab7_20213849.Controller;

import com.example.gtics_lab7_20213849.Entity.Resource;
import com.example.gtics_lab7_20213849.Entity.Users;
import com.example.gtics_lab7_20213849.Repository.ResourceRepository;
import com.example.gtics_lab7_20213849.Repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class UsersController {
    final UsersRepository usersRepository;
    final ResourceRepository resourceRepository;

    public UsersController(UsersRepository usersRepository,
                           ResourceRepository resourceRepository){
        this.usersRepository = usersRepository;
        this.resourceRepository = resourceRepository;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<HashMap<String,Object>> agregar(@RequestBody Users user){
        HashMap<String,Object> responseMap = new HashMap<>();
        if(user.getType().equals("contador") || user.getType().equals("cliente") || user.getType().equals("analista de promociones") || user.getType().equals("analista logistico")){
            Resource resource;
            if(user.getType().equals("contador")){
                resource = resourceRepository.findByName("Servidor de contabilidad");
            }else if(user.getType().equals("cliente")){
                resource = resourceRepository.findByName("Servidor de clientes");
            }else if(user.getType().equals("analista de promociones")){
                resource = resourceRepository.findByName("Servidor de promociones");
            }else{
                resource = resourceRepository.findByName("impresora");
            }
            user.setAuthorizedResource(resource);
            user.setActive(1);
            usersRepository.save(user);
            responseMap.put("estado","creado");
            responseMap.put("usuario",user);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        }else{
            responseMap.put("msg","El tipo de usuario debe ser contador, cliente, analista de promociones o analista logistico");
        }
        responseMap.put("estado","error");
        return ResponseEntity.badRequest().body(responseMap);
    }

    @GetMapping(value = "/users/{resource}")
    public ResponseEntity<HashMap<String,Object>> listar(@PathVariable("resource") String resource){
        HashMap<String,Object> responseJson = new HashMap<>();
        try{
            int id = Integer.parseInt(resource);
        }catch (NumberFormatException e){
            responseJson.put("msg","El ID del recurso debe ser un número entero");
            responseJson.put("result","failure");
            return ResponseEntity.badRequest().body(responseJson);
        }
        if(Integer.parseInt(resource)==5 || Integer.parseInt(resource)==6 || Integer.parseInt(resource)==7 || Integer.parseInt(resource)==8){
            List<Users> listaUsers = usersRepository.listarPorRecurso(Integer.parseInt(resource));
            if(listaUsers!=null && !listaUsers.isEmpty()){
                responseJson.put("result","success");
                responseJson.put("listaUsurios",listaUsers);
                return ResponseEntity.ok(responseJson);
            }else{
                responseJson.put("msg","No se encontraron usuarios");
            }
        }else{
            responseJson.put("msg","Recurso no encontrado");
        }
        responseJson.put("result","failure");
        return ResponseEntity.badRequest().body(responseJson);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<HashMap<String,Object>> borrar(@PathVariable("id") String idStr){
        HashMap<String,Object> responseMap = new HashMap<>();
        try{
            int id = Integer.parseInt(idStr);
            if(usersRepository.existsById(id)){
                responseMap.put("usuario borrado",usersRepository.findById(id));
                usersRepository.deleteById(id);
                responseMap.put("estado","borrado exitoso");
                return ResponseEntity.ok(responseMap);
            }else{
                responseMap.put("estado","error");
                responseMap.put("msg","No se encontró el usuario con id: "+id);
                return ResponseEntity.badRequest().body(responseMap);
            }
        }catch (NumberFormatException e){
            responseMap.put("estado","error");
            responseMap.put("msg","El ID debe ser un número");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PutMapping(value = "/users")
    public ResponseEntity<HashMap<String,Object>> actualizarTipo(@RequestBody Users user){
        HashMap<String,Object> responseMap = new HashMap<>();
        if(user.getId() != null && user.getId()>0){
            Optional<Users> optionalUser = usersRepository.findById(user.getId());
            if(optionalUser.isPresent()){
                Users userFromDB = optionalUser.get();
                if(user.getName() != null){
                    userFromDB.setName(user.getName());
                }
                if(user.getType() != null){
                    if(user.getType().equals("contador") || user.getType().equals("cliente") || user.getType().equals("analista de promociones") || user.getType().equals("analista logistico")) {
                        userFromDB.setType(user.getType());
                        Resource resource;
                        if(user.getType().equals("contador")){
                            resource = resourceRepository.findByName("Servidor de contabilidad");
                        }else if(user.getType().equals("cliente")){
                            resource = resourceRepository.findByName("Servidor de clientes");
                        }else if(user.getType().equals("analista de promociones")){
                            resource = resourceRepository.findByName("Servidor de promociones");
                        }else{
                            resource = resourceRepository.findByName("impresora");
                        }
                        userFromDB.setAuthorizedResource(resource);
                    }else{
                        responseMap.put("estado","error");
                        responseMap.put("msg","El tipo de usuario debe ser contador, cliente, analista de promociones o analista logistico");
                        return ResponseEntity.badRequest().body(responseMap);
                    }
                }

                if(user.getActive() != null){
                    if(user.getActive()==0){
                        responseMap.put("estado","error");
                        responseMap.put("msg","El usuario a actualizar no esta autenticado");
                        return ResponseEntity.badRequest().body(responseMap);
                    }
                    userFromDB.setActive(user.getActive());
                }

                usersRepository.save(userFromDB);
                responseMap.put("estado","actualizado");
                responseMap.put("usuario",userFromDB);
                return ResponseEntity.ok(responseMap);
            }else{
                responseMap.put("estado","error");
                responseMap.put("msg","El usuario a actualizar no existe");
                return ResponseEntity.badRequest().body(responseMap);
            }
        }else{
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar un ID");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PutMapping(value = "/users/apagado")
    public ResponseEntity<HashMap<String,Object>> apagarAutenticacion() {
        HashMap<String,Object> responseMap = new HashMap<>();
        usersRepository.apagarUsuarioPorId();
        responseMap.put("estado","apagado exitoso");
        return ResponseEntity.ok(responseMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionExcepcion(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST") || request.getMethod().equals("PUT")){
            responseMap.put("estado","error");
            responseMap.put("msg",request.getMethod().equals("POST")?"Debe enviar un usuario. El usuario debe tener un nombre y un tipo (contador, cliente, analista de promociones o analista logistico)":"Debe enviar un usuario. Indique el ID del usuario y su nuevo tipo (contador, cliente, analista de promociones o analista logistico)");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }
}
