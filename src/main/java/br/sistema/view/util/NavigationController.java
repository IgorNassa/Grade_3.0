package br.sistema.view.util;

import br.sistema.view.component.MenuButton;
import br.sistema.view.panel.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador central de navegação — Princípio da Responsabilidade Única (SOLID).
 * Qualquer painel pode chamar NavigationController.getInstance().navegarPara(chave)
 * sem conhecer o DashFrame ou o SidebarPanel diretamente.
 */
public class NavigationController {

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static NavigationController instance;

    public static NavigationController getInstance() {
        if (instance == null) instance = new NavigationController();
        return instance;
    }

    // ── Estado ────────────────────────────────────────────────────────────────
    private ContentPanel contentPanel;
    private JFrame        frameRaiz;
    private String        telaAtual = ContentPanel.DASHBOARD;
    private boolean       geradorAtivo = false;

    // Mapa chave → MenuButton (para highlight automático)
    private final Map<String, MenuButton> mapaNavegacao = new HashMap<>();

    private NavigationController() {}

    // ── Registro (chamado pelo DashFrame na inicialização) ────────────────────
    public void registrar(ContentPanel cp, JFrame frame) {
        this.contentPanel = cp;
        this.frameRaiz    = frame;
    }

    public void registrarBotao(String chave, MenuButton btn) {
        mapaNavegacao.put(chave, btn);
    }

    public void setGeradorAtivo(boolean ativo) {
        this.geradorAtivo = ativo;
    }

    // ── Navegação Central ─────────────────────────────────────────────────────

    /**
     * Navega para a tela identificada pela chave do ContentPanel.
     * Aplica cursor wait, troca o card, atualiza o highlight da sidebar.
     */
    public void navegarPara(String chave) {
        if (contentPanel == null) return;
        if (chave.equals(telaAtual)) return; // já está nessa tela

        aplicarCursorWait();

        // Pequeno delay para telas "pesadas" (Gerador, Grade) darem feedback visual
        boolean telaPesada = chave.equals(ContentPanel.GERAR_GRADE)
                || chave.equals(ContentPanel.VER_GRADE)
                || chave.equals(ContentPanel.DASHBOARD);

        if (telaPesada) {
            Timer t = new Timer(120, e -> {
                trocarTela(chave);
                restaurarCursor();
            });
            t.setRepeats(false);
            t.start();
        } else {
            trocarTela(chave);
            restaurarCursor();
        }
    }

    /** Atalho direto para o Dashboard (Home). */
    public void irParaDashboard() {
        navegarPara(ContentPanel.DASHBOARD);
    }

    // ── Internos ──────────────────────────────────────────────────────────────

    private void trocarTela(String chave) {
        telaAtual = chave;
        contentPanel.mostrar(chave);
        atualizarHighlight(chave);
    }

    private void atualizarHighlight(String chaveAtiva) {
        mapaNavegacao.forEach((chave, btn) ->
                btn.setAtivo(chave.equals(chaveAtiva))
        );
    }

    private void aplicarCursorWait() {
        if (frameRaiz != null) {
            frameRaiz.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }

    private void restaurarCursor() {
        if (frameRaiz != null) {
            frameRaiz.setCursor(Cursor.getDefaultCursor());
        }
    }

    // ── Controle de fechamento ────────────────────────────────────────────────

    /**
     * Verifica processos pendentes e exibe confirmação dark antes de fechar.
     * Retorna true se pode fechar, false se o usuário cancelou.
     */
    public boolean confirmarFechamento() {
        if (!geradorAtivo) return true;

        // Customiza o JOptionPane com o tema dark
        UIManager.put("OptionPane.background",        AppTheme.BG_CARD);
        UIManager.put("Panel.background",             AppTheme.BG_CARD);
        UIManager.put("OptionPane.messageForeground", AppTheme.TEXT_PRIMARY);
        UIManager.put("Button.background",            AppTheme.PURPLE);
        UIManager.put("Button.foreground",            java.awt.Color.WHITE);

        int resp = JOptionPane.showConfirmDialog(
                frameRaiz,
                "⚠️  Há uma geração de grade em andamento.\n\nDeseja realmente sair e cancelar o processo?",
                "Confirmar Saída — SGDG",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return resp == JOptionPane.YES_OPTION;
    }

    public String getTelaAtual() { return telaAtual; }
}