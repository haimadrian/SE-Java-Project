package org.spa.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class StringUtils {
   private static final Map<String, String> htmlToWildcard = new HashMap<>();
   private static final Map<String, String> wildcardToHtml = new HashMap<>();
   private static final String ILLEGAL_OS_FILE_NAME_CHARS_REGEX = "[\\\\/:*?\"<>|]";

   static {
      htmlToWildcard.put("<b>", "~BOLD");
      htmlToWildcard.put("<i>", "~ITALIC");
      htmlToWildcard.put("<u>", "~UNDERLINE");
      htmlToWildcard.put("</b>", "BOLD~");
      htmlToWildcard.put("</i>", "ITALIC~");
      htmlToWildcard.put("</u>", "UNDERLINE~");

      wildcardToHtml.put("~BOLD", "<b>");
      wildcardToHtml.put("~ITALIC", "<i>");
      wildcardToHtml.put("~UNDERLINE", "<u>");
      wildcardToHtml.put("BOLD~", "</b>");
      wildcardToHtml.put("ITALIC~", "</i>");
      wildcardToHtml.put("UNDERLINE~", "</u>");
   }

   /**
    * In order to support HTML styling in strings, we need to escape them before escaping HTML characters, hence we use
    * this method so we can keep the HTML style.
    *
    * @param text The text to replace its HTML styling tags with wildcards
    * @return The modified text
    */
   public static String replaceHTMLStyleWithWildcard(String text) {
      String modifiedText = text;
      for (Map.Entry<String, String> currEnt : htmlToWildcard.entrySet()) {
         modifiedText = modifiedText.replace(currEnt.getKey(), currEnt.getValue());
      }

      return modifiedText;
   }

   /**
    * In order to support HTML styling in strings, we need to escape them before escaping HTML characters, hence we use
    * this method so we can keep the HTML style. This method responsible to unescape a previously escaped styled text
    *
    * @param text The text to replace its wildcards with HTML styling tags
    * @return The modified text
    */
   public static String replaceWildcardWithHTMLStyle(String text) {
      String modifiedText = text;
      for (Map.Entry<String, String> currEnt : wildcardToHtml.entrySet()) {
         modifiedText = modifiedText.replace(currEnt.getKey(), currEnt.getValue());
      }

      return modifiedText;
   }

   /**
    * This utility method created to help making an illegal file name legal.
    *
    * @param fileName The file name that the user of this class supplied.
    * @return The file name as a OS valid file name.
    */
   public static String toLegalFileName(String fileName) {
      String legalFileName = fileName;
      if (legalFileName != null) {
         legalFileName = legalFileName.replaceAll(ILLEGAL_OS_FILE_NAME_CHARS_REGEX, "_");
      }

      return legalFileName;
   }
}
