package calculadora_financeira.dtos.res;

import java.math.BigDecimal;

public record ParcelaAmortizacaoResDTO(
    BigDecimal periodo,
    BigDecimal saldoDevedorInicial,
    BigDecimal amortizacao,
    BigDecimal juros,
    BigDecimal PMT,
    BigDecimal saldoDevedorFinal
) {
}