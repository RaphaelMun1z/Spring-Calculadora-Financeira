package calculadora_financeira.dtos.req;

import calculadora_financeira.enums.UnidadeTempoEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TempoReqDTO(
        @NotNull(message = "VP não pode ser null.")
        @Positive(message = "VP deve ser maior que zero.")
        BigDecimal VP,

        @NotNull(message = "VF não pode ser null.")
        @Positive(message = "VF deve ser maior que zero.")
        BigDecimal VF,

        @NotNull(message = "i não pode ser null.")
        @Positive(message = "i deve ser maior que zero.")
        BigDecimal i,

        @NotNull(message = "Unidade da taxa de juros não pode ser null.")
        UnidadeTempoEnum unidadeTaxaJurosEnum
) {
}
