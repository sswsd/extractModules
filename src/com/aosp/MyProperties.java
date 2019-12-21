package com.aosp;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by wangso on 11/3/2017.
 */
class PropertyNote
{
    private String key;
    private String property;

    public PropertyNote()
    {
        key = "";
        property = "";
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    public void setProperty(String property)
    {
        this.property = property;
    }
    public String getKey()
    {
        return this.key;
    }
    public String getProperty()
    {
        return this.property;
    }
}

public class MyProperties
{
    private Properties prop;
    //private String propertyLocation;
    private String propertyPath;
    private ArrayList<PropertyNote> propertyNotes;

    public MyProperties()
    {
        prop = new Properties();
        this.propertyPath = null;
        this.propertyNotes = null;
        File targetFile = null;

        try
        {
            System.out.println(System.getProperty("user.dir"));
            //this.propertyPath = URLDecoder.decode(this.getClass().getResource("").getFile(), "UTF-8") + "/config";
            this.propertyPath = URLDecoder.decode(System.getProperty("user.dir"), "UTF-8") + "/config";
            System.out.println(this.propertyPath);
            File targetFolder = new File(this.propertyPath);
            if (!targetFolder.exists())
                targetFolder.mkdirs();
            targetFile = new File(targetFolder, "config.properties");
            targetFile.createNewFile();
        } catch (UnsupportedEncodingException e)
        {
            System.out.println(this.propertyPath);

            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println(this.propertyPath);
            e.printStackTrace();
        }
        this.propertyPath = targetFile.getAbsolutePath();
        System.out.println(this.propertyPath);

    }

    public MyProperties(String propertyLocation)
    {
        prop = new Properties();
        this.propertyPath = null;
        this.propertyNotes = new ArrayList<PropertyNote>();
        //URL url = this.getClass().getResource(propertyLocation);
        String url = System.getProperty("user.dir") + File.separator +propertyLocation;

        try
        {
            if (url != null)
                this.propertyPath = URLDecoder.decode(url, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        this.abstractPropertyNotes();
    }
    private void abstractPropertyNotes()
    {
        try
        {
            InputStream in = new BufferedInputStream(new FileInputStream(this.propertyPath));
            prop.load(in);
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext())
            {
                PropertyNote pn = new PropertyNote();
                String key = it.next();
                String property = prop.getProperty(key);
                pn.setKey(key);
                pn.setProperty(property);
                this.propertyNotes.add(pn);
            }
            in.close();
        }catch (Exception e)
        {
            System.out.println(this.propertyPath);
            e.printStackTrace();
        }

    }
    public void updateProperties (ArrayList<PropertyNote> propertyNote)
    {
        if (propertyNote.size() == 0)
        {
            System.out.println("Property is not available!");
            return;
        }


        try
        {
            FileOutputStream oFile = new FileOutputStream(new File(this.propertyPath));
            //prop.setProperty(propertyNote.getKey(), propertyNote.getPropertyNote());
            Iterator<PropertyNote> it = propertyNote.iterator();
            while (it.hasNext())
            {
                PropertyNote pn = it.next();
                prop.setProperty(pn.getKey(), pn.getProperty());
            }
            prop.store(oFile, "");
            oFile.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<PropertyNote> getProperties()
    {
        return this.propertyNotes;
    }
    public PropertyNote getPropertyNote(String propertyKey)
    {
        PropertyNote pn = new PropertyNote();
        Iterator<PropertyNote> it = this.propertyNotes.iterator();
        while (it.hasNext())
        {
            PropertyNote pn2 = it.next();
            if (pn2.getKey().equals(propertyKey))
                pn = pn2;
        }
        return pn;
    }
    public String getProperty(String propertyKey)
    {
        return getPropertyNote(propertyKey).getProperty();
    }
}
