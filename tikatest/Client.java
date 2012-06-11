/* Class name: Client
 * File name:  Client.java
 * Project:    TikaTest
 * Copyright:  © 2007-2012 Alexander J. Harris, released under Creative Commons 
 * License:    Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * Created:    17-Nov-2008 22:25:48
 * Modified:   28-May-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.003  28-May-2012 Changed licensing and verified Javadoc for release on Github.
 * 1.002  27-Mar-2011 Adapted for a test of Apache Tika
 * 1.001  26-Jul-2010 Code adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package tikatest;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * This class is responsible for the GUI environment of the application.
 * The application displays a single window that contains a table which is
 * updated with metadata from the <code>Investigation</code> class.
 * <p><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">
 * <img alt="Creative Commons Licence" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" />
 * </a>
 * <br />
 * This work is licensed under a 
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
 * @version 1.003
 * @author Alexander J. Harris (email: alexander.j.harris(at)btinternet.com)
 */

public class Client extends JFrame
{
  /** The desktop pane containing all of the child windows */
  private JDesktopPane jdMain;
  /** The menu-bar for the window */
  private JMenuBar jmbMenu;
  /** The centralised instance of the class to be returned once instantiated */
  private static Client testClient;
  
  /**
   * Launches a new main window
   */
  public Client()
  {
    testClient = this;
    buildMenu();
    buildGUI();
  }

  /**
   * Part of the main constructor. This method is responsible for initialising
   * the menu along with its associated events.
   */
  private void buildMenu()
  {
    jmbMenu = new JMenuBar();
/* ~ ~ ~ ~ ~ ~ FILE MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmFile;
    JMenuItem jmiFile_Detect;
    JMenuItem jmiFile_Exit;
    jmFile = new JMenu("File");
    jmFile.setMnemonic(KeyEvent.VK_F);
    jmiFile_Detect = new JMenuItem("Investigate Files");
    jmiFile_Detect.setMnemonic(KeyEvent.VK_I);
    jmiFile_Detect.addActionListener(new InvestigateFiles(this)); // Attach the listener
    jmFile.add(jmiFile_Detect);
    jmbMenu.add(jmFile);
  }

  /**
   * Part of the constructor, it builds and places the core components of the GUI
   * on-screen.
   */
  private void buildGUI()
  {
    // Settting the JMenuBar
    setJMenuBar(jmbMenu);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Tika Test Application");
    this.setSize(600, 400);
    jdMain = new JDesktopPane();
    jdMain.setDoubleBuffered(true);
    JScrollPane jspDesktop = new JScrollPane(jdMain);
    getContentPane().add(jspDesktop,BorderLayout.CENTER);
    // Set the GUI to visible
    setVisible(true);
  }


  /**
   * Used for other classes to obtain a copy of the current Client window.
   * @return The main Client window that is being displayed.
   * @see tikatest.Client
   */
  public static Client getMainWindow()
  {
    return testClient;
  }

  /**
   * Adds the supplied instance of <code>JInternalFrame</code> to the desktop
   * for display to the user.
   * @param jIF An internal frame to be displayed to the user.
   * @see javax.swing.JInternalFrame
   */
  public void addToDesktop(JInternalFrame jIF)
  {
    jdMain.add(jIF);
  }

  /**
   * Returns the instance of <code>JDesktopPane</code> to the receiver.
   * @return The instance of <code>JDesktopPane</code> used by the current class instance.
   * @see javax.swing.JDesktopPane
   */
  public JDesktopPane getDesktopPane()
  {
    return jdMain;
  }
}