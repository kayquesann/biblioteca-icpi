package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarUsuarioNoBanco (Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
    }

    public Usuario criarUsuario(CadastrarUsuarioDTO dto) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(dto.getSenha());
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return usuarioSalvo;
        }
    }

    public Usuario editarUsuario(Long id, EditarUsuarioDTO dto) {
        Usuario usuarioEncontrado = buscarUsuarioNoBanco(id);
        usuarioEncontrado.setNome(dto.getNome());
        usuarioEncontrado.setSenha(dto.getSenha());
        return usuarioRepository.save(usuarioEncontrado);
    }

    public void excluirUsuario(Long id) {
        Usuario usuarioEncontrado = buscarUsuarioNoBanco(id);
        usuarioRepository.delete(usuarioEncontrado);
    }

    public List<Usuario> listarUsuarios () {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

}
