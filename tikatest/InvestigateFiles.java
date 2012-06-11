/* Class name: InvestigateFiles
 * File name:  InvestigateFiles.java
 * Project:    TikaTest
 * Copyright:  © 2007-2012 Alexander J. Harris, released under Creative Commons 
 * License:    Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * Created:    19-Nov-2008 12:55:10
 * Modified:   28-May-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.002  28-May-2012 Changed licensing and verified Javadoc for release on Github.
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package tikatest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 * This class handles any request to close Odin. It does this
 * by closing all log files gracefully and performs garbage collection prior to
 * the application closing.
 * <p>This class extends the <code>WindowAdapter</code> class and implements
 * the <code>ActionListener</code> interface in order to handle both types of
 * events generate either by the user closing the window, or requesting the
 * application exit through the file menu.
 * <p><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">
 * <img alt="Creative Commons Licence" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" />
 * </a>
 * <br />
 * This work is licensed under a 
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
 * @version 1.002
 * @author Alexander J. Harris (email: alexander.j.harris(at)btinternet.com)
 */
public class InvestigateFiles implements ActionListener
{
  private GenericTable gt;
  private Client parent;

  /**
   * The standard constructor for the class.
   */
  public InvestigateFiles(Client client)
  {
    parent = client;
  }

  /**
   * This method is called when the user clicks on File > Investigate from the menu.
   * The program displays a selection box where the user can choose a directory
   * to be investigated and the metadata of the contents outputted to the command
   * line and a summary to the GUI.
   * @param ae The generated ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Select folder to examine");
    fc.setMultiSelectionEnabled(true);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int returnVal = fc.showDialog(null,"Select");
    if (returnVal == 1)
    {
        return;
    }
    File f = fc.getSelectedFile();

    gt = new GenericTable();
    java.util.ArrayList<String> columns = new java.util.ArrayList<String>();
    columns.add("File Name");
    columns.add("File Path");
    columns.add("MIME Type");
    columns.add("MIME Main Type");
    columns.add("MIME Sub-type");
    columns.add("Parameters Present?");
    columns.add("Meta-data Present?");
    ArrayList row = new ArrayList();
    row.add(new Object[]{"?","?","?","?","?","?","?"});
    gt.setData(columns, row);

    // This is where the investigation is actually launched on the chosen directory.
    Investigation inv = new Investigation(f, this);
    // As the class uses SwingWorker we can let it run in the background.
    inv.doInBackground();

    GenericFrame gf = new GenericFrame("Investigation Results:",gt);
    parent.addToDesktop(gf);
    gf.setVisible(true);
  }
  
  /**
   * Add a row to the GenericFrame.
   * @param rowData An object array corresponding to the columns of the table.
   */
  public void addRow(Object[] rowData)
  {
    gt.addRow(rowData);
  }
}