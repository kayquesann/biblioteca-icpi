package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelResponseDTO;
import com.biblioteca_icpi.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<AluguelResponseDTO> alugarLivro(@RequestBody AlugarDTO dto) {
        AluguelResponseDTO response = aluguelService.alugarLivro(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AluguelResponseDTO> devolverLivro(@PathVariable Long id) {
        AluguelResponseDTO response = aluguelService.devolverLivro(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelResponseDTO> consultarAluguel (@PathVariable Long id) throws AccessDeniedException {
        return ResponseEntity.ok(aluguelService.consultarAluguelEspecifico(id));
    }


    @GetMapping
    public ResponseEntity<List<AluguelResponseDTO>> consultarAlugueis () {
        return ResponseEntity.ok(aluguelService.consultarAlugueis());
    }
}
