package ES_2Sem_2021_Grupo_13.code_smell_detection;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.xssf.usermodel.*;
import org.graalvm.polyglot.PolyglotException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

public class GI {

	private File javaFile;
	private File excelFile;
	private String regra;
	private boolean predefinido = false;
	private JFrame frame;
	private final String METRICS_INFO = "Card with metrics";
	private final String RULE_CONFIG_INFO = "Card with rules";
	private JTable jt;
	private JScrollPane scrollPane;
	private JPanel metrics_card;
	private JPanel rules_card;
	private JPanel cards;
	private projectParser selectedProjectParser;

	public GI() {
		metrics_card = new JPanel();
		rules_card = new JPanel();
		setGuiRuleCard(rules_card);
		scrollPane = new JScrollPane();
		metrics_card.add(scrollPane);
		cards = new JPanel(new CardLayout());
		cards.add(metrics_card, METRICS_INFO);
		cards.add(rules_card, RULE_CONFIG_INFO);
		metrics_card.add(new Label("hello world"));
		addFrameContent();

	}

	private void setGuiRuleCard(JPanel rules_card2) {
		
		rules_card2.setBorder(new EmptyBorder(10, 10, 10, 10));
		rules_card2.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();
		GridBagConstraints c4 = new GridBagConstraints();
		GridBagConstraints c5 = new GridBagConstraints();
		// natural height, maximum width
		c.fill = GridBagConstraints.BOTH;
		c.gridheight=2;
		c2.fill = GridBagConstraints.BOTH;
		c3.fill = GridBagConstraints.BOTH;
		c4.fill = GridBagConstraints.BOTH;
		c5.fill = GridBagConstraints.BOTH;
		StringBuilder buff = new StringBuilder(); 
        buff.append("<html>");
        buff.append(String.format("<p style=\"text-align: justify;text-justify: inter-word;\">Para definir as regras podem ser usadas as seguintes  variáveis de input:</p>"));
        buff.append(String.format("<ul><li>tea</li><li>milk</li><li>cookie</li></ul>"));
        buff.append("</html>");
		JLabel label = new JLabel(buff.toString());
		
		label.setBorder(new EmptyBorder(0,0,0,20));
		label.setVerticalAlignment(JLabel.TOP);
		label.setPreferredSize(new Dimension(20,20));
		JLabel label2 = new JLabel("Code smell rules definitions");
		label.setVerticalAlignment(JLabel.TOP);
		label2.setVerticalAlignment(JLabel.TOP);
		label2.setPreferredSize(new Dimension(20,20));
		
		JPanel test=new JPanel();
		test.add(new JButton("validar sintaxe"));
		test.add(new JButton("gravar regras"));
		test.setPreferredSize(new Dimension(10, 10));
		JLabel test2=new JLabel();
		c.gridx = 0;
		c.gridy = 0;

		c.weightx = 0.3;
		c.weighty = 1;
		
		
		rules_card2.add(label, c);

		c2.weightx = 0.7;
		c2.weighty = 0.0;
		c2.gridx = 1;
		c2.gridy = 0;
		rules_card2.add(label2,c2);
		
		c3.weightx = 0.7;
		c3.weighty = 0.3;
		TextArea txt_area = new TextArea();
		txt_area.setFont(new Font("Serif",Font.PLAIN,20));
		txt_area.setPreferredSize(new Dimension(10,10));
		c3.gridx = 1;
		c3.gridy = 1;
		
		rules_card2.add(txt_area, c3);
		c4.fill=GridBagConstraints.BOTH;
		c4.weightx = 0.7;
		c4.weighty = 0.6;
		c4.gridx = 1;
		c4.gridy = 3;
		c4.gridheight=GridBagConstraints.REMAINDER;
		rules_card2.add(test,c4);
		
		JLabel rulesInfo=new JLabel();
		 Border blackline = BorderFactory.createLineBorder(Color.black);
		 rulesInfo.setBorder(blackline);
		GridBagConstraints c6 = new GridBagConstraints();
		c6.fill=GridBagConstraints.BOTH;
		c6.weightx = 0.7;
		c6.weighty = 0.2;
		c6.gridx = 1;
		c6.gridy = 2;
		rules_card2.add(rulesInfo,c6);
		
	}

	private void addFrameContent() {

		final File defaultFile = new File(System.getProperty("user.home"));

		// Creating the Frame
		frame = new JFrame("Detecao de Code smell");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("Ficheiro");
		JMenu m2 = new JMenu("Metricas");

		mb.add(m1);
		mb.add(m2);

		JPanel panel = new JPanel();
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.getContentPane().add(BorderLayout.CENTER, cards);
		
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, METRICS_INFO);

