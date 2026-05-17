package calculadora_financeira.dtos.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ValorFuturoReqDTO(
        @NotNull(message = "VP não pode ser null.")
        @Positive(message = "VP deve ser maior que zero.")
        BigDecimal VP,

        @NotNull(message = "i não pode ser null.")
        @PositiveOrZero(message = "i deve ser maior ou igual a zero.")
        BigDecimal i,

        @NotNull(message = "n não pode ser null.")
        @PositiveOrZero(message = "n deve ser maior ou igual a zero.")
        BigDecimal n
){}
