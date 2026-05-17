package calculadora_financeira.controllers;

import calculadora_financeira.dtos.req.*;
import calculadora_financeira.services.CapitalizacaoSimplesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/capitalizacao-simples")
public class CapitalizacaoSimplesController {
    private final CapitalizacaoSimplesService service;

    public CapitalizacaoSimplesController(CapitalizacaoSimplesService service) {
        this.service = service;
    }

    @GetMapping(value = "/valor-presente")
    public ResponseEntity<BigDecimal> obterValorPresente(@Valid @ModelAttribute ValorPresenteReqDTO dto) {
        return ResponseEntity.ok(service.obterValorPresente(dto));
    }

    @GetMapping(value = "/valor-futuro")
    public ResponseEntity<BigDecimal> obterValorFuturo(@Valid @ModelAttribute ValorFuturoReqDTO dto) {
        return ResponseEntity.ok(service.obterValorFuturo(dto));
    }

    @GetMapping(value = "/juros")
    public ResponseEntity<BigDecimal> obterJuros(@Valid @ModelAttribute JurosReqDTO dto) {
        return ResponseEntity.ok(service.obterJuros(dto));
    }

    @GetMapping(value = "/taxa")
    public ResponseEntity<BigDecimal> obterTaxa(@Valid @ModelAttribute TaxaReqDTO dto) {
        return ResponseEntity.ok(service.obterTaxa(dto));
    }

    @GetMapping(value = "/tempo")
    public ResponseEntity<BigDecimal> obterTempo(@Valid @ModelAttribute TempoReqDTO dto) {
        return ResponseEntity.ok(service.obterTempo(dto));
    }

    @GetMapping(value = "/taxa-desconto-comercial")
    public ResponseEntity<BigDecimal> obterTaxaDoDescontoComercial(@Valid @ModelAttribute TaxaDoDescontoComercialReqDTO dto) {
        return ResponseEntity.ok(service.obterTaxaDoDescontoComercial(dto));
    }

    @GetMapping(value = "/taxa-efetiva")
    public ResponseEntity<BigDecimal> obterTaxaEfetiva(@Valid @ModelAttribute TaxaEfetivaReqDTO dto) {
        return ResponseEntity.ok(service.obterTaxaEfetiva(dto));
    }
}
