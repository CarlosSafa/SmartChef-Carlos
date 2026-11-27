package com.example.smartchef.controladores;

import com.example.smartchef.dtos.request.UsuarioRecetaRequestDto;
import com.example.smartchef.dtos.response.UsuarioRecetaResponseDto;
import com.example.smartchef.servicios.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    // 14. POST /api/v1/registros?usuarioId=1
    // (usuarioId es provisional: en la Fase 4 saldrá del login)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioRecetaResponseDto registrar(@RequestParam Integer usuarioId,
                                              @Valid @RequestBody UsuarioRecetaRequestDto peticion) {
        return registroService.registrar(usuarioId, peticion);
    }
}
