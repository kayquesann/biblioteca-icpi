package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.Role;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.RoleRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public ResponseUsuarioDTO buscarUsuarioNoBanco (Long id) {

        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
        return convertToResponseUsuarioDTO(usuarioEncontrado);
    }

    private Usuario criarUsuarioBase(CadastrarUsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        return usuario;
    }

    @Transactional
    public ResponseUsuarioDTO criarUsuario(CadastrarUsuarioDTO dto) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            Usuario usuario = criarUsuarioBase(dto);
            Role userRole = roleRepository.findByNome("ROLE_USER");
            usuario.getRoles().add(userRole);
           usuarioRepository.save(usuario);
            return convertToResponseUsuarioDTO(usuario);
        }
    }

    @Transactional
    public ResponseUsuarioDTO criarUsuarioAdmin (CadastrarUsuarioDTO dto) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            Usuario usuario = criarUsuarioBase(dto);
            Role roleAdmin = roleRepository.findByNome("ROLE_ADMIN");
            usuario.getRoles().add(roleAdmin);
            usuarioRepository.save(usuario);
            return convertToResponseUsuarioDTO(usuario);
        }
    }

    @Transactional
    public ResponseUsuarioDTO editarUsuario(Long id, EditarUsuarioDTO dto) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
        usuarioEncontrado.setNome(dto.getNome());
        usuarioEncontrado.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioRepository.save(usuarioEncontrado);
        return convertToResponseUsuarioDTO(usuarioEncontrado);
    }

    public void excluirUsuario(Long id) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
        usuarioRepository.delete(usuarioEncontrado);
    }

    public List<ResponseUsuarioDTO> listarUsuarios () {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::convertToResponseUsuarioDTO).toList();
    }

    public ResponseUsuarioDTO convertToResponseUsuarioDTO (Usuario usuario) {
        ResponseUsuarioDTO usuarioDTO = new ResponseUsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRoles(
                usuario.getRoles()
                        .stream()
                        .map(Role::getAuthority)
                        .toList());
        return usuarioDTO;
    }

}
