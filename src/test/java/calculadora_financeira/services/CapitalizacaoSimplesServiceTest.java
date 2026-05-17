package calculadora_financeira.services;

import calculadora_financeira.dtos.req.*;
import calculadora_financeira.enums.UnidadeTempoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CapitalizacaoSimplesServiceTest {

    private final CapitalizacaoSimplesService service = new CapitalizacaoSimplesService();

    // ==========================================================
    // VALOR PRESENTE (VP)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "1100, 0.05, Dia, 2, Dia, 1000",
        "5000, 0.00, Dia, 10, Dia, 5000",
        "1100, 0.05, Mes, 2, Mes, 1000",
        "1100, 0.60, Ano, 60, Dia, 1000",
        "1100, 0.10, Semestre, 1, Semestre, 1000",
        "1100, 0.05, Trimestre, 2, Trimestre, 1000"
    })
    void obterValorPresente(String vf, String i, String unTaxa, String n, String unTempo, String esperadoValor) {
        ValorPresenteReqDTO dto = new ValorPresenteReqDTO(
            bd(vf), bd(i), UnidadeTempoEnum.valueOf(unTaxa), bd(n), UnidadeTempoEnum.valueOf(unTempo)
        );
        assertEquals(bd(esperadoValor), service.obterValorPresente(dto));
    }

    @Test
    void obterValorPresenteDeveLancarExcecaoQuandoDivisorForZero() {
        ValorPresenteReqDTO dto = new ValorPresenteReqDTO(bd("1000"), bd("-0.5"), UnidadeTempoEnum.Dia, bd("2"), UnidadeTempoEnum.Dia);
        assertThrows(IllegalArgumentException.class, () -> service.obterValorPresente(dto));
    }

    // ==========================================================
    // VALOR FUTURO (VF)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "1000, 0.05, Dia, 2, Dia, 1100",
        "1000, 0.05, Mes, 2, Mes, 1100",
        "1000, 0.60, Ano, 60, Dia, 1100",
        "1000, 0.10, Semestre, 1, Semestre, 1100",
        "1000, 0.05, Trimestre, 2, Trimestre, 1100"
    })
    void obterValorFuturo(String vp, String i, String unTaxa, String n, String unTempo, String esperadoValor) {
        ValorFuturoReqDTO dto = new ValorFuturoReqDTO(
            bd(vp), bd(i), UnidadeTempoEnum.valueOf(unTaxa), bd(n), UnidadeTempoEnum.valueOf(unTempo)
        );
        assertEquals(bd(esperadoValor), service.obterValorFuturo(dto));
    }

    // ==========================================================
    // JUROS (J)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "1000, 0.05, Dia, 2, Dia, 100",
        "1000, 0.05, Mes, 2, Mes, 100",
        "1000, 0.60, Ano, 60, Dia, 100",
        "1000, 0.10, Semestre, 1, Semestre, 100",
        "1000, 0.05, Trimestre, 2, Trimestre, 100"
    })
    void obterJuros(String vp, String i, String unTaxa, String n, String unTempo, String esperadoValor) {
        JurosReqDTO dto = new JurosReqDTO(
            bd(vp), bd(i), UnidadeTempoEnum.valueOf(unTaxa), bd(n), UnidadeTempoEnum.valueOf(unTempo)
        );
        assertEquals(bd(esperadoValor), service.obterJuros(dto));
    }

    // ==========================================================
    // TAXA (i)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "1000, 1100, 2, Dia, 0.050000",
        "1000, 1060, 2, Mes, 0.030000",
        "1000, 1090, 1, Trimestre, 0.090000",
        "1000, 1180, 1, Semestre, 0.180000",
        "1000, 1360, 1, Ano, 0.360000"
    })
    void obterTaxa(String vp, String vf, String n, String unTempo, String esperadoValor) {
        TaxaReqDTO dto = new TaxaReqDTO(bd(vp), bd(vf), bd(n), UnidadeTempoEnum.valueOf(unTempo));
        assertEquals(bd(esperadoValor), service.obterTaxa(dto));
    }

    @Test
    void obterTaxaDeveLancarExcecaoQuandoTempoForZero() {
        TaxaReqDTO dto = new TaxaReqDTO(bd("1000"), bd("1100"), bd("0"), UnidadeTempoEnum.Dia);
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxa(dto));
    }

    // ==========================================================
    // TEMPO (n)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "1000, 1100, 0.05, Dia, 2.000000",
        "1000, 1060, 0.03, Mes, 2.000000",
        "1000, 1090, 0.09, Trimestre, 1.000000",
        "1000, 1180, 0.18, Semestre, 1.000000",
        "1000, 1360, 0.36, Ano, 1.000000"
    })
    void obterTempo(String vp, String vf, String i, String unTaxa, String esperadoValor) {
        TempoReqDTO dto = new TempoReqDTO(bd(vp), bd(vf), bd(i), UnidadeTempoEnum.valueOf(unTaxa));
        assertEquals(bd(esperadoValor), service.obterTempo(dto));
    }

    @Test
    void obterTempoDeveLancarExcecaoQuandoTaxaForZero() {
        TempoReqDTO dto = new TempoReqDTO(bd("1000"), bd("1100"), bd("0"), UnidadeTempoEnum.Dia);
        assertThrows(IllegalArgumentException.class, () -> service.obterTempo(dto));
    }

    // ==========================================================
    // TAXA DO DESCONTO COMERCIAL (ic)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "0.10, Dia, 2, Dia, 0.083333",
        "0.03, Mes, 1, Mes, 0.029126",
        "0.09, Trimestre, 1, Trimestre, 0.082569",
        "0.18, Semestre, 1, Semestre, 0.152542",
        "0.36, Ano, 1, Ano, 0.264706"
    })
    void obterTaxaDoDescontoComercial(String i, String unTaxa, String n, String unTempo, String esperadoValor) {
        TaxaDoDescontoComercialReqDTO dto = new TaxaDoDescontoComercialReqDTO(
            bd(i), UnidadeTempoEnum.valueOf(unTaxa), bd(n), UnidadeTempoEnum.valueOf(unTempo)
        );
        assertEquals(bd(esperadoValor), service.obterTaxaDoDescontoComercial(dto));
    }

    @Test
    void obterTaxaDoDescontoComercialDeveLancarExcecaoQuandoDivisorForZero() {
        TaxaDoDescontoComercialReqDTO dto = new TaxaDoDescontoComercialReqDTO(bd("-0.5"), UnidadeTempoEnum.Dia, bd("2"), UnidadeTempoEnum.Dia);
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxaDoDescontoComercial(dto));
    }

    // ==========================================================
    // TAXA EFETIVA (i)
    // ==========================================================
    @ParameterizedTest
    @CsvSource({
        "0.05, Dia, 2, Dia, 0.055556",
        "0.03, Mes, 1, Mes, 0.030928",
        "0.09, Trimestre, 1, Trimestre, 0.098901",
        "0.18, Semestre, 1, Semestre, 0.219512",
        "0.36, Ano, 1, Ano, 0.562500"
    })
    void obterTaxaEfetiva(String ic, String unTaxa, String n, String unTempo, String esperadoValor) {
        TaxaEfetivaReqDTO dto = new TaxaEfetivaReqDTO(
            bd(ic), UnidadeTempoEnum.valueOf(unTaxa), bd(n), UnidadeTempoEnum.valueOf(unTempo)
        );
        assertEquals(bd(esperadoValor), service.obterTaxaEfetiva(dto));
    }

    @Test
    void obterTaxaEfetivaDeveLancarExcecaoQuandoDivisorForZero() {
        TaxaEfetivaReqDTO dto = new TaxaEfetivaReqDTO(bd("0.5"), UnidadeTempoEnum.Dia, bd("2"), UnidadeTempoEnum.Dia);
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxaEfetiva(dto));
    }

    private BigDecimal bd(String valor) {
        return new BigDecimal(valor).setScale(6, RoundingMode.HALF_UP);
    }
}