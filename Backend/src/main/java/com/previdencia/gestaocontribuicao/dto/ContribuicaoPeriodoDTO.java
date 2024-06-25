package com.previdencia.gestaocontribuicao.controller;

import com.previdencia.gestaocontribuicao.dto.ContribuinteDTO;
import com.previdencia.gestaocontribuicao.dto.ContribuicaoPeriodoDTO;
import com.previdencia.gestaocontribuicao.service.AliquotaService;
import com.previdencia.gestaocontribuicao.service.ContribuinteService;
import com.previdencia.gestaocontribuicao.service.SalarioMinimoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/contribuintes")
public class ContribuinteController {

    @Autowired
    private ContribuinteService contribuinteService;

    @Autowired
    private SalarioMinimoService salarioMinimoService;

    @Autowired
    private AliquotaService aliquotaService;

    private Map<String, Map<String, Object>> contribuicoesCache = new LinkedHashMap<>();

    public ContribuinteController() {
    }

    @Operation(summary = "Consulta as informações do contribuinte com base no CPF", description = "Retorna informações detalhadas sobre o contribuinte.")
    @GetMapping("/contribuinte/{cpf}")
    public ResponseEntity<?> consultarContribuinte(@PathVariable @Valid @CPF(message = "CPF deve conter 11 números") String cpf) {
        try {
            ContribuinteDTO contribuinteDTO = this.contribuinteService.buscarDadosContribuinte(cpf);
            return ResponseEntity.ok(contribuinteDTO);
        } catch (RestClientException e) {
            return ResponseEntity.status(503).body(Map.of("STATUS", 503, "ERRO", "Nenhum dado encontrado para o CPF: " + cpf));
        }
    }

    @Operation(summary = "Calcula a contribuição com base nas datas fornecidas", description = "Recebe CPF e períodos de contribuição para calcular o total contribuído.")
    @PostMapping("/contribuicoes")
    public ResponseEntity<?> calcularContribuicao(@RequestBody @Valid ContribuicaoPeriodoDTO contribuicaoPeriodDTO) {
        try {
            ContribuinteDTO contribuinteDTO = this.contribuinteService.buscarDadosContribuinte(contribuicaoPeriodDTO.getCpf());
            Map<String, Object> response = this.calcularContribuicao(contribuinteDTO, contribuicaoPeriodDTO.getPeriodos());
            contribuicoesCache.put(contribuicaoPeriodDTO.getCpf(), response);
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            return ResponseEntity.status(503).body(Map.of("STATUS", 503, "ERRO", "Nenhum dado encontrado para o CPF: " + contribuicaoPeriodDTO.getCpf()));
        }
    }

    @Operation(summary = "Retorna as informações calculadas de contribuição", description = "Retorna todas as informações calculadas de contribuição com base no CPF.")
    @GetMapping("/consultar/{cpf}")
    public ResponseEntity<?> getContribuicoesCalculadas(@PathVariable @Valid @CPF(message = "CPF deve conter 11 números") String cpf) {
        if (!contribuicoesCache.containsKey(cpf)) {
            return ResponseEntity.status(400).body(Map.of("STATUS", 400, "ERRO", "Os dados calculados de contribuição não foram encontrados."));
        }
        return ResponseEntity.ok(contribuicoesCache.get(cpf));
    }

    private Map<String, Object> calcularContribuicao(ContribuinteDTO contribuinteDTO, List<LocalDate> periodos) {
        ContribuinteDTO.Info info = contribuinteDTO.getInfo();
        BigDecimal aliquota = this.aliquotaService.buscarAliquotaPorCategoriaESalario(info.getCategoria(), info.getSalario());
        BigDecimal valorContribuicaoMensal = info.getSalario().multiply(aliquota.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        BigDecimal totalContribuido = BigDecimal.ZERO;
        BigDecimal totalContribuidoAjustado = BigDecimal.ZERO;

        for (LocalDate periodo : periodos) {
            BigDecimal salarioMinimoInicio = this.salarioMinimoService.buscarValorSalarioMinimoParaData(periodo);
            BigDecimal salarioMinimoAtual = this.salarioMinimoService.buscarValorSalarioMinimoParaData(LocalDate.now());
            BigDecimal contribuicaoAjustada = valorContribuicaoMensal.multiply(salarioMinimoAtual).divide(salarioMinimoInicio);
            totalContribuido = totalContribuido.add(valorContribuicaoMensal);
            totalContribuidoAjustado = totalContribuidoAjustado.add(contribuicaoAjustada);
        }

        BigDecimal valorAjusteAplicado = totalContribuidoAjustado.subtract(totalContribuido);
        long totalMesesContribuicao = periodos.size(); // Cada entrada em periodos representa um mês de contribuição

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cpf", info.getCpf());
        response.put("categoria", info.getCategoria());
        response.put("salario", info.getSalario());
        response.put("aliquota", aliquota);
        response.put("periodosContribuicao", periodos);
        response.put("tempoContribuicaoMeses", totalMesesContribuicao); // Adiciona o total de períodos contribuídos
        response.put("valorContribuicaoMensal", valorContribuicaoMensal.setScale(2, RoundingMode.HALF_UP));
        response.put("totalContribuidoSemAjuste", totalContribuido.setScale(2, RoundingMode.HALF_UP));
        response.put("valorAjusteAplicado", valorAjusteAplicado.setScale(2, RoundingMode.HALF_UP));
        response.put("totalContribuidoAjustado", totalContribuidoAjustado.setScale(2, RoundingMode.HALF_UP));
        return response;
    }

}
