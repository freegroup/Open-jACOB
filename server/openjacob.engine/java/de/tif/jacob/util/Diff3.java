/*
 * Diff Match and Patch
 *
 * Copyright 2006 Google Inc.
 * http://code.google.com/p/google-diff-match-patch/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tif.jacob.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;


/*
 * Functions for diff, match and patch.
 * Computes the difference between two texts to create a patch.
 * Applies the patch onto another text, allowing for errors.
 *
 * @author fraser@google.com (Neil Fraser)
 * 
 * ported from java 6.0 to java 1.4 by Andreas Herz
 */

/**
 * Class containing the diff, match and patch methods.
 * Also contains the behaviour settings.
 */
public class Diff3 
{

  // Defaults.
  // Set these on your match_patch instance to override the defaults.

  // Number of seconds to map a diff before giving up.  (0 for infinity)
  public float Timeout = 1.0f;
  // Cost of an empty edit operation in terms of edit characters.
  public short EditCost = 4;
  // The size beyond which the double-ended diff activates.
  // Double-ending is twice as fast, but less accurate.
  public short DualThreshold = 32;
  // Tweak the relative importance (0.0 = accuracy, 1.0 = proximity)
  public float Match_Balance = 0.5f;
  // At what point is no match declared (0.0 = perfection, 1.0 = very loose)
  public float Match_Threshold = 0.5f;
  // The min and max cutoffs used when computing text lengths.
  public int Match_MinLength = 100;
  public int Match_MaxLength = 1000;
  // Chunk size for context length.
  public short Patch_Margin = 4;

  // The number of bits in an int.
  private int Match_MaxBits = 32;


  //  DIFF FUNCTIONS


  /**-
   * The data structure representing a diff is a Linked list of Diff objects:
   * {Diff(OPERATION_DELETE, "Hello"), Diff(OPERATION_INSERT, "Goodbye"),
   *  Diff(OPERATION_EQUAL, " world.")}
   * which means: delete "Hello", add "Goodbye" and keep " world."
   */
   private final static int OPERATION_DELETE=1;
   private final static int OPERATION_INSERT=2;
   private final static int OPERATION_EQUAL =3;


  /**
   * Find the differences between two texts.
   * Run a faster slightly less optimal diff
   * This method allows the 'checklines' of main() to be optional.
   * Most of the time checklines is wanted, so default to true.
   * @param text1 Old string to be diffed
   * @param text2 New string to be diffed
   * @return Linked List of Diff objects
   */
  public LinkedList diff(String text1, String text2) 
  {
    return diff(text1, text2, true);
  }

  /**
   * Find the differences between two texts.  Simplifies the problem by
   * stripping any common prefix or suffix off the texts before diffing.
   * @param text1 Old string to be diffed
   * @param text2 New string to be diffed
   * @param checklines Speedup flag.  If false, then don't run a
   *     line-level diff first to identify the changed areas.
   *     If true, then run a faster slightly less optimal diff
   * @return Linked List of Diff objects
   */
  public LinkedList diff(String text1, String text2, boolean checklines) 
  {
    // Check for equality (speedup)
    LinkedList diffs;
    if (text1.equals(text2)) 
    {
      diffs = new LinkedList();
      diffs.add(new Diff(OPERATION_EQUAL, text1));
      return diffs;
    }

    // Trim off common prefix (speedup)
    int commonlength = commonPrefix(text1, text2);
    String commonprefix = text1.substring(0, commonlength);
    text1 = text1.substring(commonlength);
    text2 = text2.substring(commonlength);

    // Trim off common suffix (speedup)
    commonlength = commonSuffix(text1, text2);
    String commonsuffix = text1.substring(text1.length() - commonlength);
    text1 = text1.substring(0, text1.length() - commonlength);
    text2 = text2.substring(0, text2.length() - commonlength);

    // Compute the diff on the middle block
    diffs = compute(text1, text2, checklines);

    // Restore the prefix and suffix
    if (commonprefix.length() != 0) {
      diffs.addFirst(new Diff(OPERATION_EQUAL, commonprefix));
    }
    if (commonsuffix.length() != 0) {
      diffs.addLast(new Diff(OPERATION_EQUAL, commonsuffix));
    }

    cleanupMerge(diffs);
    return diffs;
  }


