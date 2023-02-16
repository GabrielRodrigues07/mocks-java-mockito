package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;

	private Clock clock;

	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	private LocalDate proximoDiaUtil(LocalDate vencimento) {
		DayOfWeek dayOfWeek = vencimento.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.SATURDAY) {
			return vencimento.plusDays(2);
		} else if (dayOfWeek == DayOfWeek.SUNDAY) {
			return vencimento.plusDays(1);
		}
		return vencimento;
	}

}
