package br.com.ebac.cadastroclienteswing;

import java.awt.EventQueue;
import br.com.ebac.cadastroclienteswing.dao.*;
import br.com.ebac.cadastroclienteswing.domain.Cliente;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaPrincipal extends JFrame {

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtCpf;
	private JTable tableClientes;
	private DefaultTableModel modelo = new DefaultTableModel();
	private final IClienteDAO clienteDAO = new ClienteMapDAO();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 553, 531);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu Opções = new JMenu("Opções");
		menuBar.add(Opções);
		
		JMenuItem menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Deseja sair da aplicação?", 
						"Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		Opções.add(menuItemSair);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtNome = new JTextField();
		txtNome.setBounds(48, 17, 169, 19);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 17, 35, 21);
		lblNome.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblNome);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(10, 121, 85, 21);
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
				String cpf = txtCpf.getText();
				
				if(!isCamposValidos(nome,cpf)) {
					
					JOptionPane.showMessageDialog(null, "Existem campos a serem preenchidos" , 
							"ATENÇÃO", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				Cliente cliente = new Cliente(nome, cpf, cpf, null, cpf, null, null);
				Boolean isCadastrado = clienteDAO.cadastrar(cliente);
				
				if(isCadastrado) {
					modelo.addRow(new Object[]{cliente.getNome(), cliente.getCpf()});
					limparCampos();
				}else {
					JOptionPane.showMessageDialog(null, "cliente ja se encontra cadastrado");
				}
				
			}

			private void limparCampos() {
				txtNome.setText("");
				txtCpf.setText("");
				
			}

			private boolean isCamposValidos(String ...campos) {
				for(String campo: campos) {
					if(campos == null || "".equals(campo)) {
						return false;
					}
				}
				return true;
			}
		});
		contentPane.add(btnSalvar);
		
		JLabel lblCpf = new JLabel("CPF");
		lblCpf.setBounds(232, 20, 45, 13);
		contentPane.add(lblCpf);
		
		txtCpf = new JTextField();
		txtCpf.setBounds(267, 17, 185, 19);
		contentPane.add(txtCpf);
		txtCpf.setColumns(10);
		
		tableClientes = new JTable();
		tableClientes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int linhaSelecionada = tableClientes.getSelectedRow();
				
				Long cpf = (Long) tableClientes.getValueAt(linhaSelecionada, 1);
				
				Cliente cliente = clienteDAO.consultar(cpf);
				
				txtNome.setText(cliente.getNome());
				txtCpf.setText(cliente.getCpf().toString());
			}
		});
		tableClientes.setBounds(10, 152, 519, 320);
		contentPane.add(tableClientes);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableClientes.getSelectedRow();
				
				if(linhaSelecionada >= 0) {
					int result = JOptionPane.showConfirmDialog(null, 
							"Deseja realmente excluir esse cliente ?", 
							"CUIDADO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
							, null);
					if(result == JOptionPane.YES_OPTION) {
						Long cpf = (Long) tableClientes.getValueAt(linhaSelecionada, 1);
						clienteDAO.excluir(cpf);
						modelo.removeRow(linhaSelecionada);
						
						JOptionPane.showMessageDialog(null, "Cliente excluido com sucesso");
						limparCampos();
					}
				}else {
					JOptionPane.showMessageDialog(null, "Nenhum cliente selecionado");
				}
			}

			private void limparCampos() {
				txtNome.setText("");
				txtCpf.setText("");
			}
		});
		btnExcluir.setBounds(105, 121, 85, 21);
		contentPane.add(btnExcluir);
		initCustomComponents();
	}

	private void initCustomComponents() {
		modelo.addColumn("Nome");
		modelo.addColumn("CPF");
		
		tableClientes.setModel(modelo);
		
	}
}