  /**
   * Find the differences between two texts.  Assumes that the texts do not
   * have any common prefix or suffix.
   * @param text1 Old string to be diffed
   * @param text2 New string to be diffed
   * @param checklines Speedup flag.  If false, then don't run a
   *     line-level diff first to identify the changed areas.
   *     If true, then run a faster slightly less optimal diff
   * @return Linked List of Diff objects
   */
  protected LinkedList compute(String text1, String text2,  boolean checklines) 
  {
    LinkedList diffs = new LinkedList();

    if (text1.length() == 0) {
      // Just add some text (speedup)
      diffs.add(new Diff(OPERATION_INSERT, text2));
      return diffs;
    }

    if (text2.length() == 0) {
      // Just delete some text (speedup)
      diffs.add(new Diff(OPERATION_DELETE, text1));
      return diffs;
    }

    String longtext = text1.length() > text2.length() ? text1 : text2;
    String shorttext = text1.length() > text2.length() ? text2 : text1;
    int i = longtext.indexOf(shorttext);
    if (i != -1) {
      // Shorter text is inside the longer text (speedup)
      int op = (text1.length() > text2.length()) ?
          OPERATION_DELETE : OPERATION_INSERT;
      diffs.add(new Diff(op, longtext.substring(0, i)));
      diffs.add(new Diff(OPERATION_EQUAL, shorttext));
      diffs.add(new Diff(op, longtext.substring(i + shorttext.length())));
      return diffs;
    }
    longtext = shorttext = null;  // Garbage collect

    // Check to see if the problem can be split in two.
    String[] hm = halfMatch(text1, text2);
    if (hm != null) {
      // A half-match was found, sort out the return data.
      String text1_a = hm[0];
      String text1_b = hm[1];
      String text2_a = hm[2];
      String text2_b = hm[3];
      String mid_common = hm[4];
      // Send both pairs off for separate processing.
      LinkedList diffs_a=diff(text1_a, text2_a, checklines);
      LinkedList diffs_b=diff(text1_b, text2_b, checklines);
      // Merge the results.
      diffs = diffs_a;
      diffs.add(new Diff(OPERATION_EQUAL, mid_common));
      diffs.addAll(diffs_b);
      return diffs;
    }

    // Perform a real diff.
    if (checklines && (text1.length() < 100 || text2.length() < 100)) {
      checklines = false;  // Too trivial for the overhead.
    }
    ArrayList linearray = null;
    if (checklines) {
      // Scan the text on a line-by-line basis first.
      Object b[] = linesToChars(text1, text2);
      text1 = (String) b[0];
      text2 = (String) b[1];
      linearray = (ArrayList) b[2];
     }

    diffs = map(text1, text2);
    if (diffs == null) {
      // No acceptable result.
      diffs = new LinkedList();
      diffs.add(new Diff(OPERATION_DELETE, text1));
      diffs.add(new Diff(OPERATION_INSERT, text2));
    }

    if (checklines) {
      // Convert the diff back to original text.
      charsToLines(diffs, linearray);
      // Eliminate freak matches (e.g. blank lines)
      cleanupSemantic(diffs);

      // Rediff any replacement blocks, this time character-by-character.
      // Add a dummy entry at the end.
      diffs.add(new Diff(OPERATION_EQUAL, ""));
      int count_delete = 0;
      int count_insert = 0;
      String text_delete = "";
      String text_insert = "";
      ListIterator pointer = diffs.listIterator();
      Diff thisDiff = (Diff)pointer.next();
      while (thisDiff != null) {
        switch (thisDiff.operation) {
        case OPERATION_INSERT:
          count_insert++;
          text_insert += thisDiff.text;
          break;
        case OPERATION_DELETE:
          count_delete++;
          text_delete += thisDiff.text;
          break;
        case OPERATION_EQUAL:
          // Upon reaching an equality, check for prior redundancies.
          if (count_delete >= 1 && count_insert >= 1) {
            // Delete the offending records and add the merged ones.
            pointer.previous();
            for (int j = 0; j < count_delete + count_insert; j++) {
              pointer.previous();
              pointer.remove();
            }
            Iterator tmp=diff(text_delete, text_insert, false).iterator();
            while(tmp.hasNext()) 
            {
              pointer.add(tmp.next());
            }
          }
          count_insert = 0;
          count_delete = 0;
          text_delete = "";
          text_insert = "";
          break;
        }
        thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
      }
      diffs.removeLast();  // Remove the dummy entry at the end.
    }
    return diffs;
  }


  /**
   * Split two texts into a list of strings.  Reduce the texts to a string of
   * hashes where each Unicode character represents one line.
   * @param text1 First string
   * @param text2 Second string
   * @return Three element Object array, containing the encoded text1, the
   *     encoded text2 and the List of unique strings.  The zeroth element
   *     of the List of unique strings is intentionally blank.
   */
  private Object[] linesToChars(String text1, String text2) 
  {
    List lineArray = new ArrayList();
    Map/*<String, Integer>*/ lineHash = new HashMap/*<String, Integer>*/();
    // e.g. linearray[4] == "Hello\n"
    // e.g. linehash.get("Hello\n") == 4

    // "\x00" is a valid character, but various debuggers don't like it.
    // So we'll insert a junk entry to avoid generating a null character.
    lineArray.add("");

    String chars1 = linesToCharsMunge(text1, lineArray, lineHash);
    String chars2 = linesToCharsMunge(text2, lineArray, lineHash);
    return new Object[]{chars1, chars2, lineArray};
  }


  /**
   * Split a text into a list of strings.  Reduce the texts to a string of
   * hashes where each Unicode character represents one line.
   * @param text String to encode
   * @param lineArray List of unique strings
   * @param lineHash Map of strings to indices
   * @return Encoded string
   */
  private String linesToCharsMunge(String text, List/*<String>*/ lineArray,
                                        Map/*<String, Integer>*/ lineHash) {
    int lineStart = 0;
    int lineEnd = -1;
    String line;
    StringBuffer chars = new StringBuffer();
    // Walk the text, pulling out a substring for each line.
    // text.split('\n') would would temporarily double our memory footprint.
    // Modifying text would create many large strings to garbage collect.
    while (lineEnd < text.length() - 1) {
      lineEnd = text.indexOf('\n', lineStart);
      if (lineEnd == -1) {
        lineEnd = text.length() - 1;
      }
      line = text.substring(lineStart, lineEnd + 1);
      lineStart = lineEnd + 1;

      if (lineHash.containsKey(line)) {
        chars.append(String.valueOf((char) ((Integer)lineHash.get(line)).intValue()));
      } else {
        lineArray.add(line);
        lineHash.put(line, new Integer(lineArray.size() - 1));
        chars.append(String.valueOf((char) (lineArray.size() - 1)));
      }
    }
    return chars.toString();
  }


