package com.biblioteca_icpi.service;

import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario (Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }
        return usuarioRepository.save(usuario);
    }
}
