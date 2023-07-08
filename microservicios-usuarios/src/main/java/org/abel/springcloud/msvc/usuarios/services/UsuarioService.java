package org.abel.springcloud.msvc.usuarios.services;

import org.abel.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
   // optional es unwrapper que envuelve la consulta y evita el error nullpointexception
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar (Long id);

    List<Usuario> listarPorids(Iterable<Long> ids);

    Optional<Usuario> obtenerEmail (String email);
}
