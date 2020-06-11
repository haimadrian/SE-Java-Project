package org.spa.view.control;

/**
 * @author Haim Adrian
 * @since 13-May-20
 */

import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * A class that makes using the GridBagLayout layout manager easier. It extends GridBagConstraints and
 * provides utility methods that do things like set the constraints to common values for use with labels and
 * other components, incrementing the line and column numbers , etc. All the methods in this class return this
 * so that the method calls can be chained together.
 *
 * @author Haim Adrian
 * @since 12-May-20
 */
public class CustomGridBagConstraints extends GridBagConstraints {
   public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
   private static final Logger logger = LoggerFactory.getLogger(CustomGridBagConstraints.class);

   /**
    * Create a {@link CustomGridBagConstraints} object that has: gridx set to 0 <br>
    * gridy set to 0 <br>
    * gridwidth set to 1 <br>
    * gridheight set to 1 <br>
    * weightx set to 1 <br>
    * weighty set to 1 <br>
    * anchor set to GridBagConstraints.NORTHWEST <br>
    * fill set to GridBagConstraints.NONE <br>
    * insets set to new Insets(0,0,0,0) <br>
    * ipadx set to 0 <br>
    * ipady set to 0 <br>
    */
   public CustomGridBagConstraints() {
      super(0, // gridx,
            0, // gridy,
            1, // gridwidth,
            1, // gridheight,
            1, // weightx,
            1, // weighty,
            GridBagConstraints.NORTHWEST, // anchor,
            GridBagConstraints.NONE, // fill,
            new Insets(2, 2, 2, 2), // insets,
            0, // ipadx,
            0); // ipady
   }

   /**
    * Create a CustomGridBagConstraints object that has: gridx set to 0 <br>
    * gridy set to 0 <br>
    * gridwidth set to 1 <br>
    * gridheight set to 1 <br>
    * weightx set to 1 <br>
    * weighty set to 1 <br>
    * anchor set to GridBagConstraints.NORTHWEST <br>
    * fill set to GridBagConstraints.NONE <br>
    * insets set to the given insets <br>
    * ipadx set to 0 <br>
    * ipady set to 0 <br>
    *
    * @param pInsets The insets to use
    */
   public CustomGridBagConstraints(Insets pInsets) {
      super(0, // gridx,
            0, // gridy,
            1, // gridwidth,
            1, // gridheight,
            1, // weightx,
            1, // weighty,
            GridBagConstraints.NORTHWEST, // anchor,
            GridBagConstraints.NONE, // fill,
            pInsets, // insets,
            0, // ipadx,
            0); // ipady
   }

   /**
    * Create a CustomGridBagConstraints object that has: gridx set to 0 <br>
    * gridy set to 0 <br>
    * gridwidth set to 1 <br>
    * gridheight set to 1 <br>
    * weightx set to 1 <br>
    * weighty set to 1 <br>
    * anchor set to GridBagConstraints.NORTHWEST <br>
    * fill set to GridBagConstraints.NONE <br>
    * insets set to the given insets <br>
    * ipadx set to 0 <br>
    * ipady set to 0 <br>
    *
    * @param pInsets The insets to use
    */
   public CustomGridBagConstraints(String pInsets) {
      super(0, // gridx,
            0, // gridy,
            1, // gridwidth,
            1, // gridheight,
            1, // weightx,
            1, // weighty,
            GridBagConstraints.NORTHWEST, // anchor,
            GridBagConstraints.NONE, // fill,
            new Insets(1, 1, 1, 1), 0, // ipadx,
            0); // ipady
      setInsets(pInsets);
   }

   /**
    * Advance gridx by 1
    *
    * @return this
    */
   public CustomGridBagConstraints nextX() {
      gridx++;
      return this;
   }

   /**
    * Advance gridy by 1, set gridx to zero
    * first had.
    *
    * @return this
    */
   public CustomGridBagConstraints nextY() {
      gridx = 0;
      gridy++;
      return this;
   }

   /**
    * Set the constraints to those typically used by a label that is left aligned. i.e anchor to
    * GridBagConstraints.WEST, fill to GridBagConstraints.NONE, weightx to 1 gridwidth to 1, gridheight to 1,
    *
    * @return this
    */
   public CustomGridBagConstraints constrainLabel() {
      anchor = GridBagConstraints.NORTH;
      fill = GridBagConstraints.NONE;
      weightx = 1;
      weighty = 1;
      gridwidth = 1;
      gridheight = 1;
      return this;
   }

