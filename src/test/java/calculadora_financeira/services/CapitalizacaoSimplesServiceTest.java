package calculadora_financeira.services;

import calculadora_financeira.dtos.req.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CapitalizacaoSimplesServiceTest {

    private final CapitalizacaoSimplesService service = new CapitalizacaoSimplesService();

    @ParameterizedTest
    @CsvSource({"1100, 0.05, 2, 1000", "5000, 0.00, 10, 5000", "2500, 0.12, 0, 2500", "3600, 0.20, 4, 2000", "1000, -0.10, 2, 1250", "-1000, 0.10, 2, -833.333333", "0, 0.10, 2, 0"})
    void obterValorPresente(String vf, String i, String n, String esperadoValor) {
        ValorPresenteReqDTO dto = new ValorPresenteReqDTO(bd(vf), bd(i), bd(n));
        BigDecimal calculado = service.obterValorPresente(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @Test
    void obterValorPresenteDeveLancarExcecaoQuandoDivisorForZero() {
        ValorPresenteReqDTO dto = new ValorPresenteReqDTO(bd("1000"), bd("-0.5"), bd("2"));
        assertThrows(IllegalArgumentException.class, () -> service.obterValorPresente(dto));
    }

    @ParameterizedTest
    @CsvSource({"1000, 0.05, 2, 1100", "2000, 0.10, 3, 2600", "5000, 0.00, 10, 5000", "2000, 0.50, 3, 5000", "1000, -0.10, 2, 800", "-1000, 0.10, 2, -1200", "0, 0.10, 5, 0"})
    void obterValorFuturo(String vp, String i, String n, String esperadoValor) {
        ValorFuturoReqDTO dto = new ValorFuturoReqDTO(bd(vp), bd(i), bd(n));
        BigDecimal calculado = service.obterValorFuturo(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @ParameterizedTest
    @CsvSource({"1000, 0.05, 2, 100", "3000, 0.00, 5, 0", "2500, 0.10, 4, 1000", "1500, 0.20, 3, 900", "1000, -0.10, 2, -200", "-1000, 0.10, 2, -200", "0, 0.10, 2, 0"})
    void obterJuros(String vp, String i, String n, String esperadoValor) {
        JurosReqDTO dto = new JurosReqDTO(bd(vp), bd(i), bd(n));
        BigDecimal calculado = service.obterJuros(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @ParameterizedTest
    @CsvSource({"1000, 1100, 2, 0.05", "1000, 1600, 6, 0.10", "2000, 2600, 3, 0.10", "5000, 5000, 10, 0.00", "1000, 800, 2, -0.10", "-1000, -1200, 2, 0.10"})
    void obterTaxa(String vp, String vf, String n, String esperadoValor) {
        TaxaReqDTO dto = new TaxaReqDTO(bd(vp), bd(vf), bd(n));
        BigDecimal calculado = service.obterTaxa(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @Test
    void obterTaxaDeveLancarExcecaoQuandoVpForZero() {
        TaxaReqDTO dto = new TaxaReqDTO(bd("0"), bd("1000"), bd("2"));
        assertThrows(ArithmeticException.class, () -> service.obterTaxa(dto));
    }

    @Test
    void obterTaxaDeveLancarExcecaoQuandoTempoForZero() {
        TaxaReqDTO dto = new TaxaReqDTO(bd("1000"), bd("1100"), bd("0"));
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxa(dto));
    }

    @ParameterizedTest
    @CsvSource({"1000, 1100, 0.05, 2", "1000, 1500, 0.25, 2", "2000, 2600, 0.10, 3", "5000, 5000, 0.15, 0", "1000, 800, -0.10, 2", "-1000, -1200, 0.10, 2"})
    void obterTempo(String vp, String vf, String i, String esperadoValor) {
        TempoReqDTO dto = new TempoReqDTO(bd(vp), bd(vf), bd(i));
        BigDecimal calculado = service.obterTempo(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @Test
    void obterTempoDeveLancarExcecaoQuandoVpForZero() {
        TempoReqDTO dto = new TempoReqDTO(bd("0"), bd("1000"), bd("0.10"));
        assertThrows(ArithmeticException.class, () -> service.obterTempo(dto));
    }

    @Test
    void obterTempoDeveLancarExcecaoQuandoTaxaForZero() {
        TempoReqDTO dto = new TempoReqDTO(bd("1000"), bd("1100"), bd("0"));
        assertThrows(IllegalArgumentException.class, () -> service.obterTempo(dto));
    }

    @ParameterizedTest
    @CsvSource({"0.10, 2, 0.083333", "0.20, 2, 0.142857", "0.05, 1, 0.047619", "0.15, 3, 0.103448", "-0.10, 2, -0.125000", "0, 2, 0"})
    void obterTaxaDoDescontoComercial(String i, String n, String esperadoValor) {
        TaxaDoDescontoComercialReqDTO dto = new TaxaDoDescontoComercialReqDTO(bd(i), bd(n));
        BigDecimal calculado = service.obterTaxaDoDescontoComercial(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @Test
    void obterTaxaDoDescontoComercialDeveLancarExcecaoQuandoDivisorForZero() {
        TaxaDoDescontoComercialReqDTO dto = new TaxaDoDescontoComercialReqDTO(bd("-0.5"), bd("2"));
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxaDoDescontoComercial(dto));
    }

    @ParameterizedTest
    @CsvSource({"0.05, 2, 0.055556", "0.10, 2, 0.125000", "0.20, 3, 0.500000", "0.02, 1, 0.020408", "-0.10, 2, -0.083333", "0, 2, 0"})
    void obterTaxaEfetiva(String ic, String n, String esperadoValor) {
        TaxaEfetivaReqDTO dto = new TaxaEfetivaReqDTO(bd(ic), bd(n));
        BigDecimal calculado = service.obterTaxaEfetiva(dto);
        BigDecimal esperado = bd(esperadoValor);
        assertEquals(esperado, calculado);
    }

    @Test
    void obterTaxaEfetivaDeveLancarExcecaoQuandoDivisorForZero() {
        TaxaEfetivaReqDTO dto = new TaxaEfetivaReqDTO(bd("0.5"), bd("2"));
        assertThrows(IllegalArgumentException.class, () -> service.obterTaxaEfetiva(dto));
    }

    private BigDecimal bd(String valor) {
        return new BigDecimal(valor).setScale(6, RoundingMode.HALF_UP);
    }
}