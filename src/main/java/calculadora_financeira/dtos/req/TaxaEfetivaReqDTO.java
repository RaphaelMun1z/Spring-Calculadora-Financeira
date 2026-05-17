package calculadora_financeira.dtos.req;

import calculadora_financeira.enums.UnidadeTempoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaEfetivaReqDTO(
    @NotNull(message = "Ic não pode ser null.")
    @PositiveOrZero(message = "Ic deve ser maior ou igual a zero.")
    BigDecimal Ic,

    @NotNull(message = "Unidade da taxa de juros não pode ser null.")
    UnidadeTempoEnum unidadeTaxaJurosEnum,

    @NotNull(message = "n não pode ser null.")
    @Positive(message = "n deve ser maior que zero.")
    BigDecimal n,

    @NotNull(message = "Unidade de tempo não pode ser null.")
    UnidadeTempoEnum unidadeTempoEnum
) {
}
