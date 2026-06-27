package calculadora_financeira.dtos.res;

import calculadora_financeira.enums.UnidadeTempoEnum;

import java.math.BigDecimal;
import java.util.List;

public record AmortizacaoResDTO(
    BigDecimal VP,
    BigDecimal i,
    UnidadeTempoEnum unidadeTaxaJurosEnum,
    BigDecimal n,
    UnidadeTempoEnum unidadeTempoEnum,
    BigDecimal amortizacaoConstante,
    List<ParcelaAmortizacaoResDTO> parcelas,
    BigDecimal totalJuros,
    BigDecimal totalPrestacoes
) {
}