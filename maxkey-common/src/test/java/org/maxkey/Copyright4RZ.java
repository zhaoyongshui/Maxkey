/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top ]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maxkey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
 
/**
 * 给java文件批量添加License信息.
 * @author MaxKey Copyright Adder
 *
 */
public class Copyright4RZ {   
    // 存放java文件的文件夹,必须是文件夹
    private static String srcFolder = "D:\\MaxKey\\Workspaces\\maxkey\\MaxKey\\maxkey-webs\\maxkey-web-mgt";
  
    //已添加标识
    private static String copyRightText = "http://www.apache.org/licenses/LICENSE-2.0";
    //扫描目录
    private String folder;
    //待添加所以文件统计
    private long fileCount = 0;
    //添加的问题就统计
    private long copyRightFileCount = 0;
    private static String lineSeperator = System.getProperty("line.separator");
    private static String encode = "UTF-8";
    private static OutputStreamWriter writer;
    
    static {
        try {
            writer = new OutputStreamWriter(new FileOutputStream("D:/MaxKey/code.txt"), encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Copyright.
     * @param folder java文件夹.
     * @param copyRight 版权内容.
     */
    public Copyright4RZ(String folder, String copyRight) {
        this.folder = folder;
    }
    
    /**
     * main .
     * @param args String
     * @throws IOException  IOException
     */
    public static void main(String[] args) throws IOException {
        // 从文件读取版权内容
        // 在D盘创建一个copyright.txt文件,把版权内容放进去即可
        String copyright = readCopyrightFromFile(
                Copyright4RZ.class.getResource("copyright.txt").getFile());        
        new Copyright4RZ(srcFolder, copyright).process();
        
        writer.close();
    }
    
    /**
     * process.
     * @throws IOException not
     */
    public void process() throws IOException {
        this.addCopyright(new File(folder));
        System.out.println("fileCount " + fileCount);
        System.out.println("copyRightFileCount " + copyRightFileCount);
    }
 
    private void addCopyright(File folder) throws IOException {
        File[] files = folder.listFiles();
 
        if (files == null || files.length == 0) {
            return;
        }
 
        for (File f : files) {
            if (f.isFile()) {
                doAddCopyright(f);
            } else {
                addCopyright(f);
            }
        }
    }
 
    private void doAddCopyright(File file) throws IOException {
        String fileName = file.getName();
        boolean isJavaFile = fileName.toLowerCase().endsWith(".java");
        //boolean isJavaFile = fileName.toLowerCase().endsWith(".ftl");
        this.fileCount++;
        if (isJavaFile) {
            copyRightFileCount++;
            System.out.println(file.getAbsolutePath());
            try {
                this.doWrite(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void doWrite(File file) throws IOException {
        StringBuilder javaFileContent = new StringBuilder();
        String line = null;
        boolean isAddCopyrightFile = isAddCopyrightFile(file.getAbsolutePath());
        // 先添加copyright到文件头
        //javaFileContent.append(copyRight).append(lineSeperator);
        // 追加剩余内容
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), encode));
     
        int i=0;
        while ((line = br.readLine()) != null) {
            if(isAddCopyrightFile && i< 16) {
                i++;
                continue;
            }
            if(line.equals("")
                    ||line.replaceAll(" ", "").equals("")
                    ||line.replaceAll("\t", "").equals("")
                    ) {
                
            }else {
                javaFileContent.append(line).append(lineSeperator);
            }
        }  
        
        //OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), encode);
        writer.write(javaFileContent.toString());
       
        br.close();  
    }
    
    private static String readCopyrightFromFile(String copyFilePath) throws IOException {
        StringBuilder copyright = new StringBuilder();
        
        String line = null;
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(copyFilePath), encode));
       
        while ((line = br.readLine()) != null) {
            copyright.append(line).append(lineSeperator);
        }
        br.close();
        
        return copyright.toString();
    }
    
    private static boolean isAddCopyrightFile(String filePath) throws IOException {
        boolean isAddCopyright = false;
        String line = null;
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), encode));
       
        while ((line = br.readLine()) != null) {
            if (line.indexOf(copyRightText) > -1) {
                isAddCopyright = true;
                break;
            }
        }
        br.close();
        
        return isAddCopyright;
    }
 
}