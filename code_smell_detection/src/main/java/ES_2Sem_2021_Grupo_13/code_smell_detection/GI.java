package ES_2Sem_2021_Grupo_13.code_smell_detection;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
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
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.xssf.usermodel.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;


public class GI {

	private File javaFile;
	private File excelFile;
	private File excelDir;
	private String regra;
	private boolean predefinido=false;
	private JFrame frame;
	
	public GI() {
		
		excelDir = new File(System.getProperty("user.dir"));
		addFrameContent();
		
	}
	
	private void addFrameContent() {
		
		final File defaultFile = new File(System.getProperty("user.home"));
		
		//Creating the Frame
	    frame = new JFrame("Detecao de Code smell");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(600, 600);
	
	    JMenuBar mb = new JMenuBar();
	    JMenu m1 = new JMenu("Ficheiro");
	    JMenu m2 = new JMenu("Metricas");
	    
	    mb.add(m1);
	    mb.add(m2);
	    
	    JPanel panelCenter = new JPanel();
	    JPanel panel = new JPanel();
	    frame.getContentPane().add(BorderLayout.SOUTH, panel);
	    frame.getContentPane().add(BorderLayout.NORTH, mb);
	    frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.createVerticalScrollBar();
	    JMenuItem m11 = new JMenuItem("Escolher diretoria de projecto");
	    
	    m11.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    	      
	    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
	    	      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	      fileChooser.setAcceptAllFileFilterUsed(false);
	    	      int returnValue = fileChooser.showSaveDialog(null);
	    	      if (returnValue == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) {
	    	  		javaFile = fileChooser.getSelectedFile();
	    	  		
	    				
    				 String[][] rows=App.readyFileForGUI(Paths.get(javaFile.getAbsolutePath()), excelDir.getAbsolutePath());
    				 String[] column = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
	    						"is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method" }; // 1st line
    				 DefaultTableModel dtm = new DefaultTableModel(rows,column);	 
    				 JTable jt=new JTable(dtm) {
    			         public boolean editCellAt(int row, int column, java.util.EventObject e) {
    			             return false;
    			          }
    			       };
    			    ((DefaultTableModel)jt.getModel()).removeRow(0);
    				scrollPane.setViewportView(jt);
    				jt.setFillsViewportHeight(true);
    				panelCenter.add(scrollPane);
    				
    				parametrosResumo(panelCenter);
    				
    				panelCenter.revalidate();
    				panelCenter.repaint();
	    				
	    	      }
	    	      else {
	    	    	  JOptionPane.showMessageDialog(frame, "Não foi encontrada a pasta!");
	    	      }
	    	   }
	    	});
	  
	    JMenuItem m12 = new JMenuItem("Importar excel");
	    
	    m12.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    		   
	    		   try {
 		    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
 		    	      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
 		    	      int returnValue = fileChooser.showSaveDialog(null);
 		    	     
 		    	      if (returnValue == JFileChooser.APPROVE_OPTION && App.getFileExtension(fileChooser.getSelectedFile().getName()).equals("xlsx")) {
 		    	    	  
 	    	        		excelFile = fileChooser.getSelectedFile();
 	    	        		
 	    	        		String[] column = { "MethodID", "package", "class", "method", "NOM_class", "LOC_class", "WMC_class",
 		    						"is_God_Class", "LOC_method", "CYCLO_method", "is_Long_Method" }; // 1st line
 	    	        		
 	    	        		String[][] rows=App.readyExcelForGUI(excelFile);
 	    	        		
 	    	        	 DefaultTableModel dtm = new DefaultTableModel(rows,column);	 
 	      				 JTable jt=new JTable(dtm) {
 	      			         public boolean editCellAt(int row, int column, java.util.EventObject e) {
 	      			             return false;
 	      			          }
 	      			       };
 	      			    ((DefaultTableModel)jt.getModel()).removeRow(0);
 	    	        	scrollPane.setViewportView(jt);
 	       				jt.setFillsViewportHeight(true);
 	       				panelCenter.add(scrollPane);
 	       				panelCenter.revalidate();
 	       				panelCenter.repaint();
 		    	        	
 		    	        	JOptionPane.showMessageDialog(frame, "Foi importado o ficheiro " + fileChooser.getSelectedFile().getCanonicalPath());
 		    	      }
 		    	      else {
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
	    
	    JMenuItem m13 = new JMenuItem("Directoria p/ gravar Excel");
	    m13.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    	      
	    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
	    	      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	      fileChooser.setAcceptAllFileFilterUsed(false);
	    	      int returnValue = fileChooser.showSaveDialog(null);
	    	      if (returnValue == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) {
	    	  		excelDir = fileChooser.getSelectedFile();
	    	   
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
	 	    	    		writer = new FileWriter(fileChooser.getSelectedFile().getCanonicalPath()+".txt");
	 	    	    		area.write(writer);
							writer.close();
							
							regra=area.getText();
							JOptionPane.showMessageDialog(frame, "Regra guardada em " + fileChooser.getSelectedFile().getCanonicalPath() + ".txt");
						}
	 	    	     else JOptionPane.showMessageDialog(frame, "Erro na gravação do ficheiro!");
	    		  }
 	    	      catch (IOException e) {
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
                	predefinido=true;
                	regraPredefinida.setText("Regra Predefinida: ON");  
                }
                else {
                	predefinido=false;
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
	    		  
	    	   }
	    	});
	    
	    JMenuItem m22 = new JMenuItem("Abrir regra");
	    m22.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    		   
	    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
	    	      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    	      int returnValue = fileChooser.showSaveDialog(null);
	    	     
	    	      if (returnValue == JFileChooser.APPROVE_OPTION && App.getFileExtension(fileChooser.getSelectedFile().getName()).equals("txt")) {
	    	    	  
	    	    	  try {
		    			   FileReader reader = new FileReader(fileChooser.getSelectedFile());
		    			   area.read(reader, fileChooser.getSelectedFile());
		    			   Path p1 = Paths.get(fileChooser.getSelectedFile().getCanonicalPath());
		    			   regra = Files.readString(p1, Charset.defaultCharset());
		    			   regraPredefinida.setEnabled(true);
		    			   
		    			   regrasFrame.setVisible(true);
	    	    	  }
		    			catch (IOException ioe) {
		    			    System.err.println(ioe);
		    			    System.exit(1);
		    			}
	    	      }  	
	    	      else {
	    	    	  JOptionPane.showMessageDialog(frame, "Não foi encontrado o ficheiro. Deve ter formato txt");
	    	      }
	    	    	  
	    	    	  
	    	   }
	    	});
	    
	    m1.add(m11);
	    m1.add(m12);
	    m1.add(m13);
	    m2.add(m21);
	    m2.add(m22);
	
	    
	    JButton send = new JButton("Detectar Code Smells");
	    send.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    	   
	    		   if(predefinido && excelFile!=null)
	    			   dealWithCodeSmell();
	    		   else {
	    			   if(!predefinido) {
	    				   JOptionPane.showMessageDialog(frame, "Nenhuma regra esta definida.");
	    			   }
	    			   else {
	    				   JOptionPane.showMessageDialog(frame, "Projeto java em falta!");
	    			   }
	    		   }
	    	   }
	    	});
	    
	    panel.add(send);
	
	
	
	   // frame.getContentPane().add(BorderLayout.SOUTH, panel);
	    //frame.getContentPane().add(BorderLayout.NORTH, mb);
	    //frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
	    
	    
	}
	
	private void parametrosResumo(JPanel panelCenter) {
		
		JLabel pack = new JLabel("Número total de packages: " + "X");
		JLabel classes = new JLabel("Número total de classes: " + "X");
		JLabel method = new JLabel("Número total de métodos: " + "X");
		JLabel lines = new JLabel("Número total de linhas de código: " + "X");
		
		panelCenter.add(pack);
		panelCenter.add(classes);
		panelCenter.add(method);
		panelCenter.add(lines);
		
	}
	
	
	private void dealWithCodeSmell() {
		System.out.println("Code smell e mostrar na GUI");
	}
	
//	private String getFileExtension(String fullName) {
//	    String fileName = new File(fullName).getName();
//	    int dotIndex = fileName.lastIndexOf('.');
//	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
//	}
	

	public void open() {
			frame.setVisible(true);
	}

	
public static void main(String[] args) throws IOException {
	GI gi = new GI();
	gi.open();
		
	}
}