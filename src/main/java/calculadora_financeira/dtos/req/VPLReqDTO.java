package calculadora_financeira.dtos.req;

import calculadora_financeira.enums.UnidadeTempoEnum;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record VPLReqDTO(
    @NotNull(message = "Investimento não pode ser null.")
    @Positive(message = "Investimento deve ser maior que zero.")
    BigDecimal investimento,

    @NotNull(message = "i não pode ser null.")
    @PositiveOrZero(message = "i deve ser maior ou igual a zero.")
    BigDecimal i,

    @NotNull(message = "Unidade da taxa de juros não pode ser null.")
    UnidadeTempoEnum unidadeTaxaJurosEnum,

    @NotNull(message = "n não pode ser null.")
    @Positive(message = "n deve ser maior que zero.")
    @Digits(integer = 10, fraction = 0, message = "n deve ser um número inteiro.")
    BigDecimal n,

    @NotNull(message = "Unidade de tempo não pode ser null.")
    UnidadeTempoEnum unidadeTempoEnum,

    @NotNull(message = "Fluxos de caixa não podem ser null.")
    @Size(min = 1, message = "Deve ser informado pelo menos um fluxo de caixa.")
    List<
            @NotNull(message = "Fluxo de caixa não pode ser null.")
                BigDecimal
            > fluxosCaixa,

    @NotNull(message = "Valor residual não pode ser null.")
    @PositiveOrZero(message = "Valor residual deve ser maior ou igual a zero.")
    BigDecimal valorResidual,

    @NotNull(message = "Período do valor residual não pode ser null.")
    @PositiveOrZero(message = "Período do valor residual deve ser maior ou igual a zero.")
    @Digits(
        integer = 10,
        fraction = 0,
        message = "Período do valor residual deve ser um número inteiro."
    )
    BigDecimal periodoValorResidual
) {
}
