package net.sf.okapi.acorn.xom;

import java.io.StringWriter;

import org.oasisopen.xliff.om.v1.ICode;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonWriter {
	
	private ObjectMapper mapper;
	
	public JacksonWriter () {
		mapper = new ObjectMapper();
	}
	
	public String writeCode (StringWriter writer,
		ICode code)
	{
		if ( writer == null ) writer = new StringWriter();
		try {
			if ( code instanceof StartCode )
			mapper.writeValue(writer, (StartCode)code);
		}
		catch ( Throwable e ) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
}
