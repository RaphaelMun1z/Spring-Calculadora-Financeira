package calculadora_financeira.dtos.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaEfetivaReqDTO(
        @NotNull(message = "Ic não pode ser null.")
        @PositiveOrZero(message = "Ic deve ser maior ou igual a zero.")
        BigDecimal Ic,

        @NotNull(message = "n não pode ser null.")
        @Positive(message = "n deve ser maior que zero.")
        BigDecimal n
) {
}
