package calculadora_financeira.services;

import calculadora_financeira.dtos.req.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CapitalizacaoSimplesService {
    public BigDecimal obterValorPresente(ValorPresenteReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal divisor = BigDecimal.ONE.add(dto.i().multiply(dto.n()));

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return dto.VF().divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterValorFuturo(ValorFuturoReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        return dto.VP().multiply(BigDecimal.ONE.add(dto.i().multiply(dto.n()))).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterJuros(JurosReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        return dto.VP().multiply(dto.i()).multiply(dto.n()).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxa(TaxaReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal divisor = dto.n().setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return (dto.VF().divide(dto.VP(), 6, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTempo(TempoReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal divisor = dto.i().setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return (dto.VF().divide(dto.VP(), 6, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxaDoDescontoComercial(TaxaDoDescontoComercialReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal divisor = BigDecimal.ONE.add(dto.I().multiply(dto.n())).setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return dto.I().divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxaEfetiva(TaxaEfetivaReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal divisor = BigDecimal.ONE.subtract(dto.Ic().multiply(dto.n())).setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return dto.Ic().divide(divisor, 6, RoundingMode.HALF_UP);
    }
}
