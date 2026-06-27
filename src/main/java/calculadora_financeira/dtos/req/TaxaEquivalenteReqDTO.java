package calculadora_financeira.dtos.req;

import calculadora_financeira.enums.UnidadeTempoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaEquivalenteReqDTO(
    @NotNull(message = "i não pode ser null.")
    @PositiveOrZero(message = "i deve ser maior ou igual a zero.")
    BigDecimal i,

    @NotNull(message = "Unidade da taxa de origem não pode ser null.")
    UnidadeTempoEnum unidadeTaxaOrigemEnum,

    @NotNull(message = "Unidade da taxa de destino não pode ser null.")
    UnidadeTempoEnum unidadeTaxaDestinoEnum
) {
}
