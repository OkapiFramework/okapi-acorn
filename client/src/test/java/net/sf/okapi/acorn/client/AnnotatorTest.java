package net.sf.okapi.acorn.client;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.sf.okapi.acorn.calais.Annotator;
import net.sf.okapi.acorn.common.Util;
import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;

public class AnnotatorTest {

	private Annotator annotaror = new Annotator();
	
	private class Info implements Annotator.IInfoSpan {

		private int start;
		private int end;
		private IMTag info;
		
		public Info (int start, int end, String type) {
			this.start = start;
			this.end = end;
			this.info = Factory.XOM.createOpeningMTag("unused", type);
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
		public IMTag getInfo () {
			return info;
		}
		
	}
	
	@Test
	public void testAnnotationsOnPlainNested () {
		IContent frag = new Unit("id").appendSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(6, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		spans.add(new Info(6, 11, "t:3"));
		annotaror.annotates(frag, spans);
		assertEquals("word1 <mrk id=\"1\" type=\"t:1\"><mrk id=\"2\" type=\"t:2\"><mrk id=\"3\" type=\"t:3\">word2</mrk></mrk></mrk>", Util.fmt(frag));
	}

	@Test
	public void testAnnotationsOnCodedText () {
		IContent frag = new Unit("id").appendSegment().getSource();
		frag.appendCode("1", "[br/]");
		frag.append("word1 word2");
		//         ##word1 word2
		//         0123456789012
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(2, 11, "t:1"));
		spans.add(new Info(2, 5, "t:2"));
		annotaror.annotates(frag, spans);
		assertEquals("<C:1/><M:1><M:2>word1</M:2> word2</M:1>", Util.fmt(frag));
	}

	@Test
	public void testAnnotationsOnPlainNestedAtStart () {
		IContent frag = new Unit("id").appendSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(0, 5, "t:2"));
		annotaror.annotates(frag, spans);
		assertEquals("<M:1><M:2>word1</M:2> word2</M:1>", Util.fmt(frag));
	}

	@Test
	public void testAnnotationsOnPlainNestedAtEnd () {
		IContent frag = new Unit("id").appendSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		annotaror.annotates(frag, spans);
		assertEquals("<M:2>word1 <M:1>word2</M:1></M:2>", Util.fmt(frag));
	}

	@Test
	public void testAnnotationsOnPlainNestedAtStartAndEnd () {
		IContent frag = new Unit("id").appendSegment().getSource();
		frag.append("word1 word2");
		//           01234567890
		ArrayList<Annotator.IInfoSpan> spans = new ArrayList<>();
		spans.add(new Info(0, 11, "t:1"));
		spans.add(new Info(6, 11, "t:2"));
		spans.add(new Info(0, 5, "t:3"));
		annotaror.annotates(frag, spans);
		assertEquals("<M:2><M:3>word1</M:3> <M:1>word2</M:1></M:2>", Util.fmt(frag));
	}

}
