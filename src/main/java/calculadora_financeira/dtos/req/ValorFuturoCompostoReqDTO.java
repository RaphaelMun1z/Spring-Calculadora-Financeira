package calculadora_financeira.dtos.req;

import calculadora_financeira.enums.UnidadeTempoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ValorFuturoCompostoReqDTO(
    @NotNull(message = "VP não pode ser null.")
    @Positive(message = "VP deve ser maior que zero.")
    BigDecimal VP,

    @NotNull(message = "i não pode ser null.")
    @PositiveOrZero(message = "i deve ser maior ou igual a zero.")
    BigDecimal i,

    @NotNull(message = "Unidade da taxa de juros não pode ser null.")
    UnidadeTempoEnum unidadeTaxaJurosEnum,

    @NotNull(message = "n não pode ser null.")
    @PositiveOrZero(message = "n deve ser maior ou igual a zero.")
    BigDecimal n,

    @NotNull(message = "Unidade de tempo não pode ser null.")
    UnidadeTempoEnum unidadeTempoEnum
) {
}
