package com.aosp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class MainForm {
    public JPanel JPanelMy;
    private JTextField textField1;
    private JButton selectNinjaFile;
    private JList listModule;
    private JList listVariant;
    private JList listSrc;
    private JList listLinkSrc;

    ArrayList<String> allModules = new ArrayList<>();

    public MainForm(ArrayList<SingleModule> moduleList)
    {
        initComponent(moduleList);
        listModule.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedModule = (String) listModule.getSelectedValue();
                if (selectedModule == null)
                    return;
                selectedModule = "# Module:  " + selectedModule.substring(selectedModule.indexOf(":") + 1).trim();
                ArrayList<String> moduleVariant = new ArrayList<>();
                DefaultListModel dlm = new DefaultListModel();
                Iterator itr = moduleList.iterator();
                while (itr.hasNext())
                {
                    SingleModule singleModule = (SingleModule)itr.next();
                    if (singleModule.getModuleName().equals(selectedModule))
                    {
                        moduleVariant.add(singleModule.getModuleVariant());
                        dlm.addElement(singleModule.getModuleVariant());
                    }
                }
                listVariant.setModel(dlm);
            }
        });

        listVariant.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedVariant = (String) listVariant.getSelectedValue();
                String selectedModule = (String) listModule.getSelectedValue();
                selectedModule = "# Module:  " + selectedModule.substring(selectedModule.indexOf(":") + 1).trim();
                DefaultListModel dlm = new DefaultListModel();
                DefaultListModel dlm2 = new DefaultListModel();
                Iterator itr = moduleList.iterator();
                while (itr.hasNext())
                {
                    SingleModule singleModule = (SingleModule)itr.next();
                    if (singleModule.getModuleName().equals(selectedModule) && singleModule.getModuleVariant().equals(selectedVariant))
                    {
                        ArrayList<String> srcs = singleModule.getModuleSrcs();
                        ArrayList<String> linkSrcs = singleModule.getModuleLinkSrcs();
                        for (String src:srcs)
                            dlm.addElement(src);
                        for (String linkSrc:linkSrcs)
                            dlm2.addElement(linkSrc);
                    }
                }
                listSrc.setModel(dlm);
                listLinkSrc.setModel(dlm2);
            }
        });
        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println("insert  " + textField1.getText());
                String wantedModule = textField1.getText().trim();
                DefaultListModel dlm = new DefaultListModel();

                Iterator itr = allModules.iterator();
                while (itr.hasNext())
                {
                    String module = (String)itr.next();
                    if (module.contains(wantedModule))
                        dlm.addElement(module);
                }

                listModule.setModel(dlm);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("remove  " + textField1.getText());
                String wantedModule = textField1.getText().trim();
                if (wantedModule != "") {
                    DefaultListModel dlm = new DefaultListModel();
                    Iterator itr = allModules.iterator();
                    while (itr.hasNext()) {
                        String module = (String) itr.next();
                        if (module.contains(wantedModule))
                            dlm.addElement(module);
                    }
                    listModule.setModel(dlm);
                }else
                {
                    DefaultListModel dlm = new DefaultListModel();
                    Iterator itr = allModules.iterator();
                    while (itr.hasNext()) {
                        String module = (String) itr.next();
                        dlm.addElement(module);
                    }
                    listModule.setModel(dlm);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("update  " + textField1.getText());
            }
        });
    }
    private void initComponent(ArrayList<SingleModule> moduleList)
    {
        JPanelMy.setPreferredSize(new Dimension(600,500));
        DefaultListModel dlm = new DefaultListModel();
        ArrayList<String> allModulesTemp = new ArrayList<>();
        Iterator itr = moduleList.iterator();
        while (itr.hasNext())
        {
            SingleModule singleModule = (SingleModule)itr.next();
            String moduleName = singleModule.getModuleName();
            String moduleDefined = singleModule.getModuleDefiled();
            if (moduleDefined == null)
                continue;
            String moduleScopes[] = moduleDefined.split("/");
            String moduleScope = moduleScopes[0];
            moduleScope = moduleScope.substring(moduleScope.indexOf(":")+1).trim();
            moduleName = moduleName.substring(moduleName.indexOf(":")+1).trim();
            allModulesTemp.add(moduleScope + ": " + moduleName);
        }
        HashSet<String> hashSet = new HashSet<>(allModulesTemp);
        allModulesTemp.clear();
        for (String ts : hashSet)
        {
            allModulesTemp.add(ts);
        }

        Collections.sort(allModulesTemp);
        this.allModules = allModulesTemp;
        itr = allModulesTemp.iterator();

        while (itr.hasNext())
        {
            dlm.addElement(itr.next());
        }
        listModule.setModel(dlm);
    }
}
