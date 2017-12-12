package com.example.auser.androidnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String str, str1, result;
    String stri;
    StringBuilder sb;
    public ArrayAdapter<String> adapter;
    public ListView newListView;
    public ArrayList<String> listNewsLink;
    MyDataHandler dataHandler;
    Myadapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newListView = (ListView) findViewById(R.id.listView);


        dataHandler = new MyDataHandler();

        myadapter = new Myadapter(this);

//        adapter = new ArrayAdapter<String>(MainActivity.this
//                , android.R.layout.simple_list_item_1
//                , dataHandler.listNews);
//        newListView.setAdapter(adapter);
        newListView.setAdapter(myadapter);
        //加入超連結
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //將畫面導向外部網頁
//                Uri uri = Uri.parse(dataHandler.listLink.get(position));
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
                //將畫面指向另一個java頁面
                Intent it = new Intent(MainActivity.this, DetailActivity.class);
                it.putExtra("url", dataHandler.listLink.get(position));
                startActivity(it);
            }
        });


        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
//                    URL url = new URL("https://udn.com/rssfeed/news/2/6649?ch=news");
                    URL url = new URL("https://udn.com/rssfeed/news/2/6638?ch=news");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    sb = new StringBuilder();

                    while ((str = br.readLine()) != null) {
                        sb.append(str);
                    }
                    result = sb.toString();

                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(dataHandler);  //asscess from inner class,should be final
                    xr.parse(new InputSource(new StringReader(result)));

                    br.close();
                    isr.close();
                    is.close();


//                    System.out.println(MyDataHandler.listNews);
//                    adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,MyDataHandler.listNews);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void clickTest(View v) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {  //new Runnable透過runOnUiThread方法丟回主執行緒執行
                        @Override
                        public void run() {
                            myadapter.notifyDataSetChanged();
//                            adapter.notifyDataSetChanged();
//                            newListView.setAdapter(adapter);

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    //=================ADAPTER======================================
    class Myadapter extends BaseAdapter {
        private LayoutInflater myInflater;
        Context mContext;

        public Myadapter(Context context) {
            myInflater = LayoutInflater.from(context);
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return dataHandler.listNews.size();
        }

        @Override
        public Object getItem(int i) {
            return dataHandler.listNews.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        AlertDialog dialog; //讓自定Layout可有關閉功能
        View root;
        EditText et_message;
        Button confirmMessage, cancelMessage;


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
//            mDatabase = FirebaseDatabase.getInstance().getReference(FINAL_USER_ID);

            final int position = i;

//            Log.d("fire==i=", i + "");
            convertView = myInflater.inflate(R.layout.listview_myitem1, null);
            TextView txTentNote = (TextView) convertView.findViewById(R.id.text1);
            TextView txHouseAddr = (TextView) convertView.findViewById(R.id.text2);


            txHouseAddr.setText(dataHandler.listNews.get(i));
            txTentNote.setText(dataHandler.listDescription.get(i));

            ImageView img = (ImageView)convertView.findViewById(R.id.imageView);

            if (dataHandler.listImg.get(i).length()>0) {
                Log.d("img==", dataHandler.listImg.get(i) + "");
                Picasso.with(mContext)
                        .load(dataHandler.listImg.get(i))
//                    .load("https://uc.udn.com.tw/photo/2017/12/11/realtime/4333065.jpg…")
//                .placeholder(R.drawable.placeholder) //optional
                        .resize(853, 500)         //optional
                        .centerCrop()                        //optional
                        .into(img);
            }else img.setVisibility(View.INVISIBLE);
            Log.d("fire==", "" + i);

//
//            btnDel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("fire===position", position + "");
//                    Log.d("fire===alrHouseKey", alrHouseKey.get(position));
//                    LayoutInflater inflater = (LayoutInflater) mContext
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    root = inflater.inflate(R.layout.dialog_delete_layout, null);//找出根源樹,
//                    TextView txDeleteDescription = root.findViewById(R.id.txDeleteDescription);
//                    Button confirmDelete = root.findViewById(R.id.btn_Deleteconfirm);
//                    Button cancelDelete = root.findViewById(R.id.btn_Deletecancel);
//                    txDeleteDescription.setText("請問是否要將資料刪除");
//                    cancelDelete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                    confirmDelete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            DatabaseReference mDatabase;
//                            mDatabase = FirebaseDatabase.getInstance().getReference("House");
//                            mDatabase = mDatabase.child(alrHouseKey.get(position));
//                            mDatabase.removeValue();
////                            myadapter.notifyDataSetChanged();
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog.Builder abc = new AlertDialog.Builder(mContext);
//                    abc.setView(root);
//                    dialog = abc.show();
//
//                }
//            });


            return convertView;
        }
    }
}
