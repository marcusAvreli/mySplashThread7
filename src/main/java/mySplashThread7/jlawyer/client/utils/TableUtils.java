package mySplashThread7.jlawyer.client.utils;

import mySplashThread7.gdev.gawt.GTable;

//j-lawyer-client/src/com/jdimension/jlawyer/client/utils/TableUtils.java
//https://github.com/jlawyerorg/j-lawyer-org/blob/27f0d0f3fad06fb6a16079d838ab803c66394f8b/j-lawyer-client/src/com/jdimension/jlawyer/client/utils/TableUtils.java
/**
*
* @author jens
*/
public class TableUtils {

   private static String CELL_BREAK = "\t";
   private static String LINE_BREAK = System.getProperty("line.separator");

   public static void exportAndLaunch(String fileName, GTable table) throws Exception {
/*

       int numRows = table.getRowCount();
       int numCols = table.getColumnCount();

       StringBuffer excelStr = new StringBuffer();

       for (int i = 0; i < numCols; i++) {
           excelStr.append(escape(table.getColumnName(i)));
           if (i < numCols - 1) {
               excelStr.append(CELL_BREAK);
           }
       }
       excelStr.append(LINE_BREAK);

       for (int i = 0; i < numRows; i++) {
           for (int j = 0; j < numCols; j++) {
               Object cellValue = table.getValueAt(i, j);
               if (cellValue instanceof Boolean) {
                   if ((Boolean) cellValue == false) {
                       cellValue = "nein";
                   } else {
                       cellValue = "ja";
                   }
               }
               excelStr.append(escape(cellValue));
               if (j < numCols - 1) {
                   excelStr.append(CELL_BREAK);
               }
           }
           excelStr.append(LINE_BREAK);
       }

       ReadOnlyDocumentStore store = new ReadOnlyDocumentStore("jtableexport-" + fileName, fileName);
       Launcher launcher = LauncherFactory.getLauncher(fileName, excelStr.toString().getBytes(), store);
       launcher.launch(false);
*/
   }
}