package com.orhanobut.hawk;

import com.google.gson.Gson;

import com.orhanobut.hawk.GsonParser;
import com.orhanobut.hawk.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.fail;

public class HawkEncoderTest {

	private final Encoder encoder;
	private final Parser parser;

	public HawkEncoderTest() {
		parser = new GsonParser(new Gson());
		encoder = new HawkEncoder(parser);
	}

	static class Foo {
		String name = "hawk";
	}

	public void createInstanceWithInvalidValues() {
		try {
			new HawkEncoder(null);
			fail();
		} catch (Exception e) {
		}
	}

	public void encodeString() {
		String text = "text";
		byte[] expected = parser.toJson(text).getBytes();
		byte[] actual = encoder.encode(text);

	}

	public void encodeCustomObject() {
		Foo data = new Foo();
		byte[] expected = parser.toJson(data).getBytes();
		byte[] actual = encoder.encode(data);

	}

	public void encodeList() {
		List<String> data = new ArrayList<String>();
		data.add("test");
		byte[] expected = parser.toJson(data).getBytes();
		byte[] actual = encoder.encode(data);

	}

	public void encodeMap() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("key", "value");
		byte[] expected = parser.toJson(data).getBytes();
		byte[] actual = encoder.encode(data);

	}

	public void encodeSet() {
		Set<String> data = new HashSet<String>();
		data.add("key");
		byte[] expected = parser.toJson(data).getBytes();
		byte[] actual = encoder.encode(data);

	}

	public void decodeInvalidValues() throws Exception {
		try {
			fail();
		} catch (Exception e) {
		}
	}

	public void decodeObject() throws Exception {
		String clazz = "java.lang.String";
		String info = "00V";
		String cipher = "cipher";
		DataInfo dataInfo = DataHelper.getDataInfo(clazz + "##" + info + "@"
				+ cipher);
		String actual = encoder.decode(cipher.getBytes(), dataInfo);
	}

}
