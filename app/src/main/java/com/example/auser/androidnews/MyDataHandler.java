package com.example.auser.androidnews;

import android.util.Log;
import android.widget.ArrayAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by auser on 2017/10/30.
 */


public class MyDataHandler extends DefaultHandler {
    boolean isTitle ,isItem,islink,isDescription;
    String data;
    StringBuilder sbTitle=new StringBuilder();



    public static ArrayList<String > listNews=new ArrayList<>();
    public static ArrayList<String > listLink=new ArrayList<>();
    public static ArrayList<String > listImg=new ArrayList<>();
    public static ArrayList<String > listDescription=new ArrayList<>();
//    public ArrayAdapter<String> adapter;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("title")) isTitle = true;
        if (qName.equals("item")) isItem = true;
        if (qName.equals("link")) islink = true;
        if (qName.equals("description")) isDescription = true;

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("title")) isTitle = false;
        if (qName.equals("item")) isItem = false;
        if (qName.equals("link")) islink = false;
        if (qName.equals("description")) isDescription = false;

    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle&&isItem)
        {
            data = new String(ch, start, length);
            sbTitle.append(data);
            Log.d("MyTitle", sbTitle.toString());
            listNews.add(sbTitle.toString());  //若用String去接網頁的title,會出現換行的bug
//            listNews.add(data);
//            sbTitle=new StringBuilder();
            sbTitle.setLength(0);
        }
        if (islink&&isItem)
        {
            data = new String(ch, start, length);
            Log.d("MyLink", data);
            listLink.add(data);
        }

        if (isDescription&&isItem)
        {
            data = new String(ch, start, length);
            int intImgFrom=data.indexOf("photo.php?u=");
            String strDescription="";
            String imgSrc="";
            if (intImgFrom>0) {
                int intImgTo=0;
                strDescription = data.substring(intImgFrom+12);
                Log.d("MyDescriptionsrc0=", "qq="+strDescription+"");

                intImgTo=strDescription.indexOf(".jpg");
//                Log.d("MyDescriptionsrc1=", strImg.indexOf("'></p>")+"");
                Log.d("MyDescriptionsrc1=", intImgTo+"");
                Log.d("MyDescriptionsrcIMG=", strDescription.substring(0,intImgTo+4));
                //==================================================================
                imgSrc=strDescription.substring(0,intImgTo+4);
                Log.d("MyDescriptionAAimgSrc=", imgSrc+"");
//=======================================================
                Log.d("MyDescriptionsrc22=", strDescription.indexOf("'></p><p>")+"");
                strDescription=strDescription.substring(strDescription.indexOf("'></p><p>")+9);
//                strImg=strImg.substring(strImg.indexOf("'></p><p>")+10);

                intImgTo=strDescription.indexOf("</p>");
                Log.d("MyDescriptionsrc</p>=", strDescription.indexOf("</p>")+"");

                if (intImgTo>0) strDescription=strDescription.substring(0,strDescription.indexOf("</p>"));

//================================================================
            }else {
                strDescription=data;
//                Log.d("MyDescriptionBB=", data+"");
            }
            Log.d("MyDescriptionAA","~~~~~~~~~~~~~~~=");

            listImg.add(imgSrc);
            listDescription.add(strDescription);
        }

    }
}
