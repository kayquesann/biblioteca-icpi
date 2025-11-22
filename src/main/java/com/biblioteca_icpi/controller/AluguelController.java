package com.biblioteca_icpi.controller;

import com.biblioteca_icpi.dto.AlugarDTO;
import com.biblioteca_icpi.model.Aluguel;
import com.biblioteca_icpi.service.AluguelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public Aluguel alugarLivro (@RequestBody AlugarDTO request) {
        return aluguelService.alugarLivro(request.getIdLivro(), request.getIdUsuario());
    }

    @PutMapping("/{idAluguel}")
    public void devolverLivro (@PathVariable Long idAluguel) {
        aluguelService.devolverLivro(idAluguel);
    }

    @GetMapping("/{id}")
    public Aluguel consultarAluguel (@PathVariable Long idAluguel) {
        return aluguelService.consultarAluguelEspecifico(idAluguel);
    }

    @GetMapping
    public List<Aluguel> consultarAlugueis () {
        return aluguelService.consultarAlugueis();
    }
}
