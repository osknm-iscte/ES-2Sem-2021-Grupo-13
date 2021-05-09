package ES_2Sem_2021_Grupo_13.code_smell_detection;

//new
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import  org.graalvm.polyglot.PolyglotException;

import org.apache.poi.xssf.usermodel.*;
import org.graalvm.polyglot.PolyglotException;
import org.xml.sax.SAXException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;

public class GI {

	private File javaFile;
	private File excelFile;
	private JFrame frame;
	private final String METRICS_INFO = "Card with metrics";
	private final String RULE_CONFIG_INFO = "Card with rules";
	private JTable jt;
	private JScrollPane scrollPane;
	private JPanel metrics_card;
	private JPanel rules_card;
	private JPanel cards;
	private projectParser selectedProjectParser;
	private HashMap<String, Component> JComponentMap = new HashMap<String, Component>();
	private String script;
	private String[][] rows;
	private JDialog confusionMatrixGodClass = new JDialog(frame, "Matriz de confusao: is_god_class");
	private JDialog confusionMatrixLongMethod = new JDialog(frame, "Matriz de confusao: long_Method");
	private JPanel mainLongMethod;
	private JPanel mainGodClass;
	private Box[] box = new Box[4];
	private Box[] box2 = new Box[4];

	private static class MyDocumentListener implements DocumentListener {

		JButton saveButton;

		MyDocumentListener(JButton saveButton) {
			this.saveButton = saveButton;
		}

		public void insertUpdate(DocumentEvent e) {
			saveButton.setEnabled(true);
		}

		public void removeUpdate(DocumentEvent e) {
			saveButton.setEnabled(true);
		}

		public void changedUpdate(DocumentEvent e) {

		}
	}

	public static HashMap<String, String> getProjectStatsFromExcel(String[][] rowsForTable) {
		Set<String> packageSet = new HashSet<String>();
		Set<String> classSet = new HashSet<String>();
		HashMap<String, String> parsedStats = new HashMap<String, String>();
		int totalLOCcounter = 0;
		int methodCounter = 0;
		for (int i = 0; i < rowsForTable.length; i++) {

			if (rowsForTable[i][1] != null) {
				methodCounter++;
				if (!packageSet.contains(rowsForTable[i][1])) {
					packageSet.add(rowsForTable[i][1]);
					classSet.add(rowsForTable[i][1] + "." + rowsForTable[i][2]);
					totalLOCcounter += Integer.parseInt(rowsForTable[i][5]);
				} else if (!classSet.contains(rowsForTable[i][1] + "." + rowsForTable[i][2])) {
					classSet.add(rowsForTable[i][1] + "." + rowsForTable[i][2]);
					totalLOCcounter += Integer.parseInt(rowsForTable[i][5]);
				}

			} else
				break;
		}
		parsedStats.put("packages", String.valueOf(packageSet.size()));
		parsedStats.put("classes", String.valueOf(classSet.size()));
		parsedStats.put("totalLOC", String.valueOf(totalLOCcounter));
		parsedStats.put("totalMethods", String.valueOf(methodCounter));
		return parsedStats;

	}

