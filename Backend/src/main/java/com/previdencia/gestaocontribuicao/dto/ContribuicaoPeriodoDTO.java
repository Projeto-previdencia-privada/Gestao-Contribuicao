package com.previdencia.gestaocontribuicao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa os períodos de contribuição de um contribuinte.")
public class ContribuicaoPeriodoDTO {

    @Schema(description = "CPF do contribuinte", example = "12345678901", required = true)
    @CPF(message = "CPF deve conter 11 números")
    private String cpf;

    @Schema(description = "Lista de períodos de contribuição", example = "[\"01-01-2020\", \"01-01-2021\"]", required = true)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private List<LocalDate> periodos;
}