  /**
   * Rehydrate the text in a diff from a string of line hashes to real lines of
   * text.
   * @param diffs LinkedList of Diff objects
   * @param lineArray List of unique strings
   */
  private void charsToLines(LinkedList diffs,List/*<String>*/ lineArray) 
  {
    StringBuffer text;
    Iterator diffIter = diffs.iterator();
    while(diffIter.hasNext())
    {
      Diff diff =(Diff)diffIter.next();
      text = new StringBuffer();
      for (int y = 0; y < diff.text.length(); y++) {
        text.append(lineArray.get(diff.text.charAt(y)));
      }
      diff.text = text.toString();
    }
  }


  /**
   * Explore the intersection points between the two texts.
   * @param text1 Old string to be diffed
   * @param text2 New string to be diffed
   * @return LinkedList of Diff objects or null if no diff available
   */
  private LinkedList map(String text1, String text2) {
    long ms_end = System.currentTimeMillis() + (long) (Timeout * 1000);
    int max_d = text1.length() + text2.length() - 1;
    boolean doubleEnd = DualThreshold * 2 < max_d;
    List/*<Set<Long>>*/ v_map1 = new ArrayList/*<Set<Long>>*/();
    List/*<Set<Long>>*/ v_map2 = new ArrayList/*<Set<Long>>*/();
    Map/*<Integer, Integer>*/ v1 = new HashMap/*<Integer, Integer>*/();
    Map/*<Integer, Integer>*/ v2 = new HashMap/*<Integer, Integer>*/();
    v1.put(new Integer(1), new Integer(0));
    v2.put(new Integer(1), new Integer(0));
    int x, y;
    Long footstep = new Long(0);  // Used to track overlapping paths.
    Map/*<Long, Integer>*/ footsteps = new HashMap/*<Long, Integer>*/();
    boolean done = false;
    // If the total number of characters is odd, then the front path will
    // collide with the reverse path.
    boolean front = ((text1.length() + text2.length()) % 2 == 1);
    for (int d = 0; d < max_d; d++) 
    {
      // Bail out if timeout reached.
      if (Timeout > 0 && System.currentTimeMillis() > ms_end) 
      {
        return null;
      }

      // Walk the front path one step.
      v_map1.add(new HashSet/*<Long>*/());  // Adds at index 'd'.
      for (int k = -d; k <= d; k += 2) 
      {
        if (k == -d || k != d && ((Integer)v1.get(new Integer(k - 1))).intValue() < ((Integer)v1.get(new Integer(k + 1))).intValue()) {
          x = ((Integer)v1.get(new Integer(k + 1))).intValue();
        } else {
          x = ((Integer)v1.get(new Integer(k - 1))).intValue() + 1;
        }
        y = x - k;
        if (doubleEnd) {
          footstep = footprint(x, y);
          if (front && (footsteps.containsKey(footstep))) {
            done = true;
          }
          if (!front) {
            footsteps.put(footstep, new Integer(d));
          }
        }
        while (!done && x < text1.length() && y < text2.length()
               && text1.charAt(x) == text2.charAt(y)) {
          x++;
          y++;
          if (doubleEnd) {
            footstep = footprint(x, y);
            if (front && (footsteps.containsKey(footstep))) {
              done = true;
            }
            if (!front) {
              footsteps.put(footstep, new Integer( d));
            }
          }
        }
        v1.put(new Integer(k), new Integer(x));
        ((Set)v_map1.get(d)).add(footprint(x, y));
        if (x == text1.length() && y == text2.length()) 
        {
          // Reached the end in single-path mode.
          return path1(v_map1, text1, text2);
        } 
        else if (done) 
        {
          // Front path ran over reverse path.
          v_map2 = v_map2.subList(0, ((Integer)footsteps.get(footstep)).intValue() + 1);
          LinkedList a = path1(v_map1, text1.substring(0, x),
                                          text2.substring(0, y));
          a.addAll(path2(v_map2, text1.substring(x), text2.substring(y)));
          return a;
        }
      }

      if (doubleEnd) 
      {
        // Walk the reverse path one step.
        v_map2.add(new HashSet/*<Long>*/());  // Adds at index 'd'.
        for (int k = -d; k <= d; k += 2) {
          if (k == -d || k != d && ((Integer)v2.get(new Integer(k - 1))).intValue() < ((Integer)v2.get(new Integer(k + 1))).intValue()) 
          {
            x = ((Integer)v2.get(new Integer(k + 1))).intValue();
          } 
          else 
          {
            x = ((Integer)v2.get(new Integer(k - 1))).intValue() + 1;
          }
          y = x - k;
          footstep = footprint(text1.length() - x, text2.length() - y);
          if (!front && (footsteps.containsKey(footstep))) {
            done = true;
          }
          if (front) 
          {
            footsteps.put(footstep, new Integer(d));
          }
          while (!done && x < text1.length() && y < text2.length()
                 && text1.charAt(text1.length() - x - 1)
                 == text2.charAt(text2.length() - y - 1)) {
            x++;
            y++;
            footstep = footprint(text1.length() - x, text2.length() - y);
            if (!front && (footsteps.containsKey(footstep))) {
              done = true;
            }
            if (front) {
              footsteps.put(footstep, new Integer(d));
            }
          }
          v2.put(new Integer(k), new Integer(x));
          ((Set)v_map2.get(d)).add(footprint(x, y));
          if (done) {
            // Reverse path ran over front path.
            v_map1 = v_map1.subList(0, ((Integer)footsteps.get(footstep)).intValue() + 1);
            LinkedList a
                = path1(v_map1, text1.substring(0, text1.length() - x),
                             text2.substring(0, text2.length() - y));
            a.addAll(path2(v_map2, text1.substring(text1.length() - x),
                                text2.substring(text2.length() - y)));
            return a;
          }
        }
      }
    }
    // Number of diffs equals number of characters, no commonality at all.
    return null;
  }


