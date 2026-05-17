package calculadora_financeira.services;

import calculadora_financeira.dtos.req.*;
import calculadora_financeira.enums.UnidadeTempoEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CapitalizacaoSimplesService {
    public BigDecimal obterValorPresente(ValorPresenteReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.i(), dto.unidadeTaxaJurosEnum());
        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        BigDecimal divisor = BigDecimal.ONE.add(iConvertido.multiply(nConvertido));

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return dto.VF().divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterValorFuturo(ValorFuturoReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.i(), dto.unidadeTaxaJurosEnum());
        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        return dto.VP().multiply(BigDecimal.ONE.add(iConvertido.multiply(nConvertido))).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterJuros(JurosReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.i(), dto.unidadeTaxaJurosEnum());
        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        return dto.VP().multiply(iConvertido).multiply(nConvertido).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxa(TaxaReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        BigDecimal divisor = nConvertido.setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return (dto.VF().divide(dto.VP(), 6, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTempo(TempoReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.i(), dto.unidadeTaxaJurosEnum());

        BigDecimal divisor = iConvertido.setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return (dto.VF().divide(dto.VP(), 6, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxaDoDescontoComercial(TaxaDoDescontoComercialReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.I(), dto.unidadeTaxaJurosEnum());
        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        BigDecimal divisor = BigDecimal.ONE.add(iConvertido.multiply(nConvertido)).setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return iConvertido.divide(divisor, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal obterTaxaEfetiva(TaxaEfetivaReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser null");
        }

        BigDecimal iConvertido = converterTaxaParaDia(dto.Ic(), dto.unidadeTaxaJurosEnum());
        BigDecimal nConvertido = converterTempoParaDia(dto.n(), dto.unidadeTempoEnum());

        BigDecimal divisor = BigDecimal.ONE.subtract(iConvertido.multiply(nConvertido)).setScale(6, RoundingMode.HALF_UP);

        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("O divisor não pode ser zero.");
        }

        return iConvertido.divide(divisor, 6, RoundingMode.HALF_UP);
    }

    private BigDecimal converterTempoParaDia(BigDecimal variavel, UnidadeTempoEnum unidadeTempo) {
        return variavel.multiply(obterDenominador(unidadeTempo)).setScale(6, RoundingMode.HALF_UP);
    }

    private BigDecimal converterTaxaParaDia(BigDecimal variavel, UnidadeTempoEnum unidadeTempo) {
        return variavel.divide(obterDenominador(unidadeTempo), 16, RoundingMode.HALF_UP);
    }

    private BigDecimal obterDenominador(UnidadeTempoEnum unidadeTempo) {
        return switch (unidadeTempo) {
            case UnidadeTempoEnum.Ano -> BigDecimal.valueOf(360);
            case UnidadeTempoEnum.Semestre -> BigDecimal.valueOf(180);
            case UnidadeTempoEnum.Trimestre -> BigDecimal.valueOf(90);
            case UnidadeTempoEnum.Mes -> BigDecimal.valueOf(30);
            default -> BigDecimal.ONE;
        };
    }
}
