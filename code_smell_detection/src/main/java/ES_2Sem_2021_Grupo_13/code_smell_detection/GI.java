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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xssf.usermodel.*;

public class GI {

	private File javaFile;
	private File excelFile;
	private String regra;
	private boolean predefinido=false;
	private JFrame frame;
	
	public GI() {
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
	    
	    JMenuItem m11 = new JMenuItem("Escolher diretoria de projecto");
	    
	    m11.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    	      
	    		  
	    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
	    	      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	      fileChooser.setAcceptAllFileFilterUsed(false);
	    	      int returnValue = fileChooser.showSaveDialog(null);
	    	      if (returnValue == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) {
	    	    	  		javaFile = fileChooser.getSelectedFile();
		    	        	dealWithJavaFile();
	    	      }
	    	      else {
	    	    	  JOptionPane.showMessageDialog(frame, "Não foi encontrado a pasta!");
	    	      }
	    	   }
	    	});
	  
	    JMenuItem m12 = new JMenuItem("Importar excel"); //TODO!!!!!!!!!! TABELA
	    
	    m12.addActionListener(new ActionListener() {
	    	   public void actionPerformed(ActionEvent ae) {
	    		   
	    		   try {
	 		    		  JFileChooser fileChooser = new JFileChooser(defaultFile);
	 		    	      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	 		    	      int returnValue = fileChooser.showSaveDialog(null);
	 		    	     
	 		    	      if (returnValue == JFileChooser.APPROVE_OPTION && getFileExtension(fileChooser.getSelectedFile().getName()).equals("xlsx")) {
	 		    	    	  
	 	    	        		excelFile = fileChooser.getSelectedFile();
	 		    	        	dealWithExcelFile();
	 		    	        	
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
	    	     
	    	      if (returnValue == JFileChooser.APPROVE_OPTION && getFileExtension(fileChooser.getSelectedFile().getName()).equals("txt")) {
	    	    	  
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
	    m2.add(m21);
	    m2.add(m22);
	
	    JPanel panel = new JPanel();
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
	    
	    JButton gravarExcel = new JButton("Gravar Excel");//TODO!!!!!!!!!!!!! GRAVAR FICHEIRO XLSX
	    gravarExcel.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent ae) {
    		   try {
	    		   if(excelFile!=null) {
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
	    			        JOptionPane.showMessageDialog(frame, "Excel guardado em " + fileChooser.getSelectedFile().getCanonicalPath());
	    			   }
	    		   }
	    		   else {
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
	
	    JPanel panelCenter = new JPanel();
	
	    frame.getContentPane().add(BorderLayout.SOUTH, panel);
	    frame.getContentPane().add(BorderLayout.NORTH, mb);
	    frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
	    
	    
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


