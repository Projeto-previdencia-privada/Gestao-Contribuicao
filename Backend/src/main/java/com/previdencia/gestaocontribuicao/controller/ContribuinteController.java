package com.previdencia.gestaocontribuicao.controller;
import com.previdencia.gestaocontribuicao.dto.ContribuinteDTO;
import com.previdencia.gestaocontribuicao.service.AliquotaService;
import com.previdencia.gestaocontribuicao.service.ContribuinteService;
import com.previdencia.gestaocontribuicao.service.SalarioMinimoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@Validated
@RestController
@RequestMapping({"/contribuintes"})
public class ContribuinteController {
    @Autowired
    private ContribuinteService contribuinteService;
    @Autowired
    private SalarioMinimoService salarioMinimoService;
    @Autowired
    private AliquotaService aliquotaService;

    public ContribuinteController() {
    }

    @Operation(
            summary = "Consulta as informações do contribuinte com base no CPF",
            description = "Retorna informações detalhadas sobre o contribuinte, incluindo categoria, tempo de contribuição e total contribuído ajustado."
    )
    @GetMapping({"/consultar/{cpf}"})
    public ResponseEntity<?> consultarContribuinte(@PathVariable @Valid @CPF(
            message = "CPF deve conter 11 números"
    ) String cpf) {
        try {
            ContribuinteDTO contribuinteDTO = this.contribuinteService.buscarDadosContribuinte(cpf);
            Map<String, Object> response = this.calcularContribuicao(contribuinteDTO);
            return ResponseEntity.ok(response);
        } catch (RestClientException var4) {
            return ResponseEntity.status(503).body(Map.of("STATUS", 503, "ERRO", "Nenhum dado encontrado para o CPF: " + cpf));
        }
    }

    private Map<String, Object> calcularContribuicao(ContribuinteDTO contribuinteDTO) {
        ContribuinteDTO.Info info = contribuinteDTO.getInfo();
        LocalDate inicioContribuicao = info.getInicioContribuicao();
        long mesesContribuicao = ChronoUnit.MONTHS.between(inicioContribuicao, LocalDate.now());
        BigDecimal aliquota = this.aliquotaService.buscarAliquotaPorCategoriaESalario(info.getCategoria(), info.getSalario());
        BigDecimal salarioMinimoInicio = this.salarioMinimoService.buscarValorSalarioMinimoParaData(inicioContribuicao);
        BigDecimal salarioMinimoAtual = this.salarioMinimoService.buscarValorSalarioMinimoParaData(LocalDate.now());
        BigDecimal valorContribuicaoMensal = info.getSalario().multiply(aliquota.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        BigDecimal totalContribuido = valorContribuicaoMensal.multiply(new BigDecimal(mesesContribuicao));
        BigDecimal totalContribuidoAjustado = totalContribuido.multiply(salarioMinimoAtual).divide(salarioMinimoInicio, 2, RoundingMode.HALF_UP);
        BigDecimal valorAjusteAplicado = totalContribuidoAjustado.subtract(totalContribuido);
        Map<String, Object> response = new LinkedHashMap();
        response.put("cpf", info.getCpf());
        response.put("categoria", info.getCategoria());
        response.put("salario", info.getSalario());
        response.put("aliquota", aliquota);
        response.put("tempoContribuicaoMeses", mesesContribuicao);
        response.put("valorContribuicaoMensal", valorContribuicaoMensal);
        response.put("totalContribuidoSemAjuste", totalContribuido);
        response.put("valorAjusteAplicado", valorAjusteAplicado);
        response.put("totalContribuidoAjustado", totalContribuidoAjustado);
        return response;
    }
}

