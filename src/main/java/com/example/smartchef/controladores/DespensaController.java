package com.example.smartchef.controladores;

import com.example.smartchef.dtos.request.DespensaRequestDto;
import com.example.smartchef.dtos.response.DespensaResponseDto;
import com.example.smartchef.servicios.DespensaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despensa")
@RequiredArgsConstructor
public class DespensaController {

    private final DespensaService despensaService;

    // 8. GET /api/v1/despensa?usuarioId=1
    // (usuarioId es provisional: en la Fase 4 saldrá del login)
    @GetMapping
    public List<DespensaResponseDto> listar(@RequestParam Integer usuarioId) {
        return despensaService.listar(usuarioId);
    }

    // 9. PUT /api/v1/despensa/5?usuarioId=1
    @PutMapping("/{ingredienteId}")
    public ResponseEntity<DespensaResponseDto> guardar(@PathVariable Integer ingredienteId,
                                                       @RequestParam Integer usuarioId,
                                                       @Valid @RequestBody DespensaRequestDto peticion) {
        DespensaResponseDto resultado = despensaService.guardar(usuarioId, ingredienteId, peticion);

        if (resultado == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }
}