		JMenuItem m11 = new JMenuItem("Escolher diretoria de projecto");

		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				JFileChooser fileChooser = new JFileChooser(defaultFile);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnValue = fileChooser.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) {
					javaFile = fileChooser.getSelectedFile();
					Path path = Paths.get(javaFile.getAbsolutePath());
					selectedProjectParser=new projectParser(path);
					selectedProjectParser.parseJavaFiles();
					selectedProjectParser.writeParsedFilesToExcel();
					
					String[][] rows = selectedProjectParser.getParsedFilesTabularData();
					//separação entre a linha com os nomes das colunas
					//e as linhas com os dados 
					
					String[] column = Arrays.copyOf(rows[0], rows[0].length-1); 
					String[][]rowsForTable=new String[rows.length][rows[0].length];
					
					
					for (int i=1;i<rows.length;i++) {
						rowsForTable[i-1]=Arrays.copyOf(rows[i], rows[i].length-1);
					}
					if (scrollPane != null && jt != null) {
						scrollPane.remove(jt);
						metrics_card.remove(scrollPane);
					}

					jt = new JTable(rowsForTable, column) {
						public boolean editCellAt(int row, int column, java.util.EventObject e) {
							return false;
						}
					};
					
					
					scrollPane = new JScrollPane(jt);
					metrics_card.add(scrollPane);
					jt.setFillsViewportHeight(true);
					metrics_card.add(scrollPane);
					metrics_card.revalidate();
					metrics_card.repaint();
					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, METRICS_INFO);

					dealWithJavaFile();
				} 
				else {
					JOptionPane.showMessageDialog(frame, "Não foi encontrado a pasta!");
				}
			}
		});

		JMenuItem m12 = new JMenuItem("Importar excel"); // TODO!!!!!!!!!! TABELA

		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {
					JFileChooser fileChooser = new JFileChooser(defaultFile);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int returnValue = fileChooser.showSaveDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION
							&& getFileExtension(fileChooser.getSelectedFile().getName()).equals("xlsx")) {

						excelFile = fileChooser.getSelectedFile();
						dealWithExcelFile();

						JOptionPane.showMessageDialog(frame,
								"Foi importado o ficheiro " + fileChooser.getSelectedFile().getCanonicalPath());
					} else {
						JOptionPane.showMessageDialog(frame, "Não foi encontrado o ficheiro. Deve ter formato xlsx");
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Erro IO na importação do ficheiro!");
					e.printStackTrace();
				}
			}
		});

		final JDialog regrasFrame = new JDialog(frame, "Metricas");
		regrasFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		regrasFrame.setSize(400, 400);

		final JTextArea area = new JTextArea("Nova regra", 40, 40);

		JButton gravarRegra = new JButton("Gravar Regra");
		gravarRegra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {
					JFileChooser fileChooser = new JFileChooser(defaultFile);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					int returnValue = fileChooser.showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {

						FileWriter writer = null;
						writer = new FileWriter(fileChooser.getSelectedFile().getCanonicalPath() + ".txt");
						area.write(writer);
						writer.close();

						regra = area.getText();
						JOptionPane.showMessageDialog(frame,
								"Regra guardada em " + fileChooser.getSelectedFile().getCanonicalPath() + ".txt");
					} else
						JOptionPane.showMessageDialog(frame, "Erro na gravação do ficheiro!");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Erro IO na gravação do ficheiro!");
					e.printStackTrace();
				}

			}
		});

		final JToggleButton regraPredefinida = new JToggleButton("Regra Predefinida: OFF");
		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {

				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					predefinido = true;
					regraPredefinida.setText("Regra Predefinida: ON");
				} else {
					predefinido = false;
					regraPredefinida.setText("Regra Predefinida: OFF");
				}
			}
		};
		regraPredefinida.addItemListener(itemListener);

		JPanel panelTextMetricas = new JPanel();
		JPanel panelBotoesMetricas = new JPanel();
		panelTextMetricas.add(area);
		panelBotoesMetricas.add(gravarRegra);
		panelBotoesMetricas.add(regraPredefinida);
		regrasFrame.getContentPane().add(BorderLayout.WEST, panelTextMetricas);
		regrasFrame.getContentPane().add(BorderLayout.SOUTH, panelBotoesMetricas);

		JMenuItem m21 = new JMenuItem("Criar regra");

		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				regrasFrame.setVisible(true);

				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RULE_CONFIG_INFO);

			}
		});

		JMenuItem m22 = new JMenuItem("Abrir regra");
		m22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				Object[] possibilities = XMLParser.getRulesName(System.getProperty("user.dir")+"/"+"code_smell_rule_definitions.xml").toArray();
				String selectedRuleName=(String)JOptionPane.showInputDialog(
	                    frame,
	                    "Escolha uma regra:\n",
	                    
	                    "Regras de code smells",
	                    JOptionPane.PLAIN_MESSAGE,
	               
	                    null, possibilities,
	                    "ham");
				
				System.out.println(selectedRuleName);
		/*
				JFileChooser fileChooser = new JFileChooser(defaultFile);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnValue = fileChooser.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION
						&& getFileExtension(fileChooser.getSelectedFile().getName()).equals("txt")) {

					try {
						FileReader reader = new FileReader(fileChooser.getSelectedFile());
						area.read(reader, fileChooser.getSelectedFile());
						Path p1 = Paths.get(fileChooser.getSelectedFile().getCanonicalPath());
						regra = Files.readString(p1, Charset.defaultCharset());
						regraPredefinida.setEnabled(true);

						regrasFrame.setVisible(true);
					} catch (IOException ioe) {
						System.err.println(ioe);
						System.exit(1);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Não foi encontrado o ficheiro. Deve ter formato txt");
				}
*/
			}
		});

		m1.add(m11);
		m1.add(m12);
		m2.add(m21);
		m2.add(m22);

		JButton send = new JButton("Detectar Code Smells");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					assert selectedProjectParser!=null:"não foi feito parsing do projeto";
					String [][]tabularDataWithCodeSmells=selectedProjectParser.getProjectCodeSmells();
					String[] column = Arrays.copyOf(tabularDataWithCodeSmells[0], tabularDataWithCodeSmells[0].length-1); 
					String[][]rowsForTable=new String[tabularDataWithCodeSmells.length][tabularDataWithCodeSmells[0].length-1];
					
					
					for (int i=1;i<tabularDataWithCodeSmells.length;i++) {
						rowsForTable[i-1]=Arrays.copyOf(tabularDataWithCodeSmells[i], tabularDataWithCodeSmells[i].length-1);
					}
					if (scrollPane != null && jt != null) {
						scrollPane.remove(jt);
						metrics_card.remove(scrollPane);
					}

					jt = new JTable(rowsForTable, column) {
						public boolean editCellAt(int row, int column, java.util.EventObject e) {
							return false;
						}
					};
					
					
					scrollPane = new JScrollPane(jt);
					metrics_card.add(scrollPane);
					metrics_card.revalidate();
					metrics_card.repaint();
					
				} catch (NumberFormatException | PolyglotException | ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*if (predefinido && excelFile != null)
					dealWithCodeSmell();
				else {
					if (!predefinido) {
						JOptionPane.showMessageDialog(frame, "Nenhuma regra esta definida.");
					} else {
						JOptionPane.showMessageDialog(frame, "Projeto java em falta!");
					}
				}
				*/
			}
		
		});

		JButton gravarExcel = new JButton("Gravar Excel");// TODO!!!!!!!!!!!!! GRAVAR FICHEIRO XLSX
		gravarExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (excelFile != null) {
						XSSFWorkbook workbook;

						String fileDictName = "";
						JFileChooser fileChooser = new JFileChooser();
						FileFilter filter = new FileNameExtensionFilter("Files", ".xlsx");
						fileChooser.addChoosableFileFilter(filter);
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setSelectedFile(new File(fileDictName));
						int userSelection = fileChooser.showSaveDialog(fileChooser);

						if (userSelection == JFileChooser.APPROVE_OPTION) {

							fileDictName = fileChooser.getSelectedFile().getAbsolutePath();
							File file = new File(fileDictName);

							if (file.exists() == false) {
								workbook = new XSSFWorkbook();
								XSSFSheet exampleSheet = workbook.createSheet("1");
								XSSFRow firstRow = exampleSheet.createRow(1);
								XSSFCell cell = firstRow.createCell(0);
								cell.setCellValue("value");

								FileOutputStream out = new FileOutputStream(file);
								workbook.write(out);

							} else {
								JOptionPane.showMessageDialog(frame, "Excel já existe!");
							}
							JOptionPane.showMessageDialog(frame,
									"Excel guardado em " + fileChooser.getSelectedFile().getCanonicalPath());
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Excel em falta!");
					}
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Erro IO na gravação do ficheiro!");
					e.printStackTrace();
				}
			}
		});

		panel.add(gravarExcel);
		panel.add(send);

		// frame.getContentPane().add(BorderLayout.SOUTH, panel);
		// frame.getContentPane().add(BorderLayout.NORTH, mb);
		// frame.getContentPane().add(BorderLayout.CENTER, panelCenter);

	}

	private void dealWithJavaFile() {
		System.out.println("Enviar para java parser e voltar para aqui para mostrar tabela atraves do excel.");

	}

	private void dealWithExcelFile() throws FileNotFoundException, IOException {

		System.out.println("Mostrar tabela através do excel");
	}

	private void dealWithCodeSmell() {
		System.out.println("Code smell e mostrar na GUI");
	}

	private String getFileExtension(String fullName) {
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	public void open() {
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		
		GI gi = new GI();
		gi.open();

	}
}