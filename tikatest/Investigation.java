/* Class name: Investigation
 * File name:  Investigation.java
 * Project:    TikaTest
 * Copyright:  © 2007-2012 Alexander J. Harris, released under Creative Commons 
 * License:    Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License
 * Created:    28-Mar-2011 20:46:55
 * Modified:   28-May-2012
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.000  28-May-2012 Changed licensing and verified Javadoc for release on Github.
 * 0.001  28-Mar-2011 Initial build
 */

package tikatest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.SwingWorker;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.tika.detect.Detector;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * This class is responsible for detecting files within a given directory and
 * investigating them (as well as sub-directories).
 * <p>Detected files are investigated using the Apache Tika and Commons Compress
 * projects.
 * <p><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">
 * <img alt="Creative Commons Licence" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" />
 * </a>
 * <br />
 * This work is licensed under a 
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
 * @version 1.000
 * @author Alexander J. Harris (email: alexander.j.harris(at)btinternet.com)
 */
public class Investigation extends SwingWorker
{
  private int numberOfFiles;
  private int progress;
  private File startFolder;
  private InvestigateFiles parent;

  /**
   * Instantiate the class and initialise the variables.
   * @param f The base directory to be investigated.
   * @param ifParent The parent window for updates to be posted to.
   */
  public Investigation(File f, InvestigateFiles ifParent)
  {
    startFolder = f;
    numberOfFiles = 0;
    progress = 0;
    parent = ifParent;
  }

  /**
   * Recursive method for scanning files and sub-directories of a given
   * directory. This is to provide an overall number of files being scanned.
   * @param f A directory path to be investigated.
   */
  private void scan(File f)
  {
    int i = f.listFiles().length;
    numberOfFiles = numberOfFiles + i;
    for (File g : f.listFiles())
    {
      if (g.isDirectory())
      {
          numberOfFiles--;
          scan(g);
      }
    }
  }

  /**
   * Iterates through files and sub-directories in the given directory.
   * <p>Detected files are passed to the <code>investigate(File)</code> method
   * while sub-directories are recursively passed back to this method.
   * @param f The given directory path
   */
  private void interrogate(File f)
  {
    if (f.isDirectory())
    {
      for (File g : f.listFiles())
      {
        if (g.isDirectory())
        {
            interrogate(g);
        }
        else
        {
          progress++;
          investigate(g);
        }
      }
    }
  }
  
