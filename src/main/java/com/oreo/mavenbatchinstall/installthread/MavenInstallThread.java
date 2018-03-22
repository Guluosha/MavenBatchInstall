package com.oreo.mavenbatchinstall.installthread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CopyRight (C),YLINK-深圳雁联计算有限公司
 *
 * @author 李沛恒(QQ ： 单曲循环)
 * @date 2018/1/12 ~ 下午 11:19
 */
@Component
public class MavenInstallThread implements Runnable {

	private static final String POSTFIX = ".jar";
	@Value(value = "${uploadURL}")
	private String UPLOADURL;
	@Value(value = "${repositoryID}")
	private String REPOSITORYID;
	@Value(value = "${groupId}")
	private String GROUPID;

	private static Runtime RUNTIME;
	private File jarFile;

	public MavenInstallThread() {
	}

	public MavenInstallThread(File jarFile) {
		this.jarFile = jarFile;
	}

	@Override
	public void run() {
		String jarFileName = jarFile.getName();
		StringBuilder mvnInstallString = new StringBuilder();
		RUNTIME = Runtime.getRuntime();
		String parentPath = jarFile.getParent();
		File currentDirectory = new File(parentPath);
		String readLine;
		Process process;
		if (jarFile.isFile() && jarFileName.endsWith(POSTFIX)) {
			String jarFileArtifactId = getJarFileGroupID(jarFileName);
			String jarFileVersion = getJarFileVersion(jarFileName);
			mvnInstallString
					.append("cmd /c dir &&")
					.append(" mvn install:install-file")
//					.append(" mvn deploy:deploy-file")
					.append(" -Dfile=").append(jarFileName)
					.append(" -DgroupId=").append(GROUPID)
					.append(" -DartifactId=").append(jarFileArtifactId)
					.append(" -Dversion=").append(jarFileVersion)
					.append(" -Dpackaging=jar");
//					.append(" -Durl=").append(UPLOADURL)
//					.append(" -DrepositoryId=").append(REPOSITORYID);
			String executedString = mvnInstallString.toString();
			System.out.println(executedString);
			try {
				process = MavenInstallThread.RUNTIME.exec(executedString, null, currentDirectory);
				InputStream inputStream = process.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				readLine = bufferedReader.readLine();
				while (readLine != null) {
					readLine = bufferedReader.readLine();
					System.out.println(readLine);
				}
				mvnInstallString.setLength(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + "成功安装" + jarFileName);
	}

	private String getJarFileGroupID(String jarFileName) {
		String firstPattern = "\\S+-\\d";
		String firstGroupId = null;
		Pattern compile = Pattern.compile(firstPattern);
		Matcher matcher = compile.matcher(jarFileName);
		if (matcher.find()) {
			firstGroupId = matcher.group();
		}
		if (firstGroupId != null) {
			return firstGroupId.substring(0, firstGroupId.lastIndexOf("-"));
		}
		return null;
	}

	private String getJarFileVersion(String jarFileName) {
		String jarFileVersionPattern = "\\d\\S+\\d";
		String jarFileVersion;
		Pattern compile = Pattern.compile(jarFileVersionPattern);
		Matcher matcher = compile.matcher(jarFileName);
		if (matcher.find()) {
			jarFileVersion = matcher.group();
			return jarFileVersion;
		}
		return null;
	}
}