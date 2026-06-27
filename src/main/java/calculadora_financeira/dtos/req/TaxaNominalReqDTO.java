package calculadora_financeira.dtos.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaNominalReqDTO(
    @NotNull(message = "Taxa proporcional não pode ser null.")
    @PositiveOrZero(message = "Taxa proporcional deve ser maior ou igual a zero.")
    BigDecimal iProporcional,

    @NotNull(message = "k não pode ser null.")
    @Positive(message = "k deve ser maior que zero.")
    BigDecimal k
) {
}
