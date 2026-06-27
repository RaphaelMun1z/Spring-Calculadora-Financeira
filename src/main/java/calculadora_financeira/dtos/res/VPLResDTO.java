package calculadora_financeira.dtos.res;

import calculadora_financeira.enums.UnidadeTempoEnum;

import java.math.BigDecimal;

public record VPLResDTO(
    BigDecimal investimento,
    BigDecimal i,
    UnidadeTempoEnum unidadeTaxaJurosEnum,
    BigDecimal n,
    UnidadeTempoEnum unidadeTempoEnum,
    BigDecimal valorPresenteFluxos,
    BigDecimal valorPresenteResidual,
    BigDecimal VPL
) {
}
