package com.ortiz.springcloud.msvc.cursos.repositories;

import com.ortiz.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CursoRepository extends CrudRepository<Curso,Long> {

}
