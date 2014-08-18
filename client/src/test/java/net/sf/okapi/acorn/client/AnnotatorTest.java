package net.sf.okapi.acorn.client;

import static org.junit.Assert.*;
import java.util.ArrayList;

import net.sf.okapi.acorn.calais.Annotator;
import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.MTag;
import net.sf.okapi.lib.xliff2.core.Unit;

import org.junit.Test;

public class AnnotatorTest {

	private Annotator annotaror = new Annotator();
	
	private class Info implements Annotator.IInfoSpan {

		private int start;
		private int end;
		private MTag info;
		
		public Info (int start, int end, String type) {
			this.start = start;
			this.end = end;
			this.info = new MTag("unused", type);
		}
		
		@Override
		public int getStart () {
			return start;
		}

		@Override
		public int getEnd () {
			return end;
		}

		@Override
		public MTag getInfo () {
			return info;
		}
		
	}
	
	@Test
	public void testAnnotationsOnPlainNested () {
		Fragment frag = new Unit("id").appendNewSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(6, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		spans.add(new Info(6, 11, "t:3"));
		annotaror.annotates(frag, spans);
		assertEquals("word1 <mrk id=\"1\" type=\"t:1\"><mrk id=\"2\" type=\"t:2\"><mrk id=\"3\" type=\"t:3\">word2</mrk></mrk></mrk>", frag.toXLIFF());
	}

	@Test
	public void testAnnotationsOnPlainNestedAtStart () {
		Fragment frag = new Unit("id").appendNewSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(0, 5, "t:2"));
		annotaror.annotates(frag, spans);
		assertEquals("<mrk id=\"1\" type=\"t:1\"><mrk id=\"2\" type=\"t:2\">word1</mrk> word2</mrk>", frag.toXLIFF());
	}

	@Test
	public void testAnnotationsOnPlainNestedAtEnd () {
		Fragment frag = new Unit("id").appendNewSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		annotaror.annotates(frag, spans);
		assertEquals("<mrk id=\"2\" type=\"t:1\">word1 <mrk id=\"1\" type=\"t:2\">word2</mrk></mrk>", frag.toXLIFF());
	}

	@Test
	public void testAnnotationsOnPlainNestedAtStartAndEnd () {
		Fragment frag = new Unit("id").appendNewSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		spans.add(new Info(0, 5, "t:3"));
		annotaror.annotates(frag, spans);
		assertEquals("<mrk id=\"2\" type=\"t:1\"><mrk id=\"3\" type=\"t:3\">word1</mrk> <mrk id=\"1\" type=\"t:2\">word2</mrk></mrk>", frag.toXLIFF());
	}

}
