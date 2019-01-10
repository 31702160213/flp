package com.example.administrator.flp.FragmentView;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.flp.R;
import com.example.administrator.flp.adapter.Contacts_Adapter;
import com.example.administrator.flp.been.ContactsMessage;
import com.example.administrator.flp.utils.okhttpclient_contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Fragment_contacts extends Fragment {
    private View contactsview;
    private List<ContactsMessage> lists;
    private ListView list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO:联系人碎片
        //碎片相当于布局中的控件
        //        return super.onCreateView(inflater, container, savedInstanceState);
        contactsview = inflater.inflate(R.layout.fragment_contacts, container, false);

        lists=new ArrayList<>();
//okhttp链接返回的数据
        okhttpclient_contacts.contacts(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "连网失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String contactsdata =response.body().string();
                Contactsdata(contactsdata);

            }
        });


        initView();//初始化ListView,设置适配器
        return contactsview;
    }

    private void Contactsdata(String contactsdata) {
       // Log.d("AAAAA",contactsdata);

        parseJson(contactsdata);
    }
//解析JSON，保存名称，和用户
    private void parseJson(String contactsdata) {
        try {

            JSONArray jsonArray = new JSONArray(contactsdata);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String name = jsonObject.optString("name");
                String user = jsonObject.optString("user");

                ContactsMessage contactsMessage = new ContactsMessage();
                contactsMessage.setName(name);
                contactsMessage.setUser(user);
                lists.add(contactsMessage);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initView() {

        list = (ListView) contactsview.findViewById(R.id.list_contacts);
        Contacts_Adapter contacts_adapter = new Contacts_Adapter(lists,getActivity());
        list.setAdapter(contacts_adapter);
    }


}
