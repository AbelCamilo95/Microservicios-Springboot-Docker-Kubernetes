package com.ortiz.springcloud.msvc.cursos.services;

import com.ortiz.springcloud.msvc.cursos.models.Usuario;
import com.ortiz.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar (Curso curso);
    void eliminar(Long id);

    Optional<Usuario> asignarusuario(Usuario usuario,Long cursoId);
    Optional <Usuario> crearUsuario(Usuario usuario,Long cursoId);
    Optional <Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
