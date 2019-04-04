package com.hongnshao.toolbox.jar;

import org.junit.Test;

import com.hongshao.toolbox.jar.JarPacker;

public class JarPackerTest {
	
	@Test
	public void compileJava() {
		String[] classpaths = {};
		try {
			String result = JarPacker.compileJava("D:\\workspace","com.hongshao.test.HelloWorld",classpaths);
//			String result = JarPacker.compileJava("D:\\workspace\\com\\hongshao\\test\\HelloWorld.java",classpaths,"D:\\workspace");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void packClass() {
		String[] classpaths = {};
		try {
			String result = JarPacker.packClass("D:\\workspace","com.hongshao.test.HelloWorld",classpaths);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
