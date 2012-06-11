/* Class name: GenericFrame
 * File name:  GenericFrame.java
 * Project:    TikaTest
 * Copyright:  © 2007-2012 Alexander J. Harris, released under Creative Commons 
 * License:    Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * Created:    18-Mar-2009 19:13:20
 * Modified:   28-May-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.002  28-May-2012 Changed licensing and verified Javadoc for release on Github.
 * 1.001  28-Jul-2010 Code adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package tikatest;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.TableRowSorter;

/**
 * Used for displaying the contents of a <code>GenericTable</code>. It provides a simple means of
 * displaying the results of a <code>GenericTable</code> instance that can be
 * sorted by the user, by clicking on the column headers. It also provides a
 * means for the data to be exported to another program for further analysis or
 * presentation through the open CSV (comma separated value) format.
 * <p><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">
 * <img alt="Creative Commons Licence" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" />
 * </a>
 * <br />
 * This work is licensed under a 
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
 * @version 1.002
 * @author Alexander J. Harris (email: alexander.j.harris(at)btinternet.com)
 */

public class GenericFrame extends JInternalFrame
{
  /** The table holding the data to be displayed */
  private GenericTable table;

  /**
   * Initialises the class and internal logger as well as displaying the data
   * contained within the supplied instance of <code>GenericTable</code>.
   * <p>The instance of <code>GenericTable</code> passed as an argument will
   * have already had information about its structure defined through its own
   * initialisation. This allows for the this class to simply wrap it in an
   * instance of <code>JScrollPane</code> and present it to the user with a
   * basic menu allowing for the data contents to be exported.
   * <p>The class is primarily used to show the results from the metadata
   * extraction class (<code>Investigation</code>).
   * @param title The title of the window.
   * @param gT The data to be displayed in the window.
   * @see tikatest.GenericTable
   */
  public GenericFrame(String title, GenericTable gT)
  {
    super(title, true, true, true, true);
    JTable jT = new JTable(gT);
    jT.setRowSorter(new TableRowSorter(gT));
    JScrollPane jspTable = new JScrollPane(jT);
    this.add(jspTable);
    table = gT;
    SpringLayout slGenericFrame = new SpringLayout();
    this.setLayout(slGenericFrame);
    slGenericFrame.putConstraint(SpringLayout.NORTH, jspTable, 2, SpringLayout.NORTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.EAST, jspTable, -2, SpringLayout.EAST, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.SOUTH, jspTable, -2, SpringLayout.SOUTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.WEST, jspTable, 2, SpringLayout.WEST, this.getContentPane());
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(500,350);
  }

  /**
   * Returns the instance of <code>GenericTable</code> used to contain data
   * displayed within the window.
   * @return The data model displayed in the window.
   * @see tikatest.GenericTable
   */
  public GenericTable getGenericTable()
  {
    return table;
  }
}