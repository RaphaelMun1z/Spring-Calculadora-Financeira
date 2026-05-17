package calculadora_financeira.dtos.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ValorPresenteReqDTO(
        @NotNull(message = "VF não pode ser null.")
        @Positive(message = "VF deve ser maior que zero.")
        BigDecimal VF,

        @NotNull(message = "i não pode ser null.")
        @PositiveOrZero(message = "i deve ser maior ou igual a zero.")
        BigDecimal i,

        @NotNull(message = "n não pode ser null.")
        @PositiveOrZero(message = "n deve ser maior ou igual a zero.")
        BigDecimal n
) {}
