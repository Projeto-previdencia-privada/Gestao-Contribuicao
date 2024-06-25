import { useState } from "react";
import api from "../axiosconfig";
import "../Form/Form.css";

const ContribuinteConsulta = () => {
  const [cpf, setCpf] = useState("");
  const [contribuinte, setContribuinte] = useState(null);
  const [periodos, setPeriodos] = useState([{ mes: "", ano: "" }]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = async () => {
    if (!cpf) {
      setError("Por favor, insira um CPF.");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await api.get(`/contribuintes/contribuinte/${cpf}`);
      const contribuinteData = response.data.info;
      setContribuinte(contribuinteData);
      const contribuicoesResponse = await api.get(`/contribuintes/consultar/${cpf}`);
      if (contribuicoesResponse.data) {
        setContribuinte(prevState => ({
          ...prevState,
          ...contribuicoesResponse.data
        }));
      }
      setPeriodos([{ mes: "", ano: "" }]); // Limpar os períodos de contribuição ao pesquisar novo CPF
    } catch (error) {
      setError("Erro ao buscar contribuinte");
      console.error("Erro ao buscar contribuinte:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddPeriodo = () => {
    setPeriodos([...periodos, { mes: "", ano: "" }]);
  };

  const handleRemovePeriodo = (index) => {
    const newPeriodos = periodos.filter((_, i) => i !== index);
    setPeriodos(newPeriodos.length > 0 ? newPeriodos : [{ mes: "", ano: "" }]);
  };

  const handlePeriodoChange = (index, field, value) => {
    const newPeriodos = [...periodos];
    newPeriodos[index][field] = value;
    setPeriodos(newPeriodos);
  };

  const handleCalculate = async () => {
    const currentYear = new Date().getFullYear();

    if (!cpf || periodos.some(periodo => !periodo.mes || !periodo.ano)) {
      setError("Por favor, insira o CPF e todos os períodos de contribuição.");
      return;
    }

    for (const periodo of periodos) {
      const mes = parseInt(periodo.mes, 10);
      const ano = parseInt(periodo.ano, 10);

      if (isNaN(mes) || mes < 1 || mes > 12) {
        setError("Por favor, insira um mês válido (01-12).");
        return;
      }

      if (isNaN(ano) || ano < 1994 || ano > currentYear) {
        setError(`Por favor, insira um ano válido entre 1994 e ${currentYear}.`);
        return;
      }
    }

    const formattedPeriodos = [...new Set(periodos.map(({ mes, ano }) => `01-${mes.padStart(2, '0')}-${ano}`))];

    try {
      setLoading(true);
      setError(null);
      const response = await api.post(`/contribuintes/contribuicoes`, {
        cpf,
        periodos: formattedPeriodos,
      });
      setContribuinte(prevState => ({
        ...prevState,
        ...response.data
      }));
    } catch (error) {
      setError("Erro ao calcular contribuições");
      console.error("Erro ao calcular contribuições:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form">
      <h1 className="h1">Consulta de Contribuinte</h1>
      <input
        type="text"
        id="cpf"
        name="cpf"
        value={cpf}
        onChange={(e) => setCpf(e.target.value)}
        placeholder="Digite o CPF do contribuinte"
      />
      <button onClick={handleSearch} disabled={loading}>
        {loading ? "Consultando..." : "Consultar"}
      </button>
      {error && <p className="error">{error}</p>}
      {contribuinte && (
        <div>
          <p>CPF: {contribuinte.cpf}</p>
          <p>Categoria: {contribuinte.categoria}</p>
          <p>Salário: {contribuinte.salario}</p>
          {contribuinte.valorContribuicaoMensal !== undefined && (
            <>
              <p>Alíquota: {contribuinte.aliquota ? contribuinte.aliquota : 0}%</p>
              <p>Valor Contribuição Mensal: {contribuinte.valorContribuicaoMensal?.toFixed(2)}</p>
              <p>Total Contribuído (Sem Ajuste): {contribuinte.totalContribuidoSemAjuste?.toFixed(2)}</p>
              <p>Valor Ajuste Aplicado: {contribuinte.valorAjusteAplicado?.toFixed(2)}</p>
              <p>Total Contribuído (Ajustado): {contribuinte.totalContribuidoAjustado?.toFixed(2)}</p>
              <p>Tempo de Contribuição: {contribuinte.tempoContribuicaoMeses} meses</p>
            </>
          )}
          <div>
            <h2>Períodos de Contribuição</h2>
            {periodos.map((periodo, index) => (
              <div key={index}>
                <input
                  type="text"
                  id={`mes-${index}`}
                  name={`mes-${index}`}
                  value={periodo.mes}
                  onChange={(e) => handlePeriodoChange(index, "mes", e.target.value)}
                  placeholder="Mês (MM)"
                />
                <input
                  type="text"
                  id={`ano-${index}`}
                  name={`ano-${index}`}
                  value={periodo.ano}
                  onChange={(e) => handlePeriodoChange(index, "ano", e.target.value)}
                  placeholder="Ano (AAAA)"
                />
                <button onClick={() => handleRemovePeriodo(index)}>Remover</button>
              </div>
            ))}
            <button onClick={handleAddPeriodo}>Adicionar Período</button>
          </div>
          <button onClick={handleCalculate} disabled={loading}>
            {loading ? "Calculando..." : "Calcular Contribuições"}
          </button>
        </div>
      )}
    </div>
  );
};

export default ContribuinteConsulta;
