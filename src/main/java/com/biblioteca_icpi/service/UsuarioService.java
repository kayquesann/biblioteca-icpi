package com.biblioteca_icpi.service;

import com.biblioteca_icpi.dto.CadastrarUsuarioDTO;
import com.biblioteca_icpi.dto.EditarUsuarioDTO;
import com.biblioteca_icpi.dto.ResponseUsuarioDTO;
import com.biblioteca_icpi.exception.usuario.UsuarioJaExistenteException;
import com.biblioteca_icpi.exception.usuario.UsuarioNaoEncontradoException;
import com.biblioteca_icpi.model.UserRole;
import com.biblioteca_icpi.model.Usuario;
import com.biblioteca_icpi.repository.AluguelRepository;
import com.biblioteca_icpi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AluguelRepository aluguelRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    public UsuarioService(UsuarioRepository usuarioRepository, AluguelRepository aluguelRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.aluguelRepository = aluguelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseUsuarioDTO buscarUsuarioNoBanco (Long id) throws AccessDeniedException {
        Usuario usuarioLogado = obterUsuarioLogado();
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getId().equals(id)) {
            Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
            return convertToResponseUsuarioDTO(usuarioEncontrado);
        }
        throw new AccessDeniedException("Você não tem permissão para ver esse usuário");


    }

    private Usuario obterUsuarioLogado () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }


    @Transactional
    public ResponseUsuarioDTO criarUsuario(CadastrarUsuarioDTO dto) {
        Optional<Usuario> possivelUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if (possivelUsuario.isPresent()) {
            throw new UsuarioJaExistenteException("Usuário já cadastrado!");
        } else {
            String encryptedPassword = passwordEncoder.encode(dto.getSenha());
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(encryptedPassword);
            usuario.setRole(UserRole.USER);
           usuarioRepository.save(usuario);
            return convertToResponseUsuarioDTO(usuario);
        }
    }

    @Transactional
    public ResponseUsuarioDTO promoverParaAdmin (String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        usuario.setRole(UserRole.ADMIN);
        usuarioRepository.save(usuario);
        return convertToResponseUsuarioDTO(usuario);
    }


    @Transactional
    public ResponseUsuarioDTO editarUsuario(Long id, EditarUsuarioDTO dto) throws AccessDeniedException {
        Usuario usuarioLogado = obterUsuarioLogado();
        if (usuarioLogado.getRole() == UserRole.ADMIN || usuarioLogado.getId().equals(id))  {
            Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));
            usuarioEncontrado.setNome(dto.getNome());
            usuarioEncontrado.setSenha(passwordEncoder.encode(dto.getSenha()));
            usuarioRepository.save(usuarioEncontrado);
            return convertToResponseUsuarioDTO(usuarioEncontrado);
        }
        throw new AccessDeniedException("Você não tem permissão para editar esse usuário");

    }

    public void excluirUsuario(Long id) throws AccessDeniedException {
        Usuario usuarioLogado = obterUsuarioLogado();

        if (usuarioLogado.getRole() != UserRole.ADMIN && !usuarioLogado.getId().equals(id)) {
            throw new AccessDeniedException("Você não tem permissão para excluir esse usuário");
        }

        Usuario usuarioEncontrado = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));

        if (aluguelRepository.existsByUsuario(usuarioEncontrado)) {
            throw new IllegalStateException("Usuário possui histórico de aluguel e não pode ser excluído");
        }

        usuarioRepository.delete(usuarioEncontrado);
    }


    public List<ResponseUsuarioDTO> listarUsuarios () {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::convertToResponseUsuarioDTO).toList();
    }

    public ResponseUsuarioDTO convertToResponseUsuarioDTO (Usuario usuario) {
        ResponseUsuarioDTO usuarioDTO = new ResponseUsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRole(usuario.getRole());
        return usuarioDTO;
    }

}
