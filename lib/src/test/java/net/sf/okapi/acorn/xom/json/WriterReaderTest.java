package net.sf.okapi.acorn.xom.json;

import static org.junit.Assert.assertEquals;
import net.sf.okapi.acorn.xom.Factory;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
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
		IContent cont = xf.createContent(xf.createUnit("u1").getStore(), false);
		ICTag ctag1 = cont.openCodeSpan("id1", "data");
		
	}
}
