package com.hongshao.toolbox.jar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class JarPacker {
	
	/**
	 * 将java文件打包为jar
	 * 
	 * @param basePath 基础路径 D:\\workspace
	 * @param fullClassName 全类名 com.hongshao.test.HelloWorld
	 * @param classpaths 依赖路径
	 * @return
	 */
	public static String packJava(String basePath, String fullClassName, String[] classpaths) {
		try {
			String result1 = compileJava(basePath,fullClassName,classpaths);
			if(result1.equals("success")) {
				String result2 = packClass(basePath,fullClassName,classpaths);
				if(result2.equals("success")) {
					return "success";
				}
			}
			return "false";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";	
		} 
	}
	
	/**
	 * 编译java文件，得到class文件
	 * @param basePath
	 * @param fullClassName
	 * @param classpaths
	 * @return
	 * @throws Exception
	 */
	public static String compileJava(String basePath, String fullClassName, String[] classpaths)
			throws Exception {
		String result = "";
		List<String> fullClassNames = Arrays.asList(fullClassName.split("\\."));
		String javaFileName = fullClassNames.get(fullClassNames.size()-1);
		StringBuffer packagePrefix = new StringBuffer();
		fullClassNames.stream().limit(fullClassNames.size()-1).forEach(fullClassNameSeg -> {
			packagePrefix.append(fullClassNameSeg + File.separator);
		});
		
		StringBuffer classpathStrs = new StringBuffer();
		for (String classpath : classpaths) {
			classpathStrs = classpathStrs.append(classpath + " ");
		}
		String compileCmd = classpathStrs.length() > 0 ? "javac -classpath " + classpathStrs: "javac ";
		compileCmd = compileCmd + basePath+File.separator+packagePrefix+javaFileName+".java" + " -d " + basePath;
		System.out.println(compileCmd);
		Process proc = Runtime.getRuntime().exec(compileCmd);
		int exitVal = proc.waitFor();
		result = (exitVal == 0 ? "success" : "false");
		return result;
	}

	/**
	 * 将class文件打包为jar包
	 * 要保证打包后的包名正确，需设置如下所示
	 *
	 * String cmd = "jar -cf ./com/filesOf390/390.jar ./com/filesOf390/*.class";
	 * Runtime.geteRuntime().exec(cmd,null,new File("./com/filesOf390/../.."));
	 * 
	 * @param basePath 基础路径 D:\\workspace
	 * @param fullClassName 全类名 com.hongshao.test.HelloWorld
	 * @param classpaths 依赖路径
	 * @return
	 * @throws Exception
	 */
	public static String packClass(String basePath, String fullClassName, String[] classpaths)
			throws Exception {
		List<String> fullClassNames = Arrays.asList(fullClassName.split("\\."));
		
		String classFileName = fullClassNames.get(fullClassNames.size()-1);
		StringBuffer packagePrefix = new StringBuffer();
		fullClassNames.stream().limit(fullClassNames.size() - 1).forEach(fullClassNameSeg -> {
			packagePrefix.append(fullClassNameSeg + File.separator);
		});

		String MFPath = basePath + File.separator + packagePrefix.toString() + "MANIFEST.MF";
		createManifest(MFPath, fullClassName, classpaths);
		String cmd = "jar -cfm " + "./" + packagePrefix.toString() + classFileName + ".jar " + "./"
				+ packagePrefix.toString() + "MANIFEST.MF " + "./" + packagePrefix.toString() + classFileName
				+ ".class";
		System.out.println(cmd);
		Process proc = Runtime.getRuntime().exec(cmd, null, new File(basePath));
		int exitVal = proc.waitFor();
		String result = (exitVal == 0 ? "success" : "false");
		return result;
	}
	
	
	//创建MF文件，并写入jar包启动类和依赖文件
	private static void createManifest(String MFPath, String entryClassName, String[] classpaths) {
		File file = new File(MFPath);
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("Manifest-Version: 1.0\r\n" + "Created-By: 1.8.0_181 (Oracle Corporation)\r\n");
			writer.write("Main-Class: " + entryClassName + "\r\n");
			StringBuffer classpathStrs = new StringBuffer();
			for (String classpath : classpaths) {
				classpathStrs = classpathStrs.append(classpath + " ");
			}
			writer.write("Class-Path: " + classpathStrs.toString() + "\r\n");
			writer.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
