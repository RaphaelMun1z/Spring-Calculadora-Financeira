package calculadora_financeira.services;

import calculadora_financeira.dtos.req.AmortizacaoReqDTO;
import calculadora_financeira.dtos.req.VPLReqDTO;
import calculadora_financeira.dtos.res.AmortizacaoResDTO;
import calculadora_financeira.dtos.res.ParcelaAmortizacaoResDTO;
import calculadora_financeira.dtos.res.VPLResDTO;
import calculadora_financeira.enums.UnidadeTempoEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AmortizacaoEVPLService {
    private static final int ESCALA = 6;
    private static final int ESCALA_INTERNA = 12;
    private static final RoundingMode ARREDONDAMENTO = RoundingMode.HALF_UP;
    private static final MathContext CONTEXTO_CALCULO = new MathContext(24, ARREDONDAMENTO);

    public AmortizacaoResDTO obterAmortizacaoSAC(AmortizacaoReqDTO dto) {
        validarAmortizacao(dto);

        int numeroParcelas = converterNumeroParcelas(dto.n());
        BigDecimal financiamento = arredondar(dto.VP());
        BigDecimal taxa = dto.i();
        BigDecimal quantidadeParcelas = BigDecimal.valueOf(numeroParcelas);

        BigDecimal amortizacaoConstante = financiamento.divide(
            quantidadeParcelas,
            ESCALA,
            ARREDONDAMENTO
        );

        BigDecimal saldoDevedor = financiamento;
        BigDecimal totalJuros = zero();
        BigDecimal totalPrestacoes = zero();
        List<ParcelaAmortizacaoResDTO> parcelas = new ArrayList<>();

        for (int periodo = 1; periodo <= numeroParcelas; periodo++) {
            BigDecimal saldoDevedorInicial = saldoDevedor;

            BigDecimal amortizacao = periodo == numeroParcelas
                ? saldoDevedorInicial
                : amortizacaoConstante.min(saldoDevedorInicial);

            BigDecimal juros = saldoDevedorInicial
                .multiply(taxa)
                .setScale(ESCALA, ARREDONDAMENTO);

            BigDecimal prestacao = amortizacao
                .add(juros)
                .setScale(ESCALA, ARREDONDAMENTO);

            BigDecimal saldoDevedorFinal = saldoDevedorInicial
                .subtract(amortizacao)
                .setScale(ESCALA, ARREDONDAMENTO);

            if (saldoDevedorFinal.signum() < 0) {
                saldoDevedorFinal = zero();
            }

            parcelas.add(new ParcelaAmortizacaoResDTO(
                BigDecimal.valueOf(periodo),
                arredondar(saldoDevedorInicial),
                arredondar(amortizacao),
                arredondar(juros),
                arredondar(prestacao),
                arredondar(saldoDevedorFinal)
            ));

            totalJuros = totalJuros.add(juros);
            totalPrestacoes = totalPrestacoes.add(prestacao);
            saldoDevedor = saldoDevedorFinal;
        }

        return new AmortizacaoResDTO(
            financiamento,
            arredondar(taxa),
            dto.unidadeTaxaJurosEnum(),
            dto.n(),
            dto.unidadeTempoEnum(),
            arredondar(amortizacaoConstante),
            List.copyOf(parcelas),
            arredondar(totalJuros),
            arredondar(totalPrestacoes)
        );
    }

    public VPLResDTO obterVPL(VPLReqDTO dto) {
        validarVPL(dto);

        int quantidadePeriodos = converterNumeroParcelas(dto.n());
        int periodoValorResidual = converterPeriodoValorResidual(
            dto.periodoValorResidual()
        );

        BigDecimal valorPresenteFluxos = zeroInterno();

        for (int indice = 0; indice < quantidadePeriodos; indice++) {
            int periodo = indice + 1;
            BigDecimal fluxo = dto.fluxosCaixa().get(indice);
            BigDecimal fatorDesconto = obterFatorDesconto(dto.i(), periodo);

            BigDecimal fluxoDescontado = fluxo.divide(
                fatorDesconto,
                ESCALA_INTERNA,
                ARREDONDAMENTO
            );

            valorPresenteFluxos = valorPresenteFluxos.add(fluxoDescontado);
        }

        BigDecimal fatorDescontoResidual = obterFatorDesconto(
            dto.i(),
            periodoValorResidual
        );

        BigDecimal valorPresenteResidual = dto.valorResidual().divide(
            fatorDescontoResidual,
            ESCALA_INTERNA,
            ARREDONDAMENTO
        );

        BigDecimal vpl = valorPresenteFluxos
            .add(valorPresenteResidual)
            .subtract(dto.investimento());

        return new VPLResDTO(
            arredondar(dto.investimento()),
            arredondar(dto.i()),
            dto.unidadeTaxaJurosEnum(),
            dto.n(),
            dto.unidadeTempoEnum(),
            arredondar(valorPresenteFluxos),
            arredondar(valorPresenteResidual),
            arredondar(vpl)
        );
    }

    private BigDecimal obterFatorDesconto(BigDecimal taxa, int periodo) {
        return BigDecimal.ONE
            .add(taxa, CONTEXTO_CALCULO)
            .pow(periodo, CONTEXTO_CALCULO);
    }

    private void validarAmortizacao(AmortizacaoReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                "DTO de amortização não pode ser null."
            );
        }

        if (dto.VP() == null || dto.VP().signum() <= 0) {
            throw new IllegalArgumentException(
                "O financiamento (VP) deve ser maior que zero."
            );
        }

        validarTaxaTempoEUnidades(
            dto.i(),
            dto.n(),
            dto.unidadeTaxaJurosEnum(),
            dto.unidadeTempoEnum()
        );

        converterNumeroParcelas(dto.n());
    }

    private void validarVPL(VPLReqDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                "DTO de VPL não pode ser null."
            );
        }

        if (dto.investimento() == null || dto.investimento().signum() <= 0) {
            throw new IllegalArgumentException(
                "O investimento deve ser maior que zero."
            );
        }

        validarTaxaTempoEUnidades(
            dto.i(),
            dto.n(),
            dto.unidadeTaxaJurosEnum(),
            dto.unidadeTempoEnum()
        );

        int quantidadePeriodos = converterNumeroParcelas(dto.n());

        if (dto.fluxosCaixa() == null) {
            throw new IllegalArgumentException(
                "A lista de fluxos de caixa não pode ser null."
            );
        }

        if (dto.fluxosCaixa().size() != quantidadePeriodos) {
            throw new IllegalArgumentException(
                "A quantidade de fluxos de caixa deve ser igual ao tempo (n)."
            );
        }

        if (dto.fluxosCaixa().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                "Os fluxos de caixa não podem conter valores null."
            );
        }

        if (dto.valorResidual() == null || dto.valorResidual().signum() < 0) {
            throw new IllegalArgumentException(
                "O valor residual não pode ser negativo."
            );
        }

        if (dto.periodoValorResidual() == null) {
            throw new IllegalArgumentException(
                "O período do valor residual não pode ser null."
            );
        }

        converterPeriodoValorResidual(dto.periodoValorResidual());
    }

    private void validarTaxaTempoEUnidades(
        BigDecimal taxa,
        BigDecimal tempo,
        UnidadeTempoEnum unidadeTaxa,
        UnidadeTempoEnum unidadeTempo
    ) {
        if (taxa == null || taxa.signum() < 0) {
            throw new IllegalArgumentException(
                "A taxa (i) não pode ser negativa."
            );
        }

        if (tempo == null || tempo.signum() <= 0) {
            throw new IllegalArgumentException(
                "O tempo (n) deve ser maior que zero."
            );
        }

        if (unidadeTaxa == null) {
            throw new IllegalArgumentException(
                "A unidade da taxa de juros não pode ser null."
            );
        }

        if (unidadeTempo == null) {
            throw new IllegalArgumentException(
                "A unidade de tempo não pode ser null."
            );
        }

        if (unidadeTaxa != unidadeTempo) {
            throw new IllegalArgumentException(
                "A unidade da taxa de juros deve ser igual à unidade de tempo."
            );
        }
    }

    private int converterNumeroParcelas(BigDecimal numeroParcelas) {
        try {
            int valor = numeroParcelas.intValueExact();

            if (valor <= 0) {
                throw new IllegalArgumentException(
                    "n deve ser um número inteiro maior que zero."
                );
            }

            return valor;
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                "n deve ser um número inteiro válido."
            );
        }
    }

    private int converterPeriodoValorResidual(BigDecimal periodo) {
        try {
            int valor = periodo.intValueExact();

            if (valor < 0) {
                throw new IllegalArgumentException(
                    "O período do valor residual deve ser maior ou igual a zero."
                );
            }

            return valor;
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException(
                "O período do valor residual deve ser um número inteiro válido."
            );
        }
    }

    private BigDecimal arredondar(BigDecimal valor) {
        return valor.setScale(ESCALA, ARREDONDAMENTO);
    }

    private BigDecimal zero() {
        return BigDecimal.ZERO.setScale(ESCALA, ARREDONDAMENTO);
    }

    private BigDecimal zeroInterno() {
        return BigDecimal.ZERO.setScale(ESCALA_INTERNA, ARREDONDAMENTO);
    }
}