  /**
   * Work from the middle back to the start to determine the path.
   * @param v_map List of path sets.
   * @param text1 Old string fragment to be diffed
   * @param text2 New string fragment to be diffed
   * @return LinkedList of Diff objects
   */
  private LinkedList path1(List/*<Set<Long>>*/ v_map,
                                        String text1, String text2) {
    LinkedList path = new LinkedList();
    int x = text1.length();
    int y = text2.length();
    int last_op = -1;
    for (int d = v_map.size() - 2; d >= 0; d--) {
      while (true) {
        if (((Set)v_map.get(d)).contains(footprint(x - 1, y))) {
          x--;
          if (last_op == OPERATION_DELETE) {
            ((Diff)path.getFirst()).text = text1.charAt(x) + ((Diff)path.getFirst()).text;
          } else {
            path.addFirst(new Diff(OPERATION_DELETE,
                                   text1.substring(x, x + 1)));
          }
          last_op = OPERATION_DELETE;
          break;
        } else if (((Set)v_map.get(d)).contains(footprint(x, y - 1))) {
          y--;
          if (last_op == OPERATION_INSERT) {
            ((Diff)path.getFirst()).text = text2.charAt(y) + ((Diff)path.getFirst()).text;
          } else {
            path.addFirst(new Diff(OPERATION_INSERT,
                                   text2.substring(y, y + 1)));
          }
          last_op = OPERATION_INSERT;
          break;
        } else {
          x--;
          y--;
          if (last_op == OPERATION_EQUAL) {
            ((Diff)path.getFirst()).text = text1.charAt(x) + ((Diff)path.getFirst()).text;
          } else {
            path.addFirst(new Diff(OPERATION_EQUAL, text1.substring(x, x + 1)));
          }
          last_op = OPERATION_EQUAL;
        }
      }
    }
    return path;
  }


  /**
   * Work from the middle back to the end to determine the path.
   * @param v_map List of path sets.
   * @param text1 Old string fragment to be diffed
   * @param text2 New string fragment to be diffed
   * @return LinkedList of Diff objects
   */
  private LinkedList path2(List/*<Set<Long>>*/ v_map, String text1, String text2) 
  {
    LinkedList path = new LinkedList();
    int x = text1.length();
    int y = text2.length();
    int last_op = -1;
    for (int d = v_map.size() - 2; d >= 0; d--) {
      while (true) {
        if (((Set)v_map.get(d)).contains(footprint(x - 1, y))) {
          x--;
          if (last_op == OPERATION_DELETE) {
            ((Diff)path.getLast()).text += text1.charAt(text1.length() - x - 1);
          } else {
            path.addLast(new Diff(OPERATION_DELETE,
                text1.substring(text1.length() - x - 1, text1.length() - x)));
          }
          last_op = OPERATION_DELETE;
          break;
        } else if (((Set)v_map.get(d)).contains(footprint(x, y - 1))) {
          y--;
          if (last_op == OPERATION_INSERT) {
            ((Diff)path.getLast()).text += text2.charAt(text2.length() - y - 1);
          } else {
            path.addLast(new Diff(OPERATION_INSERT,
                text2.substring(text2.length() - y - 1, text2.length() - y)));
          }
          last_op = OPERATION_INSERT;
          break;
        } else {
          x--;
          y--;
          if (last_op == OPERATION_EQUAL) {
            ((Diff)path.getLast()).text += text1.charAt(text1.length() - x - 1);
          } else {
            path.addLast(new Diff(OPERATION_EQUAL,
                text1.substring(text1.length() - x - 1, text1.length() - x)));
          }
          last_op = OPERATION_EQUAL;
        }
      }
    }
    return path;
  }


  /**
   * Compute a good hash of two integers.
   * @param x First int
   * @param y Second int
   * @return A long made up of both ints.
   */
  private Long footprint(int x, int y) {
    // The maximum size for a long is 9,223,372,036,854,775,807
    // The maximum size for an int is 2,147,483,647
    // Two ints fit nicely in one long.
    // The return value is usually destined as a key in a hash, so return an
    // object rather than a primitive, thus skipping an automatic boxing.
    long result = x;
    result = result << 32;
    result += y;
    return new Long(result);
  }


  /**
   * Determine the common prefix of two strings
   * @param text1 First string
   * @param text2 Second string
   * @return The number of characters common to the start of each string.
   */
  private int commonPrefix(String text1, String text2) 
  {
    // Performance analysis: http://neil.fraser.name/news/2007/10/09/
    int n = Math.min(text1.length(), text2.length());
    for (int i = 0; i < n; i++) {
      if (text1.charAt(i) != text2.charAt(i)) {
        return i;
      }
    }
    return n;
  }


  /**
   * Determine the common suffix of two strings
   * @param text1 First string
   * @param text2 Second string
   * @return The number of characters common to the end of each string.
   */
  private int commonSuffix(String text1, String text2) {
    // Performance analysis: http://neil.fraser.name/news/2007/10/09/
    int n = Math.min(text1.length(), text2.length());
    for (int i = 0; i < n; i++) {
      if (text1.charAt(text1.length() - i - 1)
          != text2.charAt(text2.length() - i - 1)) {
        return i;
      }
    }
    return n;
  }


