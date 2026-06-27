package calculadora_financeira.services;

import calculadora_financeira.dtos.req.JurosCompostosReqDTO;
import calculadora_financeira.dtos.req.TaxaCompostaReqDTO;
import calculadora_financeira.dtos.req.TaxaEquivalenteReqDTO;
import calculadora_financeira.dtos.req.TaxaNominalReqDTO;
import calculadora_financeira.dtos.req.TaxaProporcionalReqDTO;
import calculadora_financeira.dtos.req.TempoCompostoReqDTO;
import calculadora_financeira.dtos.req.ValorFuturoCompostoReqDTO;
import calculadora_financeira.dtos.req.ValorPresenteCompostoReqDTO;
import calculadora_financeira.enums.UnidadeTempoEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class CapitalizacaoCompostaService {
    private static final int ESCALA = 6;
    private static final RoundingMode ARREDONDAMENTO = RoundingMode.HALF_UP;
    private static final MathContext CONTEXTO_CALCULO = new MathContext(24, ARREDONDAMENTO);

    public BigDecimal obterValorPresente(ValorPresenteCompostoReqDTO dto) {
        validarValorPositivo(dto.VF(), "VF");
        validarTaxaNaoNegativa(dto.i());
        validarTempoNaoNegativo(dto.n());
        validarUnidades(dto.unidadeTaxaJurosEnum(), dto.unidadeTempoEnum());

        BigDecimal periodosDaTaxa = converterTempoParaPeriodosDaTaxa(
            dto.n(),
            dto.unidadeTempoEnum(),
            dto.unidadeTaxaJurosEnum()
        );
        BigDecimal fatorCapitalizacao = potencia(BigDecimal.ONE.add(dto.i()), periodosDaTaxa);

        return arredondar(dto.VF().divide(fatorCapitalizacao, CONTEXTO_CALCULO));
    }

    public BigDecimal obterValorFuturo(ValorFuturoCompostoReqDTO dto) {
        validarValorPositivo(dto.VP(), "VP");
        validarTaxaNaoNegativa(dto.i());
        validarTempoNaoNegativo(dto.n());
        validarUnidades(dto.unidadeTaxaJurosEnum(), dto.unidadeTempoEnum());

        BigDecimal periodosDaTaxa = converterTempoParaPeriodosDaTaxa(
            dto.n(),
            dto.unidadeTempoEnum(),
            dto.unidadeTaxaJurosEnum()
        );
        BigDecimal fatorCapitalizacao = potencia(BigDecimal.ONE.add(dto.i()), periodosDaTaxa);

        return arredondar(dto.VP().multiply(fatorCapitalizacao, CONTEXTO_CALCULO));
    }

    public BigDecimal obterJuros(JurosCompostosReqDTO dto) {
        validarValorPositivo(dto.VP(), "VP");
        validarTaxaNaoNegativa(dto.i());
        validarTempoNaoNegativo(dto.n());
        validarUnidades(dto.unidadeTaxaJurosEnum(), dto.unidadeTempoEnum());

        BigDecimal periodosDaTaxa = converterTempoParaPeriodosDaTaxa(
            dto.n(),
            dto.unidadeTempoEnum(),
            dto.unidadeTaxaJurosEnum()
        );
        BigDecimal fatorCapitalizacao = potencia(BigDecimal.ONE.add(dto.i()), periodosDaTaxa);
        BigDecimal valorFuturo = dto.VP().multiply(fatorCapitalizacao, CONTEXTO_CALCULO);

        return arredondar(valorFuturo.subtract(dto.VP()));
    }

    public BigDecimal obterTaxa(TaxaCompostaReqDTO dto) {
        validarValorPositivo(dto.VP(), "VP");
        validarValorPositivo(dto.VF(), "VF");
        validarTempoPositivo(dto.n());
        validarUnidades(dto.unidadeTaxaJurosEnum(), dto.unidadeTempoEnum());

        BigDecimal periodosDaTaxa = converterTempoParaPeriodosDaTaxa(
            dto.n(),
            dto.unidadeTempoEnum(),
            dto.unidadeTaxaJurosEnum()
        );
        BigDecimal razao = dto.VF().divide(dto.VP(), CONTEXTO_CALCULO);
        BigDecimal expoente = BigDecimal.ONE.divide(periodosDaTaxa, CONTEXTO_CALCULO);

        return arredondar(potencia(razao, expoente).subtract(BigDecimal.ONE));
    }

    public BigDecimal obterTempo(TempoCompostoReqDTO dto) {
        validarValorPositivo(dto.VP(), "VP");
        validarValorPositivo(dto.VF(), "VF");
        validarTaxaPositiva(dto.i());
        validarUnidades(dto.unidadeTaxaJurosEnum(), dto.unidadeTempoEnum());

        double numerador = Math.log(dto.VF().divide(dto.VP(), CONTEXTO_CALCULO).doubleValue());
        double denominador = Math.log(BigDecimal.ONE.add(dto.i()).doubleValue());
        double periodosDaTaxa = numerador / denominador;

        if (!Double.isFinite(periodosDaTaxa)) {
            throw new IllegalArgumentException("Não foi possível calcular o tempo com os valores informados.");
        }

        BigDecimal tempoNaUnidadeSolicitada = converterPeriodosDaTaxaParaTempo(
            BigDecimal.valueOf(periodosDaTaxa),
            dto.unidadeTaxaJurosEnum(),
            dto.unidadeTempoEnum()
        );

        return arredondar(tempoNaUnidadeSolicitada);
    }

    public BigDecimal obterTaxaProporcional(TaxaProporcionalReqDTO dto) {
        validarTaxaNaoNegativa(dto.iNominal());
        validarK(dto.k());
        return arredondar(dto.iNominal().divide(dto.k(), CONTEXTO_CALCULO));
    }

    public BigDecimal obterTaxaNominal(TaxaNominalReqDTO dto) {
        validarTaxaNaoNegativa(dto.iProporcional());
        validarK(dto.k());
        return arredondar(dto.iProporcional().multiply(dto.k(), CONTEXTO_CALCULO));
    }

    public BigDecimal obterTaxaEquivalente(TaxaEquivalenteReqDTO dto) {
        validarTaxaNaoNegativa(dto.i());

        if (dto.unidadeTaxaOrigemEnum() == null) {
            throw new IllegalArgumentException("A unidade da taxa de origem não pode ser null.");
        }
        if (dto.unidadeTaxaDestinoEnum() == null) {
            throw new IllegalArgumentException("A unidade da taxa de destino não pode ser null.");
        }

        BigDecimal periodosOrigemPorAno = periodosPorAno(dto.unidadeTaxaOrigemEnum());
        BigDecimal periodosDestinoPorAno = periodosPorAno(dto.unidadeTaxaDestinoEnum());
        BigDecimal expoente = periodosOrigemPorAno.divide(periodosDestinoPorAno, CONTEXTO_CALCULO);

        return arredondar(
            potencia(BigDecimal.ONE.add(dto.i()), expoente).subtract(BigDecimal.ONE)
        );
    }

    private BigDecimal converterTempoParaPeriodosDaTaxa(
        BigDecimal tempo,
        UnidadeTempoEnum unidadeTempo,
        UnidadeTempoEnum unidadeTaxa
    ) {
        BigDecimal periodosTaxaPorAno = periodosPorAno(unidadeTaxa);
        BigDecimal unidadesTempoPorAno = periodosPorAno(unidadeTempo);
        return tempo.multiply(periodosTaxaPorAno, CONTEXTO_CALCULO)
            .divide(unidadesTempoPorAno, CONTEXTO_CALCULO);
    }

    private BigDecimal converterPeriodosDaTaxaParaTempo(
        BigDecimal periodosDaTaxa,
        UnidadeTempoEnum unidadeTaxa,
        UnidadeTempoEnum unidadeTempo
    ) {
        BigDecimal unidadesTempoPorAno = periodosPorAno(unidadeTempo);
        BigDecimal periodosTaxaPorAno = periodosPorAno(unidadeTaxa);
        return periodosDaTaxa.multiply(unidadesTempoPorAno, CONTEXTO_CALCULO)
            .divide(periodosTaxaPorAno, CONTEXTO_CALCULO);
    }

    private BigDecimal periodosPorAno(UnidadeTempoEnum unidade) {
        String nome = unidade.name().toLowerCase();

        return switch (nome) {
            case "dia", "dias" -> BigDecimal.valueOf(360);
            case "mes", "mês", "meses" -> BigDecimal.valueOf(12);
            case "trimestre", "trimestres" -> BigDecimal.valueOf(4);
            case "semestre", "semestres" -> BigDecimal.valueOf(2);
            case "ano", "anos" -> BigDecimal.ONE;
            default -> throw new IllegalArgumentException("Unidade de tempo não suportada: " + unidade);
        };
    }

    private BigDecimal potencia(BigDecimal base, BigDecimal expoente) {
        double resultado = Math.pow(base.doubleValue(), expoente.doubleValue());

        if (!Double.isFinite(resultado)) {
            throw new IllegalArgumentException("O resultado do cálculo excedeu o limite numérico suportado.");
        }

        return BigDecimal.valueOf(resultado);
    }

    private void validarValorPositivo(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException(campo + " deve ser maior que zero.");
        }
    }

    private void validarTaxaNaoNegativa(BigDecimal taxa) {
        if (taxa == null || taxa.signum() < 0) {
            throw new IllegalArgumentException("A taxa deve ser maior ou igual a zero.");
        }
    }

    private void validarTaxaPositiva(BigDecimal taxa) {
        if (taxa == null || taxa.signum() <= 0) {
            throw new IllegalArgumentException("A taxa deve ser maior que zero para calcular o tempo.");
        }
    }

    private void validarTempoNaoNegativo(BigDecimal tempo) {
        if (tempo == null || tempo.signum() < 0) {
            throw new IllegalArgumentException("O tempo deve ser maior ou igual a zero.");
        }
    }

    private void validarTempoPositivo(BigDecimal tempo) {
        if (tempo == null || tempo.signum() <= 0) {
            throw new IllegalArgumentException("O tempo deve ser maior que zero.");
        }
    }

    private void validarK(BigDecimal k) {
        if (k == null || k.signum() <= 0) {
            throw new IllegalArgumentException("k deve ser maior que zero.");
        }
    }

    private void validarUnidades(UnidadeTempoEnum unidadeTaxa, UnidadeTempoEnum unidadeTempo) {
        if (unidadeTaxa == null) {
            throw new IllegalArgumentException("A unidade da taxa de juros não pode ser null.");
        }
        if (unidadeTempo == null) {
            throw new IllegalArgumentException("A unidade de tempo não pode ser null.");
        }
    }

    private BigDecimal arredondar(BigDecimal valor) {
        return valor.setScale(ESCALA, ARREDONDAMENTO);
    }
}
