package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.dto.AluguelDTO;
import com.biblioteca_icpi.service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<AluguelDTO> alugarLivro (@Valid @RequestBody AlugarDTO dto) {
        return ResponseEntity.ok(aluguelService.alugarLivro(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AluguelDTO> devolverLivro (@PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.devolverLivro(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelDTO> consultarAluguel (@PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.consultarAluguelEspecifico(id));
    }

    @GetMapping
    public ResponseEntity<List<AluguelDTO>> consultarAlugueis () {
        return ResponseEntity.ok(aluguelService.consultarAlugueis());
    }
}