  /**
   * Do the two texts share a substring which is at least half the length of
   * the longer text?
   * @param text1 First string
   * @param text2 Second string
   * @return Five element String array, containing the prefix of text1, the
   *     suffix of text1, the prefix of text2, the suffix of text2 and the
   *     common middle.  Or null if there was no match.
   */
  private String[] halfMatch(String text1, String text2) 
  {
    String longtext = text1.length() > text2.length() ? text1 : text2;
    String shorttext = text1.length() > text2.length() ? text2 : text1;
    if (longtext.length() < 10 || shorttext.length() < 1) {
      return null;  // Pointless.
    }

    // First check if the second quarter is the seed for a half-match.
    String[] hm1 = halfMatchI(longtext, shorttext,
                                   (longtext.length() + 3) / 4);
    // Check again based on the third quarter.
    String[] hm2 = halfMatchI(longtext, shorttext,
                                   (longtext.length() + 1) / 2);
    String[] hm;
    if (hm1 == null && hm2 == null) {
      return null;
    } else if (hm2 == null) {
      hm = hm1;
    } else if (hm1 == null) {
      hm = hm2;
    } else {
      // Both matched.  Select the longest.
      hm = hm1[4].length() > hm2[4].length() ? hm1 : hm2;
    }

    // A half-match was found, sort out the return data.
    if (text1.length() > text2.length()) {
      return hm;
      //return new String[]{hm[0], hm[1], hm[2], hm[3], hm[4]};
    } else {
      return new String[]{hm[2], hm[3], hm[0], hm[1], hm[4]};
    }
  }


  /**
   * Does a substring of shorttext exist within longtext such that the
   * substring is at least half the length of longtext?
   * @param longtext Longer string
   * @param shorttext Shorter string
   * @param i Start index of quarter length substring within longtext
   * @return Five element String array, containing the prefix of longtext, the
   *     suffix of longtext, the prefix of shorttext, the suffix of shorttext
   *     and the common middle.  Or null if there was no match.
   */
  private String[] halfMatchI(String longtext, String shorttext, int i) {
    // Start with a 1/4 length substring at position i as a seed.
    String seed = longtext.substring(i,
        i + (int) Math.floor(longtext.length() / 4));
    int j = -1;
    String best_common = "";
    String best_longtext_a = "", best_longtext_b = "";
    String best_shorttext_a = "", best_shorttext_b = "";
    while ((j = shorttext.indexOf(seed, j + 1)) != -1) {
      int prefixLength = commonPrefix(longtext.substring(i),
                                     shorttext.substring(j));
      int suffixLength = commonSuffix(longtext.substring(0, i),
                                 shorttext.substring(0, j));
      if (best_common.length() < suffixLength + prefixLength) {
        best_common = shorttext.substring(j - suffixLength, j)
            + shorttext.substring(j, j + prefixLength);
        best_longtext_a = longtext.substring(0, i - suffixLength);
        best_longtext_b = longtext.substring(i + prefixLength);
        best_shorttext_a = shorttext.substring(0, j - suffixLength);
        best_shorttext_b = shorttext.substring(j + prefixLength);
      }
    }
    if (best_common.length() >= longtext.length() / 2) {
      return new String[]{best_longtext_a, best_longtext_b,
                          best_shorttext_a, best_shorttext_b, best_common};
    } else {
      return null;
    }
  }


  /**
   * Reduce the number of edits by eliminating semantically trivial equalities.
   * @param diffs LinkedList of Diff objects
   */
  public void cleanupSemantic(LinkedList diffs) 
  {
    if (diffs.isEmpty()) 
    {
      return;
    }
    boolean changes = false;
    Stack equalities = new Stack();  // Stack of qualities.
    String lastequality = null; // Always equal to equalities.lastElement().text
    ListIterator pointer = diffs.listIterator();
    // Number of characters that changed prior to the equality.
    int length_changes1 = 0;
    // Number of characters that changed after the equality.
    int length_changes2 = 0;
    Diff thisDiff = (Diff)pointer.next();
    while (thisDiff != null) {
      if (thisDiff.operation == OPERATION_EQUAL) {
        // equality found
        equalities.push(thisDiff);
        length_changes1 = length_changes2;
        length_changes2 = 0;
        lastequality = thisDiff.text;
      } else {
        // an insertion or deletion
        length_changes2 += thisDiff.text.length();
        if (lastequality != null && (lastequality.length() <= length_changes1)
            && (lastequality.length() <= length_changes2)) {
          //System.out.println("Splitting: '" + lastequality + "'");
          // Walk back to offending equality.
          while (thisDiff != equalities.lastElement()) {
            thisDiff = (Diff)pointer.previous();
          }
          pointer.next();

          // Replace equality with a delete.
          pointer.set(new Diff(OPERATION_DELETE, lastequality));
          // Insert a corresponding an insert.
          pointer.add(new Diff(OPERATION_INSERT, lastequality));

          equalities.pop();  // Throw away the equality we just deleted.
          if (!equalities.empty()) {
            // Throw away the previous equality (it needs to be reevaluated).
            equalities.pop();
          }
          if (equalities.empty()) {
            // There are no previous equalities, walk back to the start.
            while (pointer.hasPrevious()) {
              pointer.previous();
            }
          } else {
            // There is a safe equality we can fall back to.
            thisDiff = (Diff)equalities.lastElement();
            while (thisDiff != pointer.previous()) {
              // Intentionally empty loop.
            }
          }

          length_changes1 = 0;  // Reset the counters.
          length_changes2 = 0;
          lastequality = null;
          changes = true;
        }
      }
      thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    }

    if (changes) {
      cleanupMerge(diffs);
    }
    cleanupSemanticLossless(diffs);
  }