   /**
    * Set the constraints to those typically used by a label that is aligned to the top. i.e anchor to
    * GridBagConstraints.WEST, fill to GridBagConstraints.NONE, weightx to 1 gridwidth to 1, gridheight to 1,
    *
    * @return this
    */
   public CustomGridBagConstraints constrainLabelTop() {
      constrainLabel();
      anchor = GridBagConstraints.NORTHWEST;
      return this;
   }

   /**
    * Set the constraints to those typically used by a label that is right aligned. i.e anchor to
    * GridBagConstraints.EAST, fill to GridBagConstraints.NONE, weightx to 1 gridwidth to 1, gridheight to 1,
    *
    * @return this
    */
   public CustomGridBagConstraints constrainLabelRight() {
      anchor = GridBagConstraints.EAST;
      fill = GridBagConstraints.NONE;
      weightx = 1;
      weighty = 1;
      gridwidth = 1;
      gridheight = 1;
      return this;
   }

   /**
    * Set the constraints to those typically used by component other that a label. i.e anchor to
    * GridBagConstraints.WEST, fill to GridBagConstraints.HORIZONTAL, weightx to 100 gridwidth to 1,
    * gridheight to 1
    *
    * @return this
    */
   public CustomGridBagConstraints constrainCompHoriz() {
      anchor = GridBagConstraints.WEST;
      fill = GridBagConstraints.HORIZONTAL;
      weightx = 100;
      weighty = 1;
      gridwidth = 1;
      gridheight = 1;
      return this;
   }

   /**
    * Set the constraints to those typically used by a horizontal 'filler' panel i.e. anchor to
    * GridBagConstraints.WEST, fill to GridBagConstraints.HORIZONTAL, weightx to 1000 gridwidth to 1,
    * gridheight to 1
    *
    * @return this
    */
   public CustomGridBagConstraints constrainFillHoriz() {
      anchor = GridBagConstraints.WEST;
      fill = GridBagConstraints.HORIZONTAL;
      weightx = 1000;
      weighty = 1;
      gridwidth = 1;
      gridheight = 1;
      return this;
   }

   /**
    * Set the constraints to those typically used by a horizontal & vertical 'filler' panel i.e. anchor to
    * GridBagConstraints.NORTHWEST, fill to GridBagConstraints.BOTH, weightx to 1000, weighty to 1000
    * gridwidth to 1, gridheight to 1
    *
    * @return this
    */
   public CustomGridBagConstraints constrainFillBoth() {
      anchor = GridBagConstraints.NORTHWEST;
      fill = GridBagConstraints.BOTH;
      weightx = 1000;
      weighty = 1000;
      gridwidth = 1;
      gridheight = 1;
      return this;
   }

   /**
    * Change the anchor to GridBagConstraints.WEST
    *
    * @return this
    */
   public CustomGridBagConstraints setAnchorWest() {
      anchor = GridBagConstraints.WEST;
      return this;
   }

   /**
    * Set the grid width to the given value
    *
    * @param pGridwidth The new grid width
    * @return this
    */
   public CustomGridBagConstraints setGridWidth(int pGridwidth) {
      gridwidth = pGridwidth;
      return this;
   }

   /**
    * Set the grid width to the given value
    *
    * @param pGridheight The new grid height
    * @return this
    */
   public CustomGridBagConstraints setGridHeight(int pGridheight) {
      gridheight = pGridheight;
      return this;
   }

   /**
    * Set the weightx to the given value
    *
    * @param pWeightx
    * @return this
    */
   public CustomGridBagConstraints setWeightx(int pWeightx) {
      weightx = pWeightx;
      return this;
   }

   /**
    * Set the weighty to the given value
    *
    * @param pWeighty
    * @return this
    */
   public CustomGridBagConstraints setWeighty(int pWeighty) {
      weighty = pWeighty;
      return this;
   }

   /**
    * Sets both weightx & weighty to the given value
    *
    * @param weight
    * @return this
    */
   public CustomGridBagConstraints setWeight(int weight) {
      weightx = weight;
      weighty = weightx;
      return this;
   }

