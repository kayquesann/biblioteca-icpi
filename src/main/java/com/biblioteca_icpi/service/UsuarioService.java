package com.biblioteca_icpi.service;

import com.biblioteca_icpi.exception.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    Usuario buscarUsuarioNoBanco (Usuario usuario) {
        return usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
    }

    public Usuario criarUsuario(Usuario usuario) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(usuario.getEmail());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            return usuarioRepository.save(usuario);
        }
    }

    public Usuario editarUsuario(Usuario usuario) {
        Usuario usuarioEncontrado = buscarUsuarioNoBanco(usuario);
        usuarioEncontrado.setNome(usuario.getNome());
        usuarioEncontrado.setSenha(usuario.getSenha());
        return usuarioRepository.save(usuarioEncontrado);
    }

    public void excluirUsuario(Long id) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado!"));
            usuarioRepository.delete(usuarioEncontrado);
    }

    public Usuario lerUsuario(Long id) {
       return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado!"));

    }

}