	public GI() {

		metrics_card = new JPanel();
		scrollPane = new JScrollPane();

		metrics_card.setLayout(new BoxLayout(metrics_card, BoxLayout.Y_AXIS));
		// metrics_card.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel projectStatistics = new JLabel("hello there");
		metrics_card.add(projectStatistics);
		JComponentMap.put("projectStatistics", projectStatistics);

		JLabel currentRule = new JLabel("regra de detecção de code smells usada: default");
		JComponentMap.put("currentRule", currentRule);
		metrics_card.add(currentRule);
		JButton detectCodeSmells = new JButton("Detectar Code Smells");
		detectCodeSmells.setVisible(false);
		detectCodeSmells.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {
					detectCodeSmells();

				} catch (NumberFormatException | PolyglotException | ScriptException e) {
					e.printStackTrace();
				}

			}

		});
		JComponentMap.put("detectCodeSmells", detectCodeSmells);
		metrics_card.add(detectCodeSmells);
		rules_card = new JPanel();
		setGuiRuleCard(rules_card);

		// metrics_card.add(scrollPane);
		cards = new JPanel(new CardLayout());
		cards.add(metrics_card, METRICS_INFO);
		cards.add(rules_card, RULE_CONFIG_INFO);
		addFrameContent();
		// HashMap<String,String>
		// rules=XMLParser.getRulesName(System.getProperty("user.dir") + "/" +
		// "code_smell_rule_definitions.xml");
		// script=rules.get("default");
		script = codeSmellRuleInterpreter.getDefaultRule();

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
		c.gridheight = 2;
		c2.fill = GridBagConstraints.BOTH;
		c3.fill = GridBagConstraints.BOTH;
		c4.fill = GridBagConstraints.BOTH;
		c5.fill = GridBagConstraints.BOTH;
		StringBuilder buff = new StringBuilder();
		buff.append("<html>");
		
		buff.append(String.format(
				"<p style=\"text-align: justify;text-justify: inter-word;\">Para definir as regras podem ser usadas as seguintes  variáveis de input:</p>"));
		 
		buff.append(String.format("<ul><li>LOC_class</li><li>WMC_class</li><li>LOC_method</li><li>CYCLO_method</li><li>NOM_class</li></ul>"));
		
		buff.append(String.format(
				"<p style=\"text-align: justify;text-justify: inter-word;\"> variáveis de code smells de output:</p>"));
		buff.append(String.format("<ul><li>god_class</li><li>long_method</li></ul>"));
		
		buff.append("</html>");
		JLabel label = new JLabel(buff.toString());

		label.setBorder(new EmptyBorder(0, 0, 0, 20));
		label.setVerticalAlignment(JLabel.TOP);
		label.setPreferredSize(new Dimension(20, 20));
		JLabel label2 = new JLabel("Code smell rules definitions");
		JComponentMap.put("ruleName", label2);

		label.setVerticalAlignment(JLabel.TOP);
		label2.setVerticalAlignment(JLabel.TOP);
		label2.setPreferredSize(new Dimension(20, 20));

		JPanel test = new JPanel();
		JButton validateSyntax = new JButton("validar sintaxe");

		validateSyntax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				JTextArea txtArea = (JTextArea) JComponentMap.get("txt_area");
				if (txtArea.getText() == "" || txtArea.getText() == null) {
					((JLabel) JComponentMap.get("rulesInfo")).setText("não foram definidas regras");
					return;
				}

				if (codeSmellRuleInterpreter.checkIfRuns(txtArea.getText())) {
					((JLabel) JComponentMap.get("rulesInfo")).setText("Regras definidas com sucesso");
				} else {
					((JLabel) JComponentMap.get("rulesInfo")).setText("Regras mal definidas");
				}

			}
		});

		JButton saveRules = new JButton("gravar regras");
		saveRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				JTextArea txtArea = (JTextArea) JComponentMap.get("txt_area");
				JLabel ruleLabel = (JLabel) JComponentMap.get("ruleName");
				try {
					XMLParser.editRule(
							System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml", ruleLabel.getText(), txtArea.getText());
					((JButton) JComponentMap.get("saveRules")).setEnabled(false);
				} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
					e.printStackTrace();
				}

			}
		});
		test.add(validateSyntax);
		test.add(saveRules);
		JComponentMap.put("validateSyntax", validateSyntax);
		JComponentMap.put("saveRules", saveRules);

		test.setPreferredSize(new Dimension(10, 10));
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.3;
		c.weighty = 1;
		rules_card2.add(label, c);

		c2.weightx = 0.7;
		c2.weighty = 0.0;
		c2.gridx = 1;
		c2.gridy = 0;
		rules_card2.add(label2, c2);

		c3.weightx = 0.7;
		c3.weighty = 0.3;
		JTextArea txt_area = new JTextArea();

		JComponentMap.put("txt_area", txt_area);
		txt_area.setFont(new Font("Serif", Font.PLAIN, 20));
		txt_area.setPreferredSize(new Dimension(10, 10));
		c3.gridx = 1;
		c3.gridy = 1;

		rules_card2.add(txt_area, c3);
		c4.fill = GridBagConstraints.BOTH;
		c4.weightx = 0.7;
		c4.weighty = 0.6;
		c4.gridx = 1;
		c4.gridy = 3;
		c4.gridheight = GridBagConstraints.REMAINDER;
		rules_card2.add(test, c4);

		JLabel rulesInfo = new JLabel();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		JComponentMap.put("rulesInfo", rulesInfo);
		rulesInfo.setBorder(blackline);
		GridBagConstraints c6 = new GridBagConstraints();
		c6.fill = GridBagConstraints.BOTH;
		c6.weightx = 0.7;
		c6.weighty = 0.2;
		c6.gridx = 1;
		c6.gridy = 2;
		rules_card2.add(rulesInfo, c6);

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
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) {
					javaFile = fileChooser.getSelectedFile();
					Path path = Paths.get(javaFile.getAbsolutePath());
					selectedProjectParser = new projectParser(path);
					selectedProjectParser.parseJavaFiles();
					selectedProjectParser.writeParsedFilesToExcel();
					HashMap<String, String> projectStats = selectedProjectParser.getProjectData();
					StringBuilder labelStats = new StringBuilder();
					labelStats.append("<html>");
					labelStats.append(" pacotes: " + projectStats.get("packages"));
					// labelStats.append("<br>");
					labelStats.append(" classes: " + projectStats.get("classCounter") + " ");
					// labelStats.append("<br>");
					labelStats.append(" metodos: " + projectStats.get("methodCountID") + " ");
					// labelStats.append("<br>");
					labelStats
							.append(" nº total de linhas de codigo das classes: " + projectStats.get("totalLOC") + " ");
					projectStats.get("</html>");
					((JLabel) JComponentMap.get("projectStatistics")).setText(labelStats.toString());

					rows = selectedProjectParser.getParsedFilesTabularData();
					// separação entre a linha com os nomes das colunas
					// e as linhas com os dados

					tableFabric();
//					STRING[] COLUMN = ARRAYS.COPYOF(ROWS[0], ROWS[0].LENGTH - 1);
//					STRING[][] ROWSFORTABLE = NEW STRING[ROWS.LENGTH][ROWS[0].LENGTH];
//
//					FOR (INT I = 1; I < ROWS.LENGTH; I++) {
//						ROWSFORTABLE[I - 1] = ARRAYS.COPYOF(ROWS[I], ROWS[I].LENGTH - 1);
//					}
//					IF (SCROLLPANE != NULL && JT != NULL) {
//						SCROLLPANE.REMOVE(JT);
//						METRICS_CARD.REMOVE(SCROLLPANE);
//					}
//
//					JT = NEW JTABLE(ROWSFORTABLE, COLUMN) {
//						PUBLIC BOOLEAN EDITCELLAT(INT ROW, INT COLUMN, JAVA.UTIL.EVENTOBJECT E) {
//							RETURN FALSE;
//						}
//					};
//					JT.SETFILLSVIEWPORTHEIGHT(TRUE);
//
//					SCROLLPANE = NEW JSCROLLPANE(JT);
//					METRICS_CARD.ADD(SCROLLPANE);
//					JT.SETFILLSVIEWPORTHEIGHT(TRUE);
//					METRICS_CARD.ADD(SCROLLPANE);
//					METRICS_CARD.REVALIDATE();
//					METRICS_CARD.REPAINT();
					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, METRICS_INFO);
					((JButton) JComponentMap.get("detectCodeSmells")).setVisible(true);

				} else {
					JOptionPane.showMessageDialog(frame, "Não foi encontrado a pasta!");
				}
			}
		});

		JMenuItem m12 = new JMenuItem("Importar excel"); // TODO

		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				try {

					JFileChooser fileChooser = new JFileChooser(defaultFile);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int returnValue = fileChooser.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION
							&& getFileExtension(fileChooser.getSelectedFile().getName()).equals("xlsx")) {

						excelFile = fileChooser.getSelectedFile();

						rows = XLSX_read_write.readyExcelForGUI(excelFile.getAbsolutePath());
						// separação entre a linha com os nomes das colunas
						// e as linhas com os dados

						String[] column = Arrays.copyOf(rows[0], rows[0].length - 1);
						String[][] rowsForTable = new String[rows.length][rows[0].length];
//
						for (int i = 1; i < rows.length; i++) {
							rowsForTable[i - 1] = Arrays.copyOf(rows[i], rows[i].length - 1);
						}
//						if (scrollPane != null && jt != null) {
//							scrollPane.remove(jt);
//							metrics_card.remove(scrollPane);
//						}
						getProjectStatsFromExcel(rowsForTable);

//						jt = new JTable(rowsForTable, column) {
//							public boolean editCellAt(int row, int column, java.util.EventObject e) {
//								return false;
//							}
//						};

						HashMap<String, String> parsedProjectStats = getProjectStatsFromExcel(rowsForTable);
						StringBuilder labelStats = new StringBuilder();
						labelStats.append("<html>");
						labelStats.append(" pacotes: " + parsedProjectStats.get("packages"));
						// labelStats.append("<br>");
						labelStats.append(" classes: " + parsedProjectStats.get("classes") + " ");
						// labelStats.append("<br>");
						labelStats.append(" metodos: " + parsedProjectStats.get("totalMethods") + " ");
						// labelStats.append("<br>");
						labelStats.append(" nº total de linhas de codigo das classes: "
								+ parsedProjectStats.get("totalLOC") + " ");
						labelStats.append("</html>");

						((JLabel) JComponentMap.get("projectStatistics")).setText(labelStats.toString());

//						scrollPane = new JScrollPane(jt);
//						metrics_card.add(scrollPane);
//						jt.setFillsViewportHeight(true);
//						metrics_card.add(scrollPane);
//						metrics_card.revalidate();
//						metrics_card.repaint();
						tableFabric();
						((JButton) JComponentMap.get("detectCodeSmells")).setVisible(true);
						JOptionPane.
						showMessageDialog(frame, "Foi importado o ficheiro " + fileChooser.getSelectedFile().getCanonicalPath());
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

				JTextArea txtArea = (JTextArea) JComponentMap.get("txt_area");
				JLabel ruleLabel = (JLabel) JComponentMap.get("ruleName");
				try {

					XMLParser.editRule(
							System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml", ruleLabel.getText(), txtArea.getText());
				} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {

					JFileChooser fileChooser = new JFileChooser(defaultFile);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					int returnValue = fileChooser.showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {

						// writes rule to xml file

						String[] regraTeste = area.getText().split("\\r?\\n");
						String path;
						try {
							path = fileChooser.getSelectedFile().getCanonicalPath() + ".xml";

							XML_read_write.formatText(path, regraTeste);
						} catch (ParserConfigurationException | TransformerException | SAXException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

//						FileWriter writer = null;
//						writer = new FileWriter(fileChooser.getSelectedFile().getCanonicalPath() + ".txt");
//						area.write(writer);
//						writer.close();
//
//						regra = area.getText();

						try {
							JOptionPane.showMessageDialog(frame,
									"Regra guardada em " + fileChooser.getSelectedFile().getCanonicalPath() + ".xml");
						} catch (HeadlessException | IOException e1) {
							e1.printStackTrace();
						}
					} else
						JOptionPane.showMessageDialog(frame, "Erro na gravação do ficheiro!");
				}

			}
		});

		JPanel panelTextMetricas = new JPanel();
		JPanel panelBotoesMetricas = new JPanel();
		panelTextMetricas.add(area);
		panelBotoesMetricas.add(gravarRegra);
		regrasFrame.getContentPane().add(BorderLayout.WEST, panelTextMetricas);
		regrasFrame.getContentPane().add(BorderLayout.SOUTH, panelBotoesMetricas);

		JMenuItem m21 = new JMenuItem("Criar regra");

		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				// regrasFrame.setVisible(true);


				String selectedRuleName = (String) JOptionPane.showInputDialog(frame, "Nome  da regra:\n",

						"Regras de code smells", JOptionPane.PLAIN_MESSAGE, null, null,"");
				if (selectedRuleName==null || selectedRuleName.equals("")) {
					JOptionPane.showMessageDialog(frame,
						    "Não pode ser criada regra com nome vazio",
						    "erro na atribuição do nome",
						    JOptionPane.ERROR_MESSAGE);
						return;
				}
					
				
				try {
					if(XMLParser.ckeckIfRuleNameExists(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml", selectedRuleName)) {
						JOptionPane.showMessageDialog(frame,
							    "A regra com este nome já existe");
							return;
					}
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					JOptionPane.showMessageDialog(frame,
						    "Algo correu mal no processamento do ficheiro das regras",
						    "erro de processamento das regras",
						    JOptionPane.ERROR_MESSAGE);
						return;
				}

				System.out.println(selectedRuleName);
				try {

					XMLParser.createRule(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml",
							UUID.randomUUID().toString(), selectedRuleName, null);
					
				} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
					JOptionPane.showMessageDialog(frame,
						    "Algo correu mal no processamento do ficheiro das regras",
						    "erro de processamento das regras",
						    JOptionPane.ERROR_MESSAGE);
						return;

				}
				// regrasFrame.setVisible(true);

				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RULE_CONFIG_INFO);
				JLabel label = (JLabel) JComponentMap.get("ruleName");
				label.setText(selectedRuleName);
				JTextArea txt_area = (JTextArea) JComponentMap.get("txt_area");
				// txt_area.setText(rulesAndDefinitions.get(selectedRuleName));
				txt_area.getDocument()
						.addDocumentListener(new MyDocumentListener((JButton) JComponentMap.get("saveRules")));
				((JButton) JComponentMap.get("saveRules")).setEnabled(false);

			}
		});

		JMenuItem m22 = new JMenuItem("Editar regra");

		m22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				HashMap<String, String> rulesAndDefinitions = XMLParser
						.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml");
				Object[] possibilities = rulesAndDefinitions.keySet().toArray();
				String selectedRuleName = (String) JOptionPane.
						showInputDialog(frame, "Escolha uma regra:\n", "Regras de code smells", JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");

				if(selectedRuleName==null) return;

				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.show(cards, RULE_CONFIG_INFO);
				JLabel label = (JLabel) JComponentMap.get("ruleName");
				label.setText(selectedRuleName);
				JTextArea txt_area = (JTextArea) JComponentMap.get("txt_area");
				txt_area.setText(rulesAndDefinitions.get(selectedRuleName));
				txt_area.getDocument()
						.addDocumentListener(new MyDocumentListener((JButton) JComponentMap.get("saveRules")));
				((JButton) JComponentMap.get("saveRules")).setEnabled(false);

			}
		});

		JMenuItem selectedRuleToUse = new JMenuItem("Escolher regra a usar");
		selectedRuleToUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				HashMap<String, String> rulesAndDefinitions = XMLParser
						.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml");
				Object[] possibilities = rulesAndDefinitions.keySet().toArray();
				String selectedRuleName = (String) JOptionPane.
						showInputDialog(frame, "Escolha uma regra:\n", "Regras de code smells", JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");

				if (selectedRuleName != null) {
					script = rulesAndDefinitions.get(selectedRuleName);
					((JLabel) JComponentMap.get("currentRule")).setText("regra usada: " + selectedRuleName);
				}

			}
		});
		
		
		fillMatrixes();

		JMenuItem m23 = new JMenuItem("Comparar Code Smells");
		m23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				HashMap<String, String> rulesAndDefinitions = XMLParser.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml");
				Object[] possibilities = rulesAndDefinitions.keySet().toArray();

				
				String[] tempArray = new String[possibilities.length];
				
				for(int i=0; i<possibilities.length; i++) {
					tempArray[i] = (String) possibilities[i];
				}
				

				JComboBox<Object> possibilitiesBox = new JComboBox<Object>(tempArray);
				
				JCheckBox long_Method = new JCheckBox("Long_Method");
				JCheckBox is_God_Class = new JCheckBox("Is_God_Class");
				
				Object[] message = {"Regra: ", possibilitiesBox, long_Method, is_God_Class};
				