  /**
   * Look for single edits surrounded on both sides by equalities
   * which can be shifted sideways to align the edit to a word boundary.
   * e.g: The c<ins>at c</ins>ame. -> The <ins>cat </ins>came.
   * @param diffs LinkedList of Diff objects
   */
  public void cleanupSemanticLossless(LinkedList diffs) {
    String equality1, edit, equality2;
    String commonString;
    int commonOffset;
    int score, bestScore;
    String bestEquality1, bestEdit, bestEquality2;
    // Create a new iterator at the start.
    ListIterator pointer = diffs.listIterator();
    Diff prevDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    Diff thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    Diff nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    // Intentionally ignore the first and last element (don't need checking).
    while (nextDiff != null) {
      if (prevDiff.operation == OPERATION_EQUAL &&
          nextDiff.operation == OPERATION_EQUAL) {
        // This is a single edit surrounded by equalities.
        equality1 = prevDiff.text;
        edit = thisDiff.text;
        equality2 = nextDiff.text;

        // First, shift the edit as far left as possible.
        commonOffset = commonSuffix(equality1, edit);
        if (commonOffset != 0) {
          commonString = edit.substring(edit.length() - commonOffset);
          equality1 = equality1.substring(0, equality1.length() - commonOffset);
          edit = commonString + edit.substring(0, edit.length() - commonOffset);
          equality2 = commonString + equality2;
        }

        // Second, step character by character right, looking for the best fit.
        bestEquality1 = equality1;
        bestEdit = edit;
        bestEquality2 = equality2;
        bestScore = cleanupSemanticScore(equality1, edit)
            + cleanupSemanticScore(edit, equality2);
        while (edit.length() != 0 && equality2.length() != 0
            && edit.charAt(0) == equality2.charAt(0)) {
          equality1 += edit.charAt(0);
          edit = edit.substring(1) + equality2.charAt(0);
          equality2 = equality2.substring(1);
          score = cleanupSemanticScore(equality1, edit)
              + cleanupSemanticScore(edit, equality2);
          // The >= encourages trailing rather than leading whitespace on edits.
          if (score >= bestScore) {
            bestScore = score;
            bestEquality1 = equality1;
            bestEdit = edit;
            bestEquality2 = equality2;
          }
        }

        if (!prevDiff.text.equals(bestEquality1)) {
          // We have an improvement, save it back to the diff.
          if (bestEquality1.length() != 0) {
            prevDiff.text = bestEquality1;
          } else {
            pointer.previous(); // Walk past nextDiff.
            pointer.previous(); // Walk past thisDiff.
            pointer.previous(); // Walk past prevDiff.
            pointer.remove(); // Delete prevDiff.
            pointer.next(); // Walk past thisDiff.
            pointer.next(); // Walk past nextDiff.
          }
          thisDiff.text = bestEdit;
          if (bestEquality2.length() != 0) {
            nextDiff.text = bestEquality2;
          } else {
            pointer.remove(); // Delete nextDiff.
            nextDiff = thisDiff;
            thisDiff = prevDiff;
          }
        }
      }
      prevDiff = thisDiff;
      thisDiff = nextDiff;
      nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    }
  }


  /**
   * Given two strings, compute a score representing whether the internal
   * boundary falls on logical boundaries.
   * Scores range from 5 (best) to 0 (worst).
   * @param one First string
   * @param two Second string
   * @return The score.
   */
  private int cleanupSemanticScore(String one, String two) 
  {
    if (one.length() == 0 || two.length() == 0) {
      // Edges are the best.
      return 5;
    }

    // Each port of this function behaves slightly differently due to
    // subtle differences in each language's definition of things like
    // 'whitespace'.  Since this function's purpose is largely cosmetic,
    // the choice has been made to use each language's native features
    // rather than force total conformity.
    int score = 0;
    // One point for non-alphanumeric.
    if (!Character.isLetterOrDigit(one.charAt(one.length() - 1))
        || !Character.isLetterOrDigit(two.charAt(0))) {
      score++;
      // Two points for whitespace.
      if (Character.isWhitespace(one.charAt(one.length() - 1))
          || Character.isWhitespace(two.charAt(0))) {
        score++;
        // Three points for line breaks.
        if (Character.getType(one.charAt(one.length() - 1)) == Character.CONTROL
            || Character.getType(two.charAt(0)) == Character.CONTROL) {
          score++;
          // Four points for blank lines.
          if (BLANKLINEEND.matcher(one).find()
              || BLANKLINESTART.matcher(two).find()) {
            score++;
          }
        }
      }
    }
    return score;
  }


