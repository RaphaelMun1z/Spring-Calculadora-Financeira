package calculadora_financeira.controllers;

import calculadora_financeira.dtos.req.JurosCompostosReqDTO;
import calculadora_financeira.dtos.req.TaxaCompostaReqDTO;
import calculadora_financeira.dtos.req.TaxaEquivalenteReqDTO;
import calculadora_financeira.dtos.req.TaxaNominalReqDTO;
import calculadora_financeira.dtos.req.TaxaProporcionalReqDTO;
import calculadora_financeira.dtos.req.TempoCompostoReqDTO;
import calculadora_financeira.dtos.req.ValorFuturoCompostoReqDTO;
import calculadora_financeira.dtos.req.ValorPresenteCompostoReqDTO;
import calculadora_financeira.services.CapitalizacaoCompostaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/capitalizacao-composta")
public class CapitalizacaoCompostaController {
    private final CapitalizacaoCompostaService service;

    public CapitalizacaoCompostaController(CapitalizacaoCompostaService service) {
        this.service = service;
    }

    @GetMapping("/valor-presente")
    public ResponseEntity<BigDecimal> obterValorPresente(
        @Valid @ModelAttribute ValorPresenteCompostoReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterValorPresente(dto));
    }

    @GetMapping("/valor-futuro")
    public ResponseEntity<BigDecimal> obterValorFuturo(
        @Valid @ModelAttribute ValorFuturoCompostoReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterValorFuturo(dto));
    }

    @GetMapping("/juros")
    public ResponseEntity<BigDecimal> obterJuros(
        @Valid @ModelAttribute JurosCompostosReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterJuros(dto));
    }

    @GetMapping("/taxa")
    public ResponseEntity<BigDecimal> obterTaxa(
        @Valid @ModelAttribute TaxaCompostaReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterTaxa(dto));
    }

    @GetMapping("/tempo")
    public ResponseEntity<BigDecimal> obterTempo(
        @Valid @ModelAttribute TempoCompostoReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterTempo(dto));
    }

    @GetMapping("/taxa-proporcional")
    public ResponseEntity<BigDecimal> obterTaxaProporcional(
        @Valid @ModelAttribute TaxaProporcionalReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterTaxaProporcional(dto));
    }

    @GetMapping("/taxa-nominal")
    public ResponseEntity<BigDecimal> obterTaxaNominal(
        @Valid @ModelAttribute TaxaNominalReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterTaxaNominal(dto));
    }

    @GetMapping("/taxa-equivalente")
    public ResponseEntity<BigDecimal> obterTaxaEquivalente(
        @Valid @ModelAttribute TaxaEquivalenteReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterTaxaEquivalente(dto));
    }
}
