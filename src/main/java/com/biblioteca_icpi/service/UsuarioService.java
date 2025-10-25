package com.biblioteca_icpi.service;

import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
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

    Usuario buscarUsuarioNoBanco (Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
    }

    public Usuario criarUsuario(Usuario usuario) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findById(usuario.getId());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            return usuarioRepository.save(usuario);
        }
    }

    public Usuario editarUsuario(Long id, Usuario usuario) {
        Usuario usuarioEncontrado = buscarUsuarioNoBanco(id);
        usuarioEncontrado.setNome(usuario.getNome());
        usuarioEncontrado.setSenha(usuario.getSenha());
        return usuarioRepository.save(usuarioEncontrado);
    }

    public void excluirUsuario(Long id) {
        Usuario usuarioEncontrado = buscarUsuarioNoBanco(id);
        usuarioRepository.delete(usuarioEncontrado);
    }

    public Usuario lerUsuario(Long id) {
       return buscarUsuarioNoBanco(id);

    }

}
