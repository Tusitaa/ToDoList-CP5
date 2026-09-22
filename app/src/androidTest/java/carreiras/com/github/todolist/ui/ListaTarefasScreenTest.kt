package carreiras.com.github.todolist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import carreiras.com.github.todolist.data.Tarefa
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ListaTarefasScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun cancelarEConfirmarExclusaoAfetamSomenteATarefaSelecionada() {
        val primeiraTarefa = Tarefa(id = 1, titulo = "Estudar Room", descricao = "")
        val segundaTarefa = Tarefa(id = 2, titulo = "Enviar atividade", descricao = "")
        var tarefaParaExcluir by mutableStateOf<Tarefa?>(null)
        var tarefaExcluida: Tarefa? = null

        composeRule.setContent {
            ListaTarefasContent(
                tarefas = listOf(primeiraTarefa, segundaTarefa),
                tarefaParaExcluir = tarefaParaExcluir,
                onNovaTarefa = {},
                onEditarTarefa = {},
                onCheckedChange = { _, _ -> },
                onSolicitarExclusao = { tarefaParaExcluir = it },
                onCancelarExclusao = { tarefaParaExcluir = null },
                onConfirmarExclusao = {
                    tarefaExcluida = it
                    tarefaParaExcluir = null
                }
            )
        }

        composeRule
            .onNodeWithContentDescription("Excluir tarefa Estudar Room")
            .performClick()
        composeRule.onNodeWithText("Excluir tarefa?").assertIsDisplayed()
        composeRule.onAllNodesWithText("Estudar Room").assertCountEquals(2)

        composeRule.onNodeWithText("Cancelar").performClick()

        composeRule.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        assertNull(tarefaExcluida)

        composeRule
            .onNodeWithContentDescription("Excluir tarefa Enviar atividade")
            .performClick()
        composeRule.onAllNodesWithText("Enviar atividade").assertCountEquals(2)
        composeRule.onNodeWithText("Excluir").performClick()

        composeRule.onNodeWithText("Excluir tarefa?").assertDoesNotExist()
        assertEquals(segundaTarefa, tarefaExcluida)
    }
}
