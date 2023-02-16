package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDePagamentoTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao() {
        Leilao leilao = leilao();
        Lance lance = leilao.getLanceVencedor();


        LocalDate data = LocalDate.of(2023, 3 , 6);

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(lance);
        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(leilao, pagamento.getLeilao());
        assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
        assertEquals(lance.getValor(), pagamento.getValor());
        assertEquals(lance.getUsuario(), pagamento.getUsuario());
        assertFalse(pagamento.getPago());
    }

    private Leilao leilao() {
        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Ciclano"),
                new BigDecimal("900"));

        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);

        return leilao;
    }
}