  private Pattern BLANKLINEEND
      = Pattern.compile("\\n\\r?\\n\\Z", Pattern.DOTALL);
  private Pattern BLANKLINESTART
      = Pattern.compile("\\A\\r?\\n\\r?\\n", Pattern.DOTALL);

  
  /**
   * Reduce the number of edits by eliminating operationally trivial equalities.
   * @param diffs LinkedList of Diff objects
   */
  public void cleanupEfficiency(LinkedList diffs) {
    if (diffs.isEmpty()) {
      return;
    }
    
    boolean changes = false;
    Stack equalities = new Stack();  // Stack of equalities.
    String lastequality = null; // Always equal to equalities.lastElement().text
    ListIterator pointer = diffs.listIterator();
    // Is there an insertion operation before the last equality.
    boolean pre_ins = false;
    // Is there a deletion operation before the last equality.
    boolean pre_del = false;
    // Is there an insertion operation after the last equality.
    boolean post_ins = false;
    // Is there a deletion operation after the last equality.
    boolean post_del = false;
    Diff thisDiff = (Diff)pointer.next();
    Diff safeDiff = thisDiff;  // The last Diff that is known to be unsplitable.
    while (thisDiff != null) {
      if (thisDiff.operation == OPERATION_EQUAL) {
        // equality found
        if (thisDiff.text.length() < EditCost && (post_ins || post_del)) {
          // Candidate found.
          equalities.push(thisDiff);
          pre_ins = post_ins;
          pre_del = post_del;
          lastequality = thisDiff.text;
        } else {
          // Not a candidate, and can never become one.
          equalities.clear();
          lastequality = null;
          safeDiff = thisDiff;
        }
        post_ins = post_del = false;
      } else {
        // an insertion or deletion
        if (thisDiff.operation == OPERATION_DELETE) {
          post_del = true;
        } else {
          post_ins = true;
        }
        /*
         * Five types to be split:
         * <ins>A</ins><del>B</del>XY<ins>C</ins><del>D</del>
         * <ins>A</ins>X<ins>C</ins><del>D</del>
         * <ins>A</ins><del>B</del>X<ins>C</ins>
         * <ins>A</del>X<ins>C</ins><del>D</del>
         * <ins>A</ins><del>B</del>X<del>C</del>
         */
        if (lastequality != null
            && ((pre_ins && pre_del && post_ins && post_del)
                || ((lastequality.length() < EditCost / 2)
                    && ((pre_ins ? 1 : 0) + (pre_del ? 1 : 0)
                        + (post_ins ? 1 : 0) + (post_del ? 1 : 0)) == 3))) {
          //System.out.println("Splitting: '" + lastequality + "'");
          // Walk back to offending equality.
          while (thisDiff != equalities.lastElement()) {
            thisDiff = (Diff)pointer.previous();
          }
          pointer.next();

          // Replace equality with a delete.
          pointer.set(new Diff(OPERATION_DELETE, lastequality));
          // Insert a corresponding an insert.
          pointer.add(thisDiff = new Diff(OPERATION_INSERT, lastequality));

          equalities.pop();  // Throw away the equality we just deleted.
          lastequality = null;
          if (pre_ins && pre_del) {
            // No changes made which could affect previous entry, keep going.
            post_ins = post_del = true;
            equalities.clear();
            safeDiff = thisDiff;
          } else {
            if (!equalities.empty()) {
              // Throw away the previous equality (it needs to be reevaluated).
              equalities.pop();
            }
            if (equalities.empty()) {
              // There are no previous questionable equalities,
              // walk back to the last known safe diff.
              thisDiff = safeDiff;
            } else {
              // There is an equality we can fall back to.
              thisDiff = (Diff)equalities.lastElement();
            }
            while (thisDiff != pointer.previous()) {
              // Intentionally empty loop.
            }
            post_ins = post_del = false;
          }

          changes = true;
        }
      }
      thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    }

    if (changes) {
      cleanupMerge(diffs);
    }
  }


  /**
   * Reorder and merge like edit sections.  Merge equalities.
   * Any edit section can move as long as it doesn't cross an equality.
   * @param diffs LinkedList of Diff objects
   */
  public void cleanupMerge(LinkedList diffs) {
    diffs.add(new Diff(OPERATION_EQUAL, ""));  // Add a dummy entry at the end.
    ListIterator pointer = diffs.listIterator();
    int count_delete = 0;
    int count_insert = 0;
    String text_delete = "";
    String text_insert = "";
    Diff thisDiff = (Diff)pointer.next();
    Diff prevEqual = null;
    int commonlength;
    while (thisDiff != null) {
      switch (thisDiff.operation) {
      case OPERATION_INSERT:
        count_insert++;
        text_insert += thisDiff.text;
        prevEqual = null;
        break;
      case OPERATION_DELETE:
        count_delete++;
        text_delete += thisDiff.text;
        prevEqual = null;
        break;
      case OPERATION_EQUAL:
        if (count_delete != 0 || count_insert != 0) {
          // Delete the offending records.
          pointer.previous();  // Reverse direction.
          while (count_delete-- > 0) {
            pointer.previous();
            pointer.remove();
          }
          while (count_insert-- > 0) {
            pointer.previous();
            pointer.remove();
          }
          if (count_delete != 0 && count_insert != 0) {
            // Factor out any common prefixies.
            commonlength = commonPrefix(text_insert, text_delete);
            if (commonlength != 0) {
              if (pointer.hasPrevious()) {
                thisDiff = (Diff)pointer.previous();
                thisDiff.text += text_insert.substring(0, commonlength);
                pointer.next();
              } else {
                pointer.add(new Diff(OPERATION_EQUAL,
                    text_insert.substring(0, commonlength)));
              }
              text_insert = text_insert.substring(commonlength);
              text_delete = text_delete.substring(commonlength);
            }
            // Factor out any common suffixies.
            commonlength = commonSuffix(text_insert, text_delete);
            if (commonlength != 0) {
              thisDiff = (Diff)pointer.next();
              thisDiff.text = text_insert.substring(text_insert.length()
                  - commonlength) + thisDiff.text;
              text_insert = text_insert.substring(0, text_insert.length()
                  - commonlength);
              text_delete = text_delete.substring(0, text_delete.length()
                  - commonlength);
              pointer.previous();
            }
          }
          // Insert the merged records.
          if (text_delete.length() != 0) {
            pointer.add(new Diff(OPERATION_DELETE, text_delete));
          }
          if (text_insert.length() != 0) {
            pointer.add(new Diff(OPERATION_INSERT, text_insert));
          }
          // Step forward to the equality.
          thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
        } else if (prevEqual != null) {
          // Merge this equality with the previous one.
          prevEqual.text += thisDiff.text;
          pointer.remove();
          thisDiff = (Diff) pointer.previous();
          pointer.next();  // Forward direction
        }
        count_insert = 0;
        count_delete = 0;
        text_delete = "";
        text_insert = "";
        prevEqual = thisDiff;
        break;
      }
      thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    }
    // System.out.println(diff);
    if (((Diff)diffs.getLast()).text.length() == 0) 
    {
      diffs.removeLast();  // Remove the dummy entry at the end.
    }

    /*
     * Second pass: look for single edits surrounded on both sides by equalities
     * which can be shifted sideways to eliminate an equality.
     * e.g: A<ins>BA</ins>C -> <ins>AB</ins>AC
     */
    boolean changes = false;
    // Create a new iterator at the start.
    // (As opposed to walking the current one back.)
    pointer = diffs.listIterator();
    Diff prevDiff = (Diff)( pointer.hasNext() ? pointer.next() : null);
    thisDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    Diff nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    // Intentionally ignore the first and last element (don't need checking).
    while (nextDiff != null) {
      if (prevDiff.operation == OPERATION_EQUAL &&
          nextDiff.operation == OPERATION_EQUAL) {
        // This is a single edit surrounded by equalities.
        if (thisDiff.text.endsWith(prevDiff.text)) {
          // Shift the edit over the previous equality.
          thisDiff.text = prevDiff.text
              + thisDiff.text.substring(0, thisDiff.text.length()
                                           - prevDiff.text.length());
          nextDiff.text = prevDiff.text + nextDiff.text;
          pointer.previous(); // Walk past nextDiff.
          pointer.previous(); // Walk past thisDiff.
          pointer.previous(); // Walk past prevDiff.
          pointer.remove(); // Delete prevDiff.
          pointer.next(); // Walk past thisDiff.
          thisDiff = (Diff)pointer.next(); // Walk past nextDiff.
          nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
          changes = true;
        } else if (thisDiff.text.startsWith(nextDiff.text)) {
          // Shift the edit over the next equality.
          prevDiff.text += nextDiff.text;
          thisDiff.text = thisDiff.text.substring(nextDiff.text.length())
              + nextDiff.text;
          pointer.remove(); // Delete nextDiff.
          nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
          changes = true;
        }
      }
      prevDiff = thisDiff;
      thisDiff = nextDiff;
      nextDiff = (Diff)(pointer.hasNext() ? pointer.next() : null);
    }
    // If shifts were made, the diff needs reordering and another shift sweep.
    if (changes) {
      cleanupMerge(diffs);
    }
  }