//				String selectedRuleName = 
//						(String) JOptionPane.showInputDialog(frame, "Escolha uma regra:\n", "Regras de code smells", JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");
				int option = JOptionPane.showConfirmDialog(null, message, "Escolha a regra e 1 ou mais Code Smells", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					
				if(long_Method.isSelected() || is_God_Class.isSelected()) {
					
					int chooser = !long_Method.isSelected() ? 7 : !is_God_Class.isSelected() ? 10 : 0;	
					JLabel[] labelArray = new JLabel[4];
					
					int finalI = (chooser>0) ? 1 : 2;
					if(chooser==0) chooser=7;
					try {		
						for(int i=0; i<finalI; i++) {		
						
							
							JLabel tPl = new JLabel(String.valueOf(codeSmellRuleInterpreter.testRuleAccuracy(XMLParser.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml")
									.get((possibilitiesBox.getSelectedItem())), chooser).get("truePositive")));
							JLabel tNl = new JLabel(String.valueOf(codeSmellRuleInterpreter.testRuleAccuracy(XMLParser.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml")
									.get((possibilitiesBox.getSelectedItem())), chooser).get("trueNegative")));
							JLabel fPl = new JLabel(String.valueOf(codeSmellRuleInterpreter.testRuleAccuracy(XMLParser.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml")
									.get((possibilitiesBox.getSelectedItem())), chooser).get("falsePositiveCounter")));
							JLabel fNl = new JLabel(String.valueOf(codeSmellRuleInterpreter.testRuleAccuracy(XMLParser.getRulesName(System.getProperty("user.dir") + "/" + "code_smell_rule_definitions.xml")
									.get((possibilitiesBox.getSelectedItem())), chooser).get("falseNegative")));
							
							labelArray[0] = tPl;
							labelArray[1] = fPl;
							labelArray[2] = fNl;
							labelArray[3] = tNl;
							
							changeBoxStats(labelArray, chooser);
							
							chooser=10;
						}
					} catch (NumberFormatException | PolyglotException | IOException | ScriptException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Algo correu mal com o processamento dos dados",
							    "erro no processamento dos dados",
							    JOptionPane.ERROR_MESSAGE);
							return;
					}
						if(long_Method.isSelected()) confusionMatrixLongMethod.setVisible(true);
						if(is_God_Class.isSelected()) confusionMatrixGodClass.setVisible(true);
					
				}
				else JOptionPane.showMessageDialog(frame, "Deve escolher 1 ou mais Code Smells!");
				

				}
			}
		});

		m1.add(m11);
		m1.add(m12);
		m2.add(m21);
		m2.add(m22);
		m2.add(m23);
		m2.add(selectedRuleToUse);


	}

	private void fillMatrixes() {
		
		confusionMatrixGodClass.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		confusionMatrixLongMethod.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel centerLongMethod, centerGodClass, left, top;

		mainGodClass = new JPanel(new BorderLayout());
		mainLongMethod = new JPanel(new BorderLayout());
		
		centerLongMethod = new JPanel();
		centerGodClass = new JPanel();
		centerLongMethod.setLayout(new GridLayout(2, 2));
		centerGodClass.setLayout(new GridLayout(2, 2));

		for (int i = 0; i < 4; i++) {
			box[i] = new Box(BoxLayout.X_AXIS);
			box[i].setBorder(BorderFactory.createLineBorder(Color.black));
			box[i].add(new JLabel("      " + (i + 1) + "      "));
			box[i].setOpaque(true);
			if (i == 1 || i == 2)
				box[i].setBackground(Color.orange);
			else
				box[i].setBackground(Color.green);
			centerLongMethod.add(box[i]);
			
			box2[i] = new Box(BoxLayout.X_AXIS);
			box2[i].setBorder(BorderFactory.createLineBorder(Color.black));
			box2[i].add(new JLabel("      " + (i + 1) + "      "));
			box2[i].setOpaque(true);
			if (i == 1 || i == 2)
				box2[i].setBackground(Color.orange);
			else
				box2[i].setBackground(Color.green);
			centerGodClass.add(box2[i]);

		}

		left = new JPanel();
		left.setLayout(new GridLayout(2, 1));
		JLabel predictedPositive = new JLabel("Predicted: Positive");
		JLabel predictedNegative = new JLabel("Predicted: Negative");
		left.add(predictedPositive);
		left.add(predictedNegative);

		top = new JPanel();
		top.setLayout(new GridLayout(1, 2));
		JLabel actualPositive = new JLabel("Predicted: Positive");
		JLabel actualNegative = new JLabel("Predicted: Negative");
		JLabel matrixLabel = new JLabel("Confusion Matrix");
		top.add(matrixLabel);
		top.add(actualPositive);
		top.add(actualNegative);

		mainLongMethod.add(centerLongMethod, BorderLayout.CENTER);
		mainLongMethod.add(left, BorderLayout.WEST);
		mainLongMethod.add(top, BorderLayout.NORTH);
		confusionMatrixLongMethod.add(mainLongMethod);
		confusionMatrixLongMethod.getContentPane();
		
		mainGodClass.add(centerGodClass, BorderLayout.CENTER);
		mainGodClass.add(left, BorderLayout.WEST);
		mainGodClass.add(top, BorderLayout.NORTH);
		confusionMatrixGodClass.add(mainGodClass);
		confusionMatrixGodClass.getContentPane();

		confusionMatrixLongMethod.setBounds(50, 50, 500, 500);
		confusionMatrixLongMethod.setResizable(false);
		confusionMatrixLongMethod.setLocationRelativeTo(null);
		
		confusionMatrixGodClass.setBounds(50, 50, 500, 500);
		confusionMatrixGodClass.setResizable(false);
		confusionMatrixGodClass.setLocationRelativeTo(null);
	}
	
	private void changeBoxStats(JLabel[] labelArray, int chooser) {
		for(int i=0; i<4; i++) {
			
			if(chooser==10) {
				box[i].removeAll();;
				box[i].add(labelArray[i]);
				mainLongMethod.revalidate();
				mainLongMethod.repaint();
			}
			else {
				box2[i].removeAll();
				box2[i].add(labelArray[i]);
				mainGodClass.revalidate();
				mainGodClass.repaint();
			}	
		}
		
	}

