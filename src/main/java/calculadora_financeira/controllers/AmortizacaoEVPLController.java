package calculadora_financeira.controllers;

import calculadora_financeira.dtos.req.AmortizacaoReqDTO;
import calculadora_financeira.dtos.req.VPLReqDTO;
import calculadora_financeira.dtos.res.AmortizacaoResDTO;
import calculadora_financeira.dtos.res.VPLResDTO;
import calculadora_financeira.services.AmortizacaoEVPLService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amortizacao-vpl")
public class AmortizacaoEVPLController {
    private final AmortizacaoEVPLService service;

    public AmortizacaoEVPLController(AmortizacaoEVPLService service) {
        this.service = service;
    }

    @GetMapping("/amortizacao-sac")
    public ResponseEntity<AmortizacaoResDTO> obterAmortizacaoSAC(
        @Valid @ModelAttribute AmortizacaoReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterAmortizacaoSAC(dto));
    }

    @GetMapping("/vpl")
    public ResponseEntity<VPLResDTO> obterVPL(
        @Valid @ModelAttribute VPLReqDTO dto
    ) {
        return ResponseEntity.ok(service.obterVPL(dto));
    }
}