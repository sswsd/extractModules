package com.aosp;

import com.apple.eawt.Application;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class SingleModule
{
    private String moduleName;
    private String moduleVariant;
    private String moduleType;
    private String moduleFactory;
    private String moduleDefiled;
    private ArrayList<String> moduleSrcs;
    private ArrayList<String> moduleLinkSrcs;

    SingleModule(String moduleName)
    {
        this.moduleName = moduleName;
        this.moduleSrcs = new ArrayList<>();
        this.moduleLinkSrcs = new ArrayList<>();
    }
    public String getModuleName()
    {
        return this.moduleName;
    }
    public void setModuleVariant(String moduleVariant)
    {
        this.moduleVariant = moduleVariant;
    }
    public String getModuleVariant() {
        return moduleVariant;
    }

    public void setModuleType(String moduleType)
    {
        this.moduleType = moduleType;
    }
    public String getModuleType() {
        return moduleType;
    }
    public void setModuleFactory(String moduleFactory)
    {
        this.moduleFactory = moduleFactory;
    }

    public String getModuleFactory() {
        return moduleFactory;
    }

    public void setModuleDefiled(String moduleDefiled)
    {
        this.moduleDefiled = moduleDefiled;
    }

    public String getModuleDefiled() {
        return moduleDefiled;
    }

    public void addModuleSrc(String src)
    {
        this.moduleSrcs.add(src);
    }

    public ArrayList<String> getModuleSrcs() {
        return moduleSrcs;
    }
    public void addModuleLinkSrc(String src)
    {
        this.moduleLinkSrcs.add(src);
    }

    public ArrayList<String> getModuleLinkSrcs() {
        return moduleLinkSrcs;
    }
}
public class Modules extends JFrame{
    private static String moduleNameString = "Module:  ";
    private static String singletonNameString = "Singleton: ";
    private static String variantNameString = "Variant:";
    private static String variantNameSpecialString = "# Variant:";
    private static String typeNameString = "Type:";
    private static String typeNameSpecialString = "# Type:";
    private static String factoryNameString = "Factory:";
    private static String definedNameString = "Defined:";
    private static String propertiesPath = "aospModules.properties";
    private static String ninjaFilePrpt = "ninjaFile";
    private static String ccCompileRulePrpt = "ccCompileRule";
    private static String ccCompileRule2Prpt = "ccCompileRule2";
    private static String ccShareLinkRulePrpt = "ccShareLinkRule";
    private static String ccStaticLinkRulePrpt = "ccStaticLinkRule";
    public String ccCompileRule;
    public String ccCompileRule2;
    public String ccShareLinkRule;
    public String ccStaticLinkRule;
    private static JFrame frame;
    private File ninjaFile;
    private Modules()
    {
        MyProperties myProperties = new MyProperties(propertiesPath);
        String fileName = myProperties.getProperty(ninjaFilePrpt);
        ccCompileRule = myProperties.getProperty(ccCompileRulePrpt);
        ccCompileRule2 = myProperties.getProperty(ccCompileRule2Prpt);
        ccShareLinkRule = myProperties.getProperty(ccShareLinkRulePrpt);
        ccStaticLinkRule = myProperties.getProperty(ccStaticLinkRulePrpt);
        this.ninjaFile = new File(fileName);
    }
    public static void main(String[] args)
    {
        ImageIcon i = new ImageIcon(Modules.class.getClassLoader().getResource("logo.jpg"));
        String OsName = System.getProperty("os.name");
        if (OsName.contains("Mac")) {
            Application app = Application.getApplication();
            Image im = i.getImage();
            app.setDockIconImage(im);
            app.requestUserAttention(true);
        }

        Modules modules = new Modules();
        ArrayList<SingleModule> moduleList = modules.extractModules();

        MainForm mainForm = new MainForm(moduleList);
        frame = new JFrame("MainForm");
        frame.setContentPane(mainForm.JPanelMy);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.pack();
        frame.setLocationRelativeTo(null);

        //mainForm.initComponent(moduleList);


        frame.setIconImage(i.getImage());
        frame.setTitle("Extract Modules");
        frame.setName("TEST");
        frame.setVisible(true);
    }
    public ArrayList<SingleModule> extractModules()
    {
        //ArrayList<String> moduleList = new ArrayList<>();
        ArrayList<SingleModule> allModules = new ArrayList<>();
        String carveLine = "*************************************";
        ArrayList<String> output = new ArrayList<>();
        FileInputStream in = null;

        try {
            in = new FileInputStream(ninjaFile);
            //PrintWriter out = new PrintWriter(new FileWriter("extractedModules.txt"));
            //BufferedWriter out = new BufferedWriter(new FileWriter("extractedModules.txt"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String str;

            while ((str = bf.readLine()) != null) {
                if (str.contains(singletonNameString) || str.contains(moduleNameString))
                {
                    System.out.println(carveLine);
                    System.out.println(str);
                    output.add(carveLine);
                    output.add(str);
                    SingleModule singleModule = new SingleModule(str);
                    allModules.add(singleModule);

                }
                if (str.contains(variantNameString))
                {
                    System.out.println(str);
                    output.add(str);
                    if (str.equals(variantNameSpecialString))
                    {
                        String strtemp = bf.readLine();
                        if ((strtemp != null) && !(strtemp.startsWith(typeNameSpecialString)))
                        {
                            str = str + " " + strtemp.substring(strtemp.indexOf("#") + 1).trim();
                        }
                    }
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).setModuleVariant(str);
                }else if (str.contains(typeNameString))
                {
                    System.out.println(str);
                    output.add(str);
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).setModuleType(str);
                }else if (str.contains(factoryNameString))
                {
                    System.out.println(str);
                    output.add(str);
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).setModuleFactory(str);
                }else if (str.contains(definedNameString))
                {
                    System.out.println(str);
                    output.add(str);
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).setModuleDefiled(str);
                }
                if (str.contains(ccCompileRule2)) {
                    String[] strs;
                    strs = bf.readLine().split("\\$");
                    str = strs[0].replace("|", "");
                    str = str.replace("$", "");
                    System.out.println("\t\tg.cc.cc " + str.trim());
                    output.add("\t\tg.cc.cc " + str.trim());
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).addModuleSrc("\t\tg.cc.cc " + str.trim());
                } else if (str.contains(ccCompileRule)) {
                    String[] strs = str.split(" \\|");
                    str = strs[0].replace("|", "");
                    str = str.replace("$", "");
                    str = str.replace(":", "");
                    System.out.println("\t\t" + str.trim());
                    output.add("\t\t" + str.trim());
                    if (!allModules.isEmpty())
                        allModules.get(allModules.size()-1).addModuleSrc("\t\t" + str.trim());
                }
                if (str.contains(ccShareLinkRule) || str.contains(ccStaticLinkRule))
                {
                    String tempStr;
                    while ((tempStr=bf.readLine())!=null)
                    {
                        if (tempStr.trim().startsWith("description"))
                            break;
                        if (tempStr.contains("${ldCmd}") || tempStr.contains("${arCmd}"))
                            continue;
                        if (tempStr.endsWith("$"))
                            tempStr = tempStr.replace("$", "");
                        if (tempStr.endsWith(".toc "))
                            tempStr = tempStr.replace(".toc ", "");
                        if (!allModules.isEmpty())
                            allModules.get(allModules.size()-1).addModuleLinkSrc(tempStr.trim());
                    }
                }
            }
            in.close();
            /*Iterator itr = output.iterator();
            while(itr.hasNext())
            {
                out.write(itr.next().toString());
                out.newLine();
            }*/
            /*while (itr.hasNext())
            {
                SingleModule tempModule = (SingleModule) itr.next();
                if (tempModule.getModuleName().contains("Singleton"))
                    continue;
                if (tempModule.getModuleVariant().contains("android_common"))
                    continue;
                out.write(carveLine);
                out.newLine();
                out.write(tempModule.getModuleName());
                out.newLine();
                out.write(tempModule.getModuleVariant());
                out.newLine();
                out.write(tempModule.getModuleType());
                out.newLine();
                out.write(tempModule.getModuleFactory());
                out.newLine();
                out.write(tempModule.getModuleDefiled());
                out.newLine();
                Iterator itr2 = tempModule.getModuleSrcs().iterator();
                while(itr2.hasNext()) {
                    String tempS = itr2.next().toString();
                    out.write(tempS);
                    out.newLine();
                }
            }
            out.flush();
            out.close();
            */
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e)
        {e.printStackTrace();}
        return allModules;
    }
}
