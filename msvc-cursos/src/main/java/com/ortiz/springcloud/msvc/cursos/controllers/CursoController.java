package com.ortiz.springcloud.msvc.cursos.controllers;

import com.ortiz.springcloud.msvc.cursos.models.Usuario;
import com.ortiz.springcloud.msvc.cursos.models.entity.Curso;
import com.ortiz.springcloud.msvc.cursos.services.CursoService;
import feign.FeignException;
import feign.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(name = "/")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity <List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable  Long id){
        Optional<Curso> o = service.porIdConUsuarios(id);//service.porId(id);
        if(o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){

        if(result.hasErrors()){
            return Validar(result);
        }

        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        Optional<Curso> o = service.porId(id);

        if(result.hasErrors()){
            return Validar(result);
        }

        if(o.isPresent()){
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoid}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario,@PathVariable Long cursoid) {
        Optional<Usuario> o ;
        try{
            o = service.asignarUsuario(usuario,cursoid);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap
                            ("Mensaje","No existe el Usuario por el id o error en la comunicacion" + e.getMessage()));

        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoid}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario,@PathVariable Long cursoid) {
        Optional<Usuario> o ;
        try{
            o = service.crearUsuario(usuario,cursoid);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap
                            ("Mensaje","No se pudo crear el Usuario por el id o error en la comunicacion" + e.getMessage()));

        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoid}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario,@PathVariable Long cursoid) {
        Optional<Usuario> o ;
        try{
            o = service.eliminarUsuario(usuario,cursoid);
        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap
                            ("Mensaje","No existe el Usuario por el id o error en la comunicacion" + e.getMessage()));

        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> Validar(BindingResult result) {
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(),"El campo" + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