  /**
   * Take a given file and extract any contained metadata. All of the metadata is
   * printed on the command line and a summary shown on the GUI.
   * <p>This uses <a href="http://tika.apache.org">Apache Tika</a> to extract
   * the metadata and also detect the content language.
   * <p>Document formats that are supported by Tika are listed on the project's
   * main site <a href="http://tika.apache.org/1.1/formats.html">here</a>.
   * <p>Archive formats are passed to the <code>handleGeneric()</code> method
   * which uses Apache Commons Compress to investigate the contents. Currently
   * supported MIME types for this are:
   * <uL>
   *   <li>archive/zip</li>
   *   <li>archive/x-cpio</li>
   *   <li>archive/x-gtar</li>
   *   <li>archive/x-bzip</li>
   *   <li>archive/x-bzip2</li>
   *   <li>archive/x-archive</li>
   *   <li>archive/x-tar</li>
   * </ul>
   * <p>This method makes use of code which has been publicly available through
   * the Apache website.
   * @param g The file to investigate
   * @see java.io.File
   */
  private void investigate(File g)
  {
    System.out.println("Investigating: " + g.getAbsolutePath());
    try
    {
      // Open the file as an InputStream
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(g));
      // Create a new instances of Metadata to store the file's meta-data.
      Metadata meta = new Metadata();
      ParseContext pc = new ParseContext();
      StringWriter sw = new StringWriter();
      WriteOutContentHandler woch = new WriteOutContentHandler(sw);
      // We use AutoDetectParser as we cannot be certain of the content
      AutoDetectParser adp = new AutoDetectParser();
      // We parse the document to extract the metadata into the empty metadata object
      adp.parse(bis, woch, meta, pc);
      // After parsing the file the InputStream maybe closed. Let's force it to close
      bis.close();
      // And now reopen the file to detect the MIME type
      bis = new BufferedInputStream(new FileInputStream(g));
      Detector d = adp.getDetector();
      // The MediaType class is used to contain the MIME type information
      MediaType mt = d.detect(bis, meta);
      // We extract some of the document's content from the StringWriter used earlier
      String content = sw.toString();
      // From the content we can run this through the LanguageIdentifier
      LanguageIdentifier li = new LanguageIdentifier(content);
      
      /* The getLanguage method will return an ISO 639-1 identifier.
       * Supported languages in Tika 0.9 included:
       * Danish (da)      * Dutch (nl)        * English (en)
       * Estonian (et)    * Finnish (fi)      * French (fr)
       * German (de)      * Greek (el)        * Hungarian (hu)
       * Icelandic (is)   * Italian (it)      * Norwegian - Bokmål (nb)
       * Polish (pl)      * Portuguese (pt)   * Russian (ru)
       * Spanish (es)     * Swedish (sv)      * Thai (th)
       * ----------------------------------------------------------------------------
       * NOTE: Depending on the encoding of the document contents results may vary
       * PDF files, for example, can return "et" instead of "en" where the contents
       * of the StringWriter is encoded characters.
       */
      System.out.println("Detected language: " + li.getLanguage());
      // Create a row for the GenericTable (JTable) in the GUI.
      Object[] row = new Object[]{"?","?","?","?","?","No","No"};
      row[0] = g.getName();     // Set the file name
      row[1] = g.getPath();     // Set the file path
      row[2] = mt.toString();   // Set the whole MIME type e.g. application/zip
      row[3] = mt.getType();    // Set the MIME main type e.g. application
      row[4] = mt.getSubtype(); // Set the MIME subtype e.g. zip
      System.out.println("Detected type: " + mt.toString());

      // Check to see if the file type is an archive - as supported by the PackageParser in Tika
      if (mt.getSubtype().equals("zip") ||
          mt.getSubtype().equals("x-cpio") ||
          mt.getSubtype().equals("x-gtar") ||
          mt.getSubtype().equals("x-bzip") ||
          mt.getSubtype().equals("x-bzip2") ||
          mt.getSubtype().equals("x-archive") ||
          mt.getSubtype().equals("x-tar"))
      { 
        // So far we've only extracted the archive metadata so we now pass the
        // InputStream to the method to extract the contents of the archive
        handleGeneric(bis);
      }
      
      // Parameters aren't frequently used but if the file has them then iterate
      // through them and print them to the console.
      Map<String,String> params = mt.getParameters();
      Iterator i = params.keySet().iterator();
      if (params.size() > 0)
      {
        System.out.println("Parameters detected for " + g.getAbsolutePath() + ":");
        row[5] = "Yes"; // Show in the GUI table that Parameters are present
      }
      while (i.hasNext())
      {
        String k = i.next().toString();
        String v = params.get(k);
        System.out.printf("\t%s\t%s\n", k,v); // Print the parameter to the console
      }
      // Check the size of the Metadata array
      if (meta.size() > 0)
      {
        // If metadata is present then update the GUI table.
        row[6] = "Yes";
        // Output the file name in the console
        System.out.println("Metadata detected for " + g.getAbsolutePath() + ":");
        for (String name : meta.names())
        {
          String value = meta.get(name);
          // Output the metadata parameter and value
          System.out.println("\t" + name + ":\t" + value);
        }
      }
      // Add the row to the GUI's GenericTable (JTable)
      parent.addRow(row);
    }
    catch (Exception x)
    {
      /*
       * Catch any errors and briefly print the stack.
       * 
       * Likely errors result from an insufficient heap size when parsing files
       * in Tika. If you do receive this error then adjust the JVM arguments.
       */
      x.printStackTrace();
    }
    finally
    {
      // Update the SwingWorker thread
      publish(progress);
    }
  }

  /**
   * Starts the thread to investigate files starting from the directory specified
   * in the constructor.
   * @return Returns nothing.
   */
  public Void doInBackground()
  {
    scan(startFolder);
    interrogate(startFolder);
    return null;
  }
  
  /**
   * Takes a file which has already been detected as a supported archive and
   * displays the contents.
   * <p>This method makes use of code which has been publicly available through
   * the Apache website.
   * @param The file to be inspected
   * @see java.io.BufferedInputStream
   */
  private void handleGeneric(BufferedInputStream bis)
  {
    try
    {
      // File type is a known archive type and we can work with it (fingers crossed)
      ArchiveInputStream aisInput = new ArchiveStreamFactory().createArchiveInputStream(bis);
      ArchiveEntry aeFile = aisInput.getNextEntry();
      System.out.println("ArchiveInputStream: " + aisInput.getClass().getName());
      System.out.println("Archive Entry - Type: " + aeFile.getClass().getName());
      while (aeFile != null)
      {
        if (!aeFile.isDirectory())
        {
          String[] segments = aeFile.getName().split("\\/");
          String filename = "";
          for(String segment: segments)
          {
            filename = segment;
          }
          System.out.println("Archive Entry - Name: " + filename);
        }
        aeFile = aisInput.getNextEntry();
      }
    }
    catch (ArchiveException aX)
    {
      aX.printStackTrace();
    }
    catch (IOException ioX)
    {
      ioX.printStackTrace();
    }
  }
}