//	private void compararCodeSmells() {
//
//		ArrayList<Object> is_God_Class = new ArrayList<Object>();
//		for (int i = 0; i < jt.getModel().getRowCount(); i++) {
//			is_God_Class.add(jt.getModel().getValueAt(i, 7));
//		}
//		System.out.println("Teste a column " + jt.getModel().getValueAt(1, 7).toString());
//
//		ArrayList<Object> is_Long_Method = new ArrayList<Object>();
//		for (int i = 0; i < jt.getModel().getRowCount(); i++) {
//			is_Long_Method.add(jt.getModel().getValueAt(i, 10));
//		}
//		System.out.println("Teste a column " + jt.getModel().getValueAt(1, 10).toString());
//
//	}

	private void detectCodeSmells() throws NumberFormatException, PolyglotException, ScriptException {
		assert selectedProjectParser != null : "não foi feito parsing do projeto";

		String[][] dataWithCodeSmellFlags = codeSmellRuleInterpreter.getProjectCodeSmells(rows, script);
		
		tableFabric();
		
//		String[] column = Arrays.copyOf(rows[0], rows[0].length - 1);
//		String[][] rowsForTable = new String[rows.length][rows[0].length - 1];

		
//		for (int i = 1; i < rows.length; i++) {
//			rowsForTable[i - 1] = Arrays.copyOf(rows[i], rows[i].length - 1);
//		}
//		if (scrollPane != null && jt != null) {
//			scrollPane.remove(jt);
//			metrics_card.remove(scrollPane);
//		}
//
//		jt = new JTable(rowsForTable, column) {
//			public boolean editCellAt(int row, int column, java.util.EventObject e) {
//				return false;
//			}
//		};
//
//		scrollPane = new JScrollPane(jt);
//		metrics_card.add(scrollPane);
//		metrics_card.revalidate();
//		metrics_card.repaint();
	}
	
	private void tableFabric() {
		String[] column = Arrays.copyOf(rows[0], rows[0].length - 1);
		String[][] rowsForTable = new String[rows.length][rows[0].length];

		for (int i = 1; i < rows.length; i++) {
			rowsForTable[i - 1] = Arrays.copyOf(rows[i], rows[i].length - 1);
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
		jt.setFillsViewportHeight(true);

		scrollPane = new JScrollPane(jt);
		metrics_card.add(scrollPane);
		jt.setFillsViewportHeight(true);
		metrics_card.add(scrollPane);
		metrics_card.revalidate();
		metrics_card.repaint();
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