package org.agenda;

import org.agenda.model.Contato;
import org.agenda.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgendaServiceTest {

    private AgendaService agenda;

    @BeforeEach
    void setUp() {
        agenda = new AgendaService();
    }

    @Test
    void testaAdicionarContato() {
        boolean resultado = agenda.adicionar("João", "999", "joao@email.com", LocalDate.of(2000, 1, 1));

        assertTrue(resultado);
        assertEquals(1, agenda.listar().size());

        Contato c = agenda.buscarPorId(1);
        assertNotNull(c);
        assertEquals("João", c.getNome());
        assertEquals("joao@email.com", c.getEmail());
    }

    @Test
    void testaNaoAdicionarEmailDuplicado() {
        agenda.adicionar("João", "999", "email@test.com", LocalDate.of(2000, 1, 1));
        boolean resultado = agenda.adicionar("Maria", "888", "email@test.com", LocalDate.of(1999, 2, 2));

        assertFalse(resultado);
        assertEquals(1, agenda.listar().size());
    }

    @Test
    void testaNaoAdicionarEmailDuplicadoCaseInsensitive() {
        agenda.adicionar("João", "999", "EMAIL@TEST.COM", LocalDate.of(2000, 1, 1));

        boolean resultado = agenda.adicionar("Maria", "888", "email@test.com", LocalDate.of(1999, 2, 2));

        assertFalse(resultado);
        assertEquals(1, agenda.listar().size());
    }

    @Test
    void testaListarAgendaVazia() {
        List<Contato> lista = agenda.listar();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }

    @Test
    void testaListarOrdenadoPorNome() {
        agenda.adicionar("Carlos", "111", "c@test.com", LocalDate.of(2000, 1, 1));
        agenda.adicionar("Ana", "222", "a@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> lista = agenda.listar();

        assertEquals("Ana", lista.get(0).getNome());
        assertEquals("Carlos", lista.get(1).getNome());
    }

    @Test
    void testaRemoverContato() {
        agenda.adicionar("João", "999", "j@test.com", LocalDate.of(2000, 1, 1));

        boolean removido = agenda.remover(1);

        assertTrue(removido);
        assertEquals(0, agenda.listar().size());
    }

    @Test
    void testaRemoverIdInexistente() {
        boolean resultado = agenda.remover(99);

        assertFalse(resultado);
    }

    @Test
    void testaEditarContato() {
        agenda.adicionar("João", "999", "j@test.com", LocalDate.of(2000, 1, 1));

        boolean editado = agenda.editar(1, "João Silva", "888", "novo@test.com", LocalDate.of(1995, 5, 5));

        assertTrue(editado);

        Contato c = agenda.buscarPorId(1);
        assertEquals("João Silva", c.getNome());
        assertEquals("888", c.getTelefone());
        assertEquals("novo@test.com", c.getEmail());
        assertEquals(LocalDate.of(1995, 5, 5), c.getDataNascimento());
    }

    @Test
    void testaEditarIdInexistente() {
        boolean resultado = agenda.editar(99, "X", "0", "x@x.com", LocalDate.now());

        assertFalse(resultado);
    }

    @Test
    void testaBuscarPorId() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 1, 1));

        Contato c = agenda.buscarPorId(1);

        assertNotNull(c);
        assertEquals("Ana", c.getNome());
    }

    @Test
    void testaBuscarPorIdInexistente() {
        Contato c = agenda.buscarPorId(99);

        assertNull(c);
    }

    @Test
    void testaPesquisarPorPrefixo() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 1, 1));
        agenda.adicionar("Carlos", "222", "c@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorPrefixo("An");

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());
    }

    @Test
    void testaPesquisarPorPrefixoInexistente() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorPrefixo("ZZZ");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testaPesquisarPorPrefixoCaseInsensitive() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorPrefixo("an");

        assertEquals(1, resultado.size());
    }

    @Test
    void testaPesquisarPorTelefone() {
        agenda.adicionar("Ana", "12345", "a@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorTelefone("123");

        assertEquals(1, resultado.size());
    }

    @Test
    void testaPesquisarPorTelefoneInexistente() {
        agenda.adicionar("Ana", "12345", "a@test.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorTelefone("99999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testaPesquisarPorEmail() {
        agenda.adicionar("Ana", "111", "ana@gmail.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorEmail("gmail");

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());
    }

    @Test
    void testaPesquisarPorEmailInexistente() {
        agenda.adicionar("Ana", "111", "ana@gmail.com", LocalDate.of(2000, 1, 1));

        List<Contato> resultado = agenda.pesquisarPorEmail("outlook");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testaEmailJaExiste() {
        agenda.adicionar("João", "999", "j@test.com", LocalDate.of(2000, 1, 1));

        assertTrue(agenda.emailJaExiste("j@test.com", 99));
    }

    @Test
    void testaEmailJaExisteIgnorandoProprioId() {
        agenda.adicionar("João", "999", "j@test.com", LocalDate.of(2000, 1, 1));
        assertFalse(agenda.emailJaExiste("j@test.com", 1));
    }

    @Test
    void testaAniversariantesDoMes() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 5, 10));
        agenda.adicionar("Carlos", "222", "c@test.com", LocalDate.of(2000, 6, 10));

        List<Contato> resultado = agenda.aniversariantesDoMes(5);

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());
    }

    @Test
    void testaAniversariantesDoMesSemResultado() {
        agenda.adicionar("Ana", "111", "a@test.com", LocalDate.of(2000, 5, 10));

        List<Contato> resultado = agenda.aniversariantesDoMes(12);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testaAniversariantesOrdenadosPorDia() {
        agenda.adicionar("B", "1", "b@t.com", LocalDate.of(2000, 3, 20));
        agenda.adicionar("A", "2", "a@t.com", LocalDate.of(2000, 3, 5));

        List<Contato> resultado = agenda.aniversariantesDoMes(3);

        // Dia 5 deve vir antes do dia 20
        assertEquals("A", resultado.get(0).getNome());
        assertEquals("B", resultado.get(1).getNome());
    }
}