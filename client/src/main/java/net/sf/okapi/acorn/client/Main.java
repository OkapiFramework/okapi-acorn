/*===========================================================================
  Copyright (C) 2014 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package net.sf.okapi.acorn.client;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Main {

	public static void main (String[] originalArgs) {

		// Set the look-and-feel and a larger font size
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel() {
				private static final long serialVersionUID = 1L;
				@Override
				public UIDefaults getDefaults () {
					UIDefaults ret = super.getDefaults();
					ret.put("defaultFont", new Font(Font.DIALOG, 0, 16));
					return ret;
				}
			});
		}
		catch (UnsupportedLookAndFeelException e) {
			// Use the default font
			e.printStackTrace();
		}

		MainDialog.start();
		
//		TAAS taas = new TAAS();
//		taas.handleUnit(null);
		
//		OpenCalais proc = new OpenCalais();
//		Unit unit = new Unit("id");
//		Segment seg = unit.appendSegment();
//		Fragment frag = seg.getSource();
//		frag.append("The Rotary Club in Iqaluit meets every first Monday of each month.");

		//		frag.append("It became a vital link in a trade route between the Orient, Eastern Canada and London. ");

//		frag.append("This is a test from Boulder, ");
//		frag.append(TagType.OPENING, "1", "<b>", false);
//		frag.append("Colorado");
//		frag.append(TagType.CLOSING, "1", "</b>", false);
//		frag.append(", to see if this Java program works fine.");
//		frag.appendStandalone("2", "<br>");
		
//		proc.handleUnit(new Event(EventType.TEXT_UNIT, null, unit));

	}

}
