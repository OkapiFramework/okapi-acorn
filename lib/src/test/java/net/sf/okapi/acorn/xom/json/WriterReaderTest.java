package net.sf.okapi.acorn.xom.json;

import static org.junit.Assert.assertEquals;
import net.sf.okapi.acorn.xom.Factory;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class WriterReaderTest {

	static final IXLIFFFactory xf = Factory.XOM;
	
	final private JSONWriter jw = new JSONWriter();
	final private JSONReader jr = new JSONReader();

	@Test
	public void testContent () {
		IContent cont1 = xf.createContent(xf.createUnit("u1").getStore(), false);
		cont1.append("text ");
		ICTag ctag1 = cont1.openCodeSpan("1", "<b>");
		ctag1.setCanReorder(CanReorder.FIRSTNO);
		ctag1.setCanCopy(false);
		cont1.append("bold");
		cont1.closeCodeSpan("1", "</b>");
		IUnit unit = xf.createUnit("u2");
		String jstr = jw.fromContent(cont1).toJSONString();
		System.out.println(jstr);
		IContent cont2 = jr.readContent(unit.getStore(), false, jstr);
		assertEquals(cont1.getCodedText(), cont2.getCodedText());
	}

	@Test
	public void testTag () {
		IContent cont1 = xf.createContent(xf.createUnit("u1").getStore(), false);
		ICTag ctag1 = cont1.openCodeSpan("id1", "[data]");
		ctag1.setDisp("display-text");
		ctag1.setEquiv("equiv-text");
		cont1.closeCodeSpan(ctag1.getId(), "[/data]");
		IMTag mtag1 = cont1.openMarkerSpan("m1", "generic");
		mtag1.setValue("value");
		mtag1.setRef("http://here");
		mtag1.setTranslate(false);
		cont1.closeMarkerSpan(mtag1.getId());
		
		String jstr = jw.fromContent(cont1).toJSONString();
		System.out.println(jstr);
		ISegment outSeg = xf.createLoneSegment();
		IContent cont2 = jr.readContent(outSeg.getStore(), false, jstr);
		
		ICTag ctag2 = cont2.getTags().getOpeningCTag("id1");
		assertEquals(ctag1.getData(), ctag2.getData());
		assertEquals(ctag1.getDisp(), ctag2.getDisp());
		assertEquals(ctag1.getEquiv(), ctag2.getEquiv());
		
		IMTag mtag2 = cont2.getTags().getOpeningMTag("m1");
		assertEquals(mtag1.getType(), mtag2.getType());
		assertEquals(mtag1.getTranslate(), mtag2.getTranslate());
		assertEquals(mtag1.getValue(), mtag2.getValue());
		assertEquals(mtag1.getRef(), mtag2.getRef());
	}
}
