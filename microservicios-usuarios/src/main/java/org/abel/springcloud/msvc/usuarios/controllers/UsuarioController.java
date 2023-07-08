package org.abel.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.abel.springcloud.msvc.usuarios.models.entity.Usuario;
import org.abel.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(name = "/")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    //Listar
    @GetMapping("/")
    public List<Usuario> listar(){
        return service.listar();
    }

    // busqueda de usuario por id, se busca por id
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable(name ="id") Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok().body(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // crear nuevo usuario se manda por medio de un Json por eso se usa en requestbody con un objeto usuario
    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){

        if(result.hasErrors()){
            return Validar(result);
        }
        if(!usuario.getEmail().isEmpty() && service.obtenerEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje","ya existe un usuario con ese email"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    // editar susarios se usa requestbody debido a que se modificaran datos segun la variable que se asigna con el
    // pathvariable
    @PutMapping("/{id}")
    public  ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result ,@PathVariable Long id){

        if(result.hasErrors()){
            return Validar(result);
        }

        Optional<Usuario> o =service.porId(id);
        if (o.isPresent()){
            Usuario usuariodb = o.get();
            if(!usuario.getEmail().isEmpty() &&
                    !usuario.getEmail().equalsIgnoreCase(usuariodb.getEmail()) &&
                    service.obtenerEmail(usuario.getEmail()).isPresent()){

                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje","ya existe un usuario con ese email"));
            }

            usuariodb.setNombre(usuario.getNombre());
            usuariodb.setEmail(usuario.getEmail());
            usuariodb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuariodb));
        }
        return ResponseEntity.notFound().build();
    }
    // se elimina en base al id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id){
        Optional<Usuario> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(id);
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorids(ids));
    }

    private static ResponseEntity<Map<String, String>> Validar(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(),"El campo" + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
