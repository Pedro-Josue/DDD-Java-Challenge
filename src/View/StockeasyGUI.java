package View;

import Controller.AlmoxarifadoController;
import Controller.UsuarioController;
import DAO.CategoriaDAO;
import DAO.MaterialDAO;
import DAO.MovimentacaoDAO;
import DAO.UsuarioDAO;
import Model.Categoria;
import Model.Material;
import Model.Movimentacao;
import Model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class StockeasyGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}

class TelaLogin extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioController usuarioController;

    public TelaLogin() {
        super("Login - Stockeasy");
        this.usuarioController = new UsuarioController();

        initializeUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Stockeasy", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(lblTitulo, constraints);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(lblEmail, constraints);

        txtEmail = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(txtEmail, constraints);

        // Senha
        JLabel lblSenha = new JLabel("Senha:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(lblSenha, constraints);

        txtSenha = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(txtSenha, constraints);

        // Botões
        JButton btnLogin = new JButton("Login");
        JButton btnSair = new JButton("Sair");
        JButton btnCriarUsuario = new JButton("Criar Nova Conta");

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotoes.add(btnLogin);
        panelBotoes.add(btnCriarUsuario);
        panelBotoes.add(btnSair);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(panelBotoes, constraints);

        // Ações dos botões
        btnLogin.addActionListener(e -> realizarLogin());
        btnSair.addActionListener(e -> System.exit(0));
        btnCriarUsuario.addActionListener(e -> criarNovoUsuario());

        // Enter pressionado no campo de senha também realiza login
        txtSenha.addActionListener(e -> realizarLogin());

        add(panel);
    }

    private void criarNovoUsuario() {
        JDialog dialog = new JDialog(this, "Criar Nova Conta", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JPasswordField txtConfirmarSenha = new JPasswordField();

        dialog.add(new JLabel("Nome completo:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Senha:"));
        dialog.add(txtSenha);
        dialog.add(new JLabel("Confirmar senha:"));
        dialog.add(txtConfirmarSenha);

        JButton btnCriar = new JButton("Criar Conta");
        JButton btnCancelar = new JButton("Cancelar");

        btnCriar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String senha = new String(txtSenha.getPassword());
            String confirmarSenha = new String(txtConfirmarSenha.getPassword());

            // Validações
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(dialog, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (senha.length() < 4) {
                JOptionPane.showMessageDialog(dialog, "A senha deve ter pelo menos 4 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(dialog, "Email inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar se email já existe
            if (usuarioController.buscarUsuarioPorEmail(email) != null) {
                JOptionPane.showMessageDialog(dialog, "Este email já está em uso!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar novo usuário
            Usuario novoUsuario = new Usuario(0, nome, email, senha);
            String resultado = usuarioController.criarUsuario(novoUsuario);

            if (resultado.equals("Usuário criado com sucesso!")) {
                JOptionPane.showMessageDialog(dialog, "Conta criada com sucesso!\nAgora faça login com suas credenciais.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        JPanel panelBotoesCriar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotoesCriar.add(btnCriar);
        panelBotoesCriar.add(btnCancelar);

        dialog.add(new JLabel("")); // Espaço vazio
        dialog.add(panelBotoesCriar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void realizarLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar se é o primeiro acesso (sem usuários no sistema)
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        if (usuarios.isEmpty()) {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Nenhum usuário cadastrado. Deseja criar um usuário administrador?",
                    "Primeiro Acesso", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // Criar usuário admin padrão
                Usuario admin = new Usuario(0, "Administrador", "admin@admin.com", "admin");
                usuarioController.criarUsuario(admin);
                JOptionPane.showMessageDialog(this,
                        "Usuário administrador criado:\nEmail: admin@admin.com\nSenha: admin",
                        "Usuário Criado", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        if (usuarioController.validarLogin(email, senha)) {
            Usuario usuarioLogado = usuarioController.buscarUsuarioPorEmail(email);
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!\nBem-vindo, " + usuarioLogado.getNome(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TelaPrincipal(usuarioLogado).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
        }
    }
}

class TelaPrincipal extends JFrame {
    private Usuario usuarioLogado;

    // Controladores
    private AlmoxarifadoController almoxarifadoController = new AlmoxarifadoController();
    private UsuarioController usuarioController = new UsuarioController();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private MaterialDAO materialDAO = new MaterialDAO();
    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Componentes da interface
    private JTabbedPane tabbedPane;
    private JTable tabelaMateriais;
    private JTable tabelaMovimentacoes;
    private JTable tabelaCategorias;
    private JTable tabelaUsuarios;
    private JLabel lblUsuario;

    public TelaPrincipal(Usuario usuario) {
        super("Stockeasy    ");
        this.usuarioLogado = usuario;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        criarMenuPrincipal();
        inicializarComponentes();

        setVisible(true);
    }

    private void criarMenuPrincipal() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair do sistema?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuArquivo.add(itemSair);

        JMenu menuRelatorios = new JMenu("Relatórios");
        JMenuItem itemEstoqueBaixo = new JMenuItem("Estoque Baixo");
        itemEstoqueBaixo.addActionListener(e -> mostrarEstoqueBaixo());
        menuRelatorios.add(itemEstoqueBaixo);

        JMenuItem itemTotalEstoque = new JMenuItem("Total em Estoque");
        itemTotalEstoque.addActionListener(e -> mostrarTotalEstoque());
        menuRelatorios.add(itemTotalEstoque);

        JMenu menuUsuario = new JMenu("Usuário");
        JMenuItem itemTrocarUsuario = new JMenuItem("Trocar Usuário");
        itemTrocarUsuario.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja fazer logout e trocar de usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new TelaLogin().setVisible(true);
            }
        });
        menuUsuario.add(itemTrocarUsuario);

        menuBar.add(menuArquivo);
        menuBar.add(menuRelatorios);
        menuBar.add(menuUsuario);

        // Label com informações do usuário logado
        lblUsuario = new JLabel("Usuário: " + usuarioLogado.getNome());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(lblUsuario);

        setJMenuBar(menuBar);
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();

        // Aba de Materiais
        JPanel panelMateriais = criarPanelMateriais();
        tabbedPane.addTab("Materiais", panelMateriais);

        // Aba de Movimentações
        JPanel panelMovimentacoes = criarPanelMovimentacoes();
        tabbedPane.addTab("Movimentações", panelMovimentacoes);

        // Aba de Categorias
        JPanel panelCategorias = criarPanelCategorias();
        tabbedPane.addTab("Categorias", panelCategorias);

        // Aba de Usuários (apenas para administradores)
        if (usuarioLogado.getEmail().equals("admin@admin.com")) { // Exemplo de verificação de admin
            JPanel panelUsuarios = criarPanelUsuarios();
            tabbedPane.addTab("Usuários", panelUsuarios);
        }

        add(tabbedPane);

        // Carregar dados iniciais
        carregarDados();
    }

    private JPanel criarPanelMateriais() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        btnAdicionar.addActionListener(e -> adicionarMaterial());
        btnEditar.addActionListener(e -> editarMaterial());
        btnExcluir.addActionListener(e -> excluirMaterial());
        btnAtualizar.addActionListener(e -> carregarMateriais());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);

        // Tabela de materiais
        String[] colunas = {"ID", "Nome", "Descrição", "Quantidade", "Unidade", "Categoria"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna a tabela não editável
            }
        };
        tabelaMateriais = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabelaMateriais);

        panel.add(panelBotoes, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPanelMovimentacoes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEntrada = new JButton("Entrada");
        JButton btnSaida = new JButton("Saída");
        JButton btnAtualizar = new JButton("Atualizar");

        btnEntrada.addActionListener(e -> registrarMovimentacao("entrada"));
        btnSaida.addActionListener(e -> registrarMovimentacao("saída"));
        btnAtualizar.addActionListener(e -> carregarMovimentacoes());

        panelBotoes.add(btnEntrada);
        panelBotoes.add(btnSaida);
        panelBotoes.add(btnAtualizar);

        // Tabela de movimentações
        String[] colunas = {"ID", "Material", "Tipo", "Quantidade", "Data", "Usuário"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaMovimentacoes = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabelaMovimentacoes);

        panel.add(panelBotoes, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPanelCategorias() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        btnAdicionar.addActionListener(e -> adicionarCategoria());
        btnEditar.addActionListener(e -> editarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnAtualizar.addActionListener(e -> carregarCategorias());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);

        // Tabela de categorias
        String[] colunas = {"ID", "Nome"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);

        panel.add(panelBotoes, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnAtualizar.addActionListener(e -> carregarUsuarios());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);

        // Tabela de usuários
        String[] colunas = {"ID", "Nome", "Email"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaUsuarios = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);

        panel.add(panelBotoes, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void carregarDados() {
        carregarMateriais();
        carregarMovimentacoes();
        carregarCategorias();
        if (usuarioLogado.getEmail().equals("admin@admin.com")) {
            carregarUsuarios();
        }
    }

    private void carregarMateriais() {
        DefaultTableModel model = (DefaultTableModel) tabelaMateriais.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Material> materiais = materialDAO.listarTodos();
        for (Material m : materiais) {
            Categoria categoria = categoriaDAO.buscarPorId(m.getCategoria().getId());
            model.addRow(new Object[]{
                    m.getId(),
                    m.getNome(),
                    m.getDescricao(),
                    m.getQuantidadeEstoque(),
                    m.getUnidadeMedida(),
                    categoria != null ? categoria.getNome() : "N/A"
            });
        }
    }

    private void carregarMovimentacoes() {
        DefaultTableModel model = (DefaultTableModel) tabelaMovimentacoes.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Movimentacao> movimentacoes = movimentacaoDAO.listarTodos();
        for (Movimentacao m : movimentacoes) {
            Material material = materialDAO.buscarPorId(m.getMaterial().getId());
            Usuario usuario = usuarioDAO.buscarPorId(m.getUsuario().getId());

            model.addRow(new Object[]{
                    m.getId(),
                    material != null ? material.getNome() : "N/A",
                    m.getTipo(),
                    m.getQuantidade(),
                    m.getData().toString(),
                    usuario != null ? usuario.getNome() : "N/A"
            });
        }
    }

    private void carregarCategorias() {
        DefaultTableModel model = (DefaultTableModel) tabelaCategorias.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria c : categorias) {
            model.addRow(new Object[]{c.getId(), c.getNome()});
        }
    }

    private void carregarUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tabelaUsuarios.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            model.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail()});
        }
    }

    private void adicionarMaterial() {
        JDialog dialog = new JDialog(this, "Adicionar Material", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNome = new JTextField();
        JTextField txtDescricao = new JTextField();
        JSpinner spnQuantidade = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        JTextField txtUnidade = new JTextField();

        // Combobox para categorias
        JComboBox<String> cbCategoria = new JComboBox<>();
        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria c : categorias) {
            cbCategoria.addItem(c.getNome());
        }

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Descrição:"));
        dialog.add(txtDescricao);
        dialog.add(new JLabel("Quantidade:"));
        dialog.add(spnQuantidade);
        dialog.add(new JLabel("Unidade:"));
        dialog.add(txtUnidade);
        dialog.add(new JLabel("Categoria:"));
        dialog.add(cbCategoria);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome do material é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Encontrar a categoria selecionada
            Categoria categoriaSelecionada = null;
            for (Categoria c : categorias) {
                if (c.getNome().equals(cbCategoria.getSelectedItem())) {
                    categoriaSelecionada = c;
                    break;
                }
            }

            if (categoriaSelecionada == null) {
                JOptionPane.showMessageDialog(dialog, "Selecione uma categoria válida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Material material = new Material(
                    0,
                    txtNome.getText(),
                    txtDescricao.getText(),
                    (Integer) spnQuantidade.getValue(),
                    txtUnidade.getText(),
                    categoriaSelecionada
            );

            materialDAO.salvar(material);
            carregarMateriais();
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editarMaterial() {
        int selectedRow = tabelaMateriais.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um material para editar.");
            return;
        }

        int id = (Integer) tabelaMateriais.getValueAt(selectedRow, 0);
        Material material = materialDAO.buscarPorId(id);

        if (material == null) {
            JOptionPane.showMessageDialog(this, "Material não encontrado.");
            return;
        }

        JDialog dialog = new JDialog(this, "Editar Material", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNome = new JTextField(material.getNome());
        JTextField txtDescricao = new JTextField(material.getDescricao());
        JSpinner spnQuantidade = new JSpinner(new SpinnerNumberModel(material.getQuantidadeEstoque(), 0, 10000, 1));
        JTextField txtUnidade = new JTextField(material.getUnidadeMedida());

        // Combobox para categorias
        JComboBox<String> cbCategoria = new JComboBox<>();
        List<Categoria> categorias = categoriaDAO.listarTodos();
        for (Categoria c : categorias) {
            cbCategoria.addItem(c.getNome());
            if (c.getId() == material.getCategoria().getId()) {
                cbCategoria.setSelectedItem(c.getNome());
            }
        }

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Descrição:"));
        dialog.add(txtDescricao);
        dialog.add(new JLabel("Quantidade:"));
        dialog.add(spnQuantidade);
        dialog.add(new JLabel("Unidade:"));
        dialog.add(txtUnidade);
        dialog.add(new JLabel("Categoria:"));
        dialog.add(cbCategoria);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome do material é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Encontrar a categoria selecionada
            Categoria categoriaSelecionada = null;
            for (Categoria c : categorias) {
                if (c.getNome().equals(cbCategoria.getSelectedItem())) {
                    categoriaSelecionada = c;
                    break;
                }
            }

            material.setNome(txtNome.getText());
            material.setDescricao(txtDescricao.getText());
            material.setQuantidadeEstoque((Integer) spnQuantidade.getValue());
            material.setUnidadeMedida(txtUnidade.getText());
            material.setCategoria(categoriaSelecionada);

            materialDAO.atualizar(material);
            carregarMateriais();
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void excluirMaterial() {
        int selectedRow = tabelaMateriais.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um material para excluir.");
            return;
        }

        int id = (Integer) tabelaMateriais.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este material?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            movimentacaoDAO.deletarPorMaterial(id); // apaga todas as movimentações relacionadas
            materialDAO.deletar(id); // depois apaga o material
            carregarMateriais();
        }
    }

    private void registrarMovimentacao(String tipo) {
        JDialog dialog = new JDialog(this, "Registrar " + (tipo.equals("entrada") ? "Entrada" : "Saída"), true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        // Combobox para materiais
        JComboBox<String> cbMaterial = new JComboBox<>();
        List<Material> materiais = materialDAO.listarTodos();
        for (Material m : materiais) {
            cbMaterial.addItem(m.getNome() + " (Estoque: " + m.getQuantidadeEstoque() + ")");
        }

        JSpinner spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));

        dialog.add(new JLabel("Material:"));
        dialog.add(cbMaterial);
        dialog.add(new JLabel("Quantidade:"));
        dialog.add(spnQuantidade);
        dialog.add(new JLabel("Usuário:"));
        dialog.add(new JLabel(usuarioLogado.getNome()));

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            int selectedIndex = cbMaterial.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= materiais.size()) {
                JOptionPane.showMessageDialog(dialog, "Selecione um material válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Material material = materiais.get(selectedIndex);
            int quantidade = (Integer) spnQuantidade.getValue();

            // Atualizar estoque do material
            if (tipo.equals("entrada")) {
                material.setQuantidadeEstoque(material.getQuantidadeEstoque() + quantidade);
            } else {
                if (material.getQuantidadeEstoque() < quantidade) {
                    JOptionPane.showMessageDialog(dialog, "Quantidade em estoque insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                material.setQuantidadeEstoque(material.getQuantidadeEstoque() - quantidade);
            }

            materialDAO.atualizar(material);

            // Registrar movimentação
            Movimentacao movimentacao = new Movimentacao(
                    0,
                    material,
                    tipo,
                    quantidade,
                    LocalDateTime.now(),
                    usuarioLogado
            );

            movimentacaoDAO.salvar(movimentacao);
            carregarMateriais();
            carregarMovimentacoes();
            dialog.dispose();

            JOptionPane.showMessageDialog(this,
                    tipo.equals("entrada") ? "Entrada registrada com sucesso!" : "Saída registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void adicionarCategoria() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome da categoria:");
        if (nome != null && !nome.trim().isEmpty()) {
            Categoria categoria = new Categoria(0, nome);
            categoriaDAO.salvar(categoria);
            carregarCategorias();
        }
    }

    private void editarCategoria() {
        int selectedRow = tabelaCategorias.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.");
            return;
        }

        int id = (Integer) tabelaCategorias.getValueAt(selectedRow, 0);
        String nomeAtual = (String) tabelaCategorias.getValueAt(selectedRow, 1);

        String novoNome = JOptionPane.showInputDialog(this, "Editar categoria:", nomeAtual);
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            Categoria categoria = new Categoria(id, novoNome);
            categoriaDAO.atualizar(categoria);
            carregarCategorias();
        }
    }

    private void excluirCategoria() {
        int selectedRow = tabelaCategorias.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir.");
            return;
        }

        int id = (Integer) tabelaCategorias.getValueAt(selectedRow, 0);

        // Verificar se a categoria está sendo usada por algum material
        List<Material> materiais = materialDAO.listarTodos();
        for (Material m : materiais) {
            if (m.getCategoria().getId() == id) {
                JOptionPane.showMessageDialog(this,
                        "Esta categoria não pode ser excluída pois está vinculada a um ou mais materiais!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir esta categoria?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            categoriaDAO.deletar(id);
            carregarCategorias();
        }
    }

    private void adicionarUsuario() {
        JDialog dialog = new JDialog(this, "Adicionar Usuário", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtSenha = new JPasswordField();

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Senha:"));
        dialog.add(txtSenha);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            Usuario usuario = new Usuario(
                    0,
                    txtNome.getText(),
                    txtEmail.getText(),
                    new String(txtSenha.getPassword())
            );

            String resultado = usuarioController.criarUsuario(usuario);
            if (resultado.equals("Usuário criado com sucesso!")) {
                carregarUsuarios();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, resultado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            return;
        }

        int id = (Integer) tabelaUsuarios.getValueAt(selectedRow, 0);
        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            return;
        }

        JDialog dialog = new JDialog(this, "Editar Usuário", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField txtNome = new JTextField(usuario.getNome());
        JTextField txtEmail = new JTextField(usuario.getEmail());
        JPasswordField txtSenha = new JPasswordField(usuario.getSenha());

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Senha:"));
        dialog.add(txtSenha);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            usuario.setNome(txtNome.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setSenha(new String(txtSenha.getPassword()));

            usuarioDAO.atualizar(usuario);
            carregarUsuarios();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void excluirUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
            return;
        }

        int id = (Integer) tabelaUsuarios.getValueAt(selectedRow, 0);

        // Não permitir excluir o próprio usuário logado
        if (id == usuarioLogado.getId()) {
            JOptionPane.showMessageDialog(this,
                    "Você não pode excluir seu próprio usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioDAO.deletar(id);
            carregarUsuarios();
        }
    }

    private void mostrarEstoqueBaixo() {
        String input = JOptionPane.showInputDialog(this,
                "Defina o limite para estoque baixo:", "10");

        if (input == null) return; // Usuário cancelou

        try {
            int limite = Integer.parseInt(input);
            List<Material> estoqueBaixo = almoxarifadoController.listarEstoqueBaixo(limite);

            if (estoqueBaixo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum material com estoque baixo.", "Estoque Baixo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder mensagem = new StringBuilder("Materiais com estoque baixo (limite: " + limite + "):\n\n");
            for (Material m : estoqueBaixo) {
                mensagem.append("- ").append(m.getNome()).append(": ").append(m.getQuantidadeEstoque()).append("\n");
            }

            JOptionPane.showMessageDialog(this, mensagem.toString(), "Estoque Baixo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTotalEstoque() {
        int total = almoxarifadoController.totalEstoque();
        JOptionPane.showMessageDialog(this,
                "Total de itens em estoque: " + total, "Total em Estoque", JOptionPane.INFORMATION_MESSAGE);
    }
}

// As classes DAO, Model e Controller permanecem as mesmas que você forneceu
// ... (insira aqui as classes DAO, Model e Controller que você forneceu)