   /**
    * Set the anchor
    *
    * @param pAnchor
    * @return this
    */
   public CustomGridBagConstraints setAnchor(int pAnchor) {
      anchor = pAnchor;
      return this;
   }

   /**
    * Set the insets for the component to the same value in all directions
    *
    * @param value
    * @return this
    */
   public CustomGridBagConstraints setInsets(int value) {
      insets = new Insets(value, value, value, value);
      return this;
   }

   /**
    * Set the insets for the constraint
    *
    * @param top
    * @param left
    * @param bottom
    * @param right
    * @return this
    */
   public CustomGridBagConstraints setInsets(int top, int left, int bottom, int right) {
      insets = new Insets(top, left, bottom, right);
      return this;
   }

   /**
    * Set the Insets for the constraint
    *
    * @param pInsets
    * @return this
    */
   public CustomGridBagConstraints setInsets(Insets pInsets) {
      insets = pInsets;
      return this;
   }

   /**
    * Add a blank panel to the given Container that fills the remainder of a row of components
    *
    * @param toContainer The container to add the filler to
    * @return this
    */
   public CustomGridBagConstraints addXFiller(Container toContainer) {
      JPanel filler = new JPanel();
      toContainer.add(filler, nextX().constrainFillHoriz());
      return this;
   }

   /**
    * Add a blank panel to the given Container that fills the remainder of a container of components
    *
    * @param toContainer The container to add the filler to
    * @param gridwidth The width the filler should span
    * @return this
    */
   public CustomGridBagConstraints addXYFiller(Container toContainer, int gridwidth) {
      JPanel filler = new JPanel();
      toContainer.add(filler, nextY().constrainFillBoth().setGridWidth(gridwidth).setWeightx(100).setWeighty(100));
      return this;
   }

   /**
    * Set the insets based on a string that has the format T=top-value, L=left-value, B=bottom-value, R=right-value
    * where the right-hand side of each comma delimited token is a number. The directions are optional and when a
    * direction is not present the existing inset for that direction is preserved.
    *
    * @param insetString The insets in the format T=nn,L=nn,B=nn,R=nn. If the format is incorrect then an error is
    * logged and the insets are not set.
    * @return this
    */
   public CustomGridBagConstraints setInsets(String insetString) {
      try {
         int[] directionValue = new int[4];
         directionValue[DIRECTION.T.ordinal()] = insets.top;
         directionValue[DIRECTION.L.ordinal()] = insets.left;
         directionValue[DIRECTION.B.ordinal()] = insets.bottom;
         directionValue[DIRECTION.R.ordinal()] = insets.right;

         boolean[] hasDirection = new boolean[4];

         StringTokenizer stringTokenizer = new StringTokenizer(insetString, ",");
         while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            if (token.length() < 3) {
               throw new Exception("Invalid inset format in '" + insetString + "'");
            }

            String directionString = token.substring(0, 1).toUpperCase();
            DIRECTION direction = DIRECTION.T;

            boolean ok = false;
            for (int di = 0; di < 4; di++) {
               if (directionString.equals(DIRECTION.values()[di].toString())) {
                  direction = DIRECTION.values()[di];
                  ok = true;
                  if (hasDirection[di]) {
                     throw new Exception("The direction " + direction + " is defined more than once in '" + insetString + "'");
                  }
                  hasDirection[di] = true;
                  break;
               }
            }

            if (!ok) {
               throw new Exception("Invalid inset format in '" + insetString + "'");
            }

            if (token.charAt(1) != '=') {
               throw new Exception("Invalid inset format in '" + insetString + "'");
            }

            try {
               directionValue[direction.ordinal()] = Integer.parseInt(token.substring(2));
            } catch (NumberFormatException e) {
               throw new Exception("Invalid inset format in '" + insetString + "'");
            }

         }

         insets = new Insets(directionValue[DIRECTION.T.ordinal()], directionValue[DIRECTION.L.ordinal()], directionValue[DIRECTION.B.ordinal()], directionValue[DIRECTION.R.ordinal()]);
      } catch (Exception exception) {
         logger.error("Error setting insets", exception);
      }

      return this;
   }

   /**
    * An enum that represents the directions in an Inset. This must remain in the order defined here
    */
   enum DIRECTION {
      T, L, B, R
   }
}
