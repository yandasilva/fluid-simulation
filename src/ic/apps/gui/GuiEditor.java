package ic.apps.gui;

//Java imports
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Awt imports
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

//Swing imports
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.filechooser.FileFilter;

class MyFilter extends FileFilter {
    
    //Filters files in order to accept only those with .settings extension
    @Override
    public boolean accept(File file) {
        String filename = file.getName();
        return filename.endsWith(".settings");
    }
    @Override
    public String getDescription() {
        return "*.settings";
    }
}

public final class GuiEditor extends JFrame {

    //Text area
    private JTextArea    textComp = null;
    //Scroll pane
    private JScrollPane  scrlText = null;
    //File chooser
    private JFileChooser chooser  = new JFileChooser( "./src/ic/apps" );
    //Current file
    private String       curPath  = null;
    
    //Creates an editor
    public GuiEditor(int posX, int posY, int width, int height) {
        super("Settings editor");
        textComp = createTextComponent();
        scrlText = createScrollComponent(textComp);
        makeActionsHuman();

        Container content = getContentPane();
        content.add(scrlText, BorderLayout.CENTER);
        setJMenuBar( createMenuBar() );
        setLocation(posX + width + 5, posY);
        setSize(width, height + 20);
        
        FileFilter filt = new MyFilter();
        chooser.addChoosableFileFilter(filt);
    }

    //Creates the text component
    protected final JTextArea createTextComponent() {
        JTextArea ta = new JTextArea();
        ta.setLineWrap(true);
        return ta;
    }

   //Creates the scroll pane component
    protected final JScrollPane createScrollComponent(JTextArea ta) {
        JScrollPane sc =  new JScrollPane(ta);
        sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        return sc;
    }
       
    //Adds user-friendly names to actions we care about
    protected void makeActionsHuman() {
        
        Action a;
        a = textComp.getActionMap().get(DefaultEditorKit.cutAction);
        a.putValue(Action.NAME, "Cut");

        a = textComp.getActionMap().get(DefaultEditorKit.copyAction);
        a.putValue(Action.NAME, "Copy");

        a = textComp.getActionMap().get(DefaultEditorKit.pasteAction);
        a.putValue(Action.NAME, "Paste");

        a = textComp.getActionMap().get(DefaultEditorKit.selectAllAction);
        a.putValue(Action.NAME, "Select all");
    }

    //Create a JMenuBar with file & edit menus
    protected JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        menubar.add(file);
        menubar.add(edit);
                
        file.add(new OpenAction());
        file.getItem(0).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                                                              java.awt.Event.META_MASK));

        file.add(new SaveAction());
        file.getItem(1).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                                                              java.awt.Event.META_MASK));

        file.add(new SaveAsAction());
        file.getItem(2).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 
                                                             (java.awt.Event.SHIFT_MASK | 
                                                              java.awt.Event.META_MASK)));
        file.add(new ExitAction());
        file.getItem(3).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 
                                                              java.awt.Event.META_MASK));
        
        edit.add(textComp.getActionMap().get(DefaultEditorKit.cutAction));
        edit.getItem(0).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                                                              java.awt.Event.META_MASK));
        
        edit.add(textComp.getActionMap().get(DefaultEditorKit.copyAction));
        edit.getItem(1).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                                                              java.awt.Event.META_MASK));

        edit.add(textComp.getActionMap().get(DefaultEditorKit.pasteAction));
        edit.getItem(2).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                                                              java.awt.Event.META_MASK));

        edit.add(textComp.getActionMap().get(DefaultEditorKit.selectAllAction));
        edit.getItem(3).setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, 
                                                             (java.awt.Event.SHIFT_MASK | 
                                                              java.awt.Event.META_MASK)));
        return menubar;
    }
    
    //Returns the currently loaded configuration file
    public String getCurPath() {
        return curPath;
    }
    
    // ********** ACTION INNER CLASSES ********** //
    
    //A very simple exit action
    public class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            System.exit(0);
        }
    }

    //An action that opens an existing file
    class OpenAction extends AbstractAction {

        public OpenAction() {
            super("Open");
        }

        //Query user for a filename and attempt to open and read the file into
        //the text component.
        @Override
        public void actionPerformed(ActionEvent ev) {
            
            if (chooser.showOpenDialog(GuiEditor.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            curPath = file.getAbsolutePath();            

            FileReader reader = null;
            try {
                reader = new FileReader(file);
                textComp.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(GuiEditor.this,
                        "File Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException x) {
                    }
                }
            }
        }
    }

    //An action that saves the document to a file
    class SaveAsAction extends AbstractAction {

        public SaveAsAction() {
            super("Save As...");
        }

        //Query user for a filename and attempt to open and write the text
        //component's content to the file.
        @Override
        public void actionPerformed(ActionEvent ev) {

            if (chooser.showSaveDialog(GuiEditor.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            curPath = file.getAbsolutePath();            

            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                textComp.write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(GuiEditor.this,
                        "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        }
    }
    

    //An action that saves the document to a file
    class SaveAction extends AbstractAction {

        public SaveAction() {
            super("Save");
        }

        //Query user for a filename and attempt to open and write the text
        //component's content to the file.
        @Override
        public void actionPerformed(ActionEvent ev) {

            File file = chooser.getSelectedFile();
            if (file == null) {
                return;
            }
            curPath = file.getAbsolutePath();            

            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                textComp.write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(GuiEditor.this,
                        "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException x) {
                        x.printStackTrace();
                    }
                }
            }
        }
    }  
    
}
