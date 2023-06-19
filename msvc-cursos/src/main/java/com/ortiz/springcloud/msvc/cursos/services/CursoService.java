package com.ortiz.springcloud.msvc.cursos.services;

import com.ortiz.springcloud.msvc.cursos.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar (Curso curso);
    void eliminar(Long id);
}