  /**
   * Convert a Diff list into a pretty HTML report.
   * @param diffs LinkedList of Diff objects
   * @return HTML representation
   */
  public String toHtml(LinkedList diffs) {
    StringBuffer html = new StringBuffer();
    int i = 0;
    Iterator diffIter = diffs.iterator();
    while(diffIter.hasNext())
    {
      Diff aDiff =(Diff)diffIter.next();
//      String text = aDiff.text.replace("&", "&amp;").replace("<", "&lt;")
//      .replace(">", "&gt;").replace("\n", "<BR>");
      String text = StringUtil.replace(aDiff.text, "&", "&amp;");
      text = StringUtil.replace(text, "<", "&lt;");
      text = StringUtil.replace(text, ">", "&gt;");
      text = StringUtil.replace(text, "\n", "<BR>");
      switch (aDiff.operation) {
      case OPERATION_INSERT:
        html.append("<INS STYLE=\"background:#E6FFE6;\" TITLE=\"i=" + i + "\">"
            + text + "</INS>");
        break;
      case OPERATION_DELETE:
        html.append("<DEL STYLE=\"background:#FFE6E6;\" TITLE=\"i=" + i + "\">"
            + text + "</DEL>");
        break;
      case OPERATION_EQUAL:
        html.append("<SPAN TITLE=\"i=" + i + "\">" + text + "</SPAN>");
        break;
      }
      if (aDiff.operation != OPERATION_DELETE) {
        i += aDiff.text.length();
      }
    }
    return html.toString();
  }

  /**
   * Compute and return the score for a match with e errors and x location.
   * @param e Number of errors in match
   * @param x Location of match
   * @param loc Expected location of match
   * @param score_text_length Coerced version of text's length
   * @param pattern Pattern being sought
   * @return Overall score for match
   */
  private double match_bitapScore(int e, int x, int loc, int score_text_length, String pattern) 
  {
    int d = Math.abs(loc - x);
    return (e / (float) pattern.length() / Match_Balance)
        + (d / (float) score_text_length / (1.0 - Match_Balance));
  }


  /**
   * Class representing one diff OPERATION_
   */
  public static class Diff 
  {
    public int operation;
    // One of: INSERT, DELETE or EQUAL.
    public String text;
    // The text associated with this diff OPERATION_

    /**
     * Constructor.  Initializes the diff with the provided values.
     * @param operation One of INSERT, DELETE or EQUAL
     * @param text The text being applied
     */
    public Diff(int operation, String text) {
      // Construct a diff with the specified operation and text.
      this.operation = operation;
      this.text = text;
    }

    /**
     * Display a human-readable version of this Diff.
     * @return text version
     */
    public String toString() {
      String prettyText = this.text.replace('\n', '\u00b6');
      return "Diff(" + this.operation + ",\"" + prettyText + "\")";
    }

    /**
     * Is this Diff equivalent to another Diff?
     * @param d Another Diff to compare against
     * @return true or false
     */
    public boolean equals(Object d) {
      try {
        return (((Diff) d).operation == this.operation)
               && (((Diff) d).text.equals(this.text));
      } catch (ClassCastException e) {
        return false;
      }
    }
  }
  
  
  public static void main(String[] args)
  {
    Diff3 diff= new Diff3();
    LinkedList result = diff.diff("Dies ist der erste Text","Dies ist der zweite Text",false);
    System.out.println(result);
    String html = diff.toHtml(result);
    System.out.println(html+"<br>");
    diff.cleanupSemantic(result);
    html = diff.toHtml(result);
    System.out.println(html+"<br>");
    diff.cleanupSemanticLossless(result);
    html = diff.toHtml(result);
    System.out.println(html+"<br>");
  }
}
