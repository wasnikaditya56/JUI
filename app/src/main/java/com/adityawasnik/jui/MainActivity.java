package com.adityawasnik.jui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    Button btnAddNew;
    String user, ticket, date, status, remarks, unitId, defectLocation;

    int cnt1 = 0;
    Spinner sp_supplier1;
    String suplier1 = "", device_id = "";
    ListView listView;
    Context context=this;
    View includedLayout;
    DefectsList_DataModel dataModels;



    //--------- To Save Value In SharedPreference -----------
    public static final String PREF_NAME = "PEOPLE_PREFERENCES";
    SharedPreferences myprefs;

    List<String> userIdList = new ArrayList<String>();
    List<String> ticketList = new ArrayList<String>();
    List<String> dateList = new ArrayList<String>();
    List<String> viewStatusList = new ArrayList<String>();
    List<String> devicelist = new ArrayList<String>();


    ArrayList<DefectsList_DataModel> dataModel = new ArrayList<DefectsList_DataModel>();
    //--------------- Dynamic Table Layout -----------------
    TableLayout tableLayout;
    TableRow[] tableRow;
    TextView[] edt_Item, edt_ClosingStock, edt_ClosingCost;

    JSONArray jsonArray;
    JSONArray jsonArray1;
    JSONObject jsonObject, jsonObject1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          listView = findViewById(R.id.listview_addNew);
          includedLayout = findViewById(R.id.include);


        btnAddNew = findViewById(R.id.btn_addNew);
        btnAddNew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                postData();
            }
        });
    }

    // Post Request For JSONObject
    public void postData()
    {
      //  String email = edtEmail.getText().toString().trim();
       // String password = edtPassword.getText().toString().trim();

        final EditText edt_addNew, des;
        Button btn_submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.add_new_dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        edt_addNew = (EditText) dialog.findViewById(R.id.edt_add_new_dialog);
        btn_submit = (Button) dialog.findViewById(R.id.btn_add_new_dialog);

        btn_submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v)
            {
                if (edt_addNew.getText().toString().isEmpty()) {
                    edt_addNew.setError("Please Enter User Name");
                }
                else
                {
                    user = edt_addNew.getText().toString();

                    JSONObject json = new JSONObject();
                    try {
                        //input your API parameters
                        json.put("user", user);

                        System.out.println("User::: "+user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Enter the correct url for your api service site
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.URL_GETDEFECTSLIST, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        jsonArray = response.getJSONArray("data");

                                        for(int i=0; i<jsonArray.length(); i++)
                                        {
                                             jsonObject = jsonArray.getJSONObject(i);

                                            String id = jsonObject.getString("id");
                                            String ticket = jsonObject.getString("ticket");
                                            String date = jsonObject.getString("created_at");
                                            String view_status = jsonObject.getString("view_status");


                                            userIdList.add(id);
                                            ticketList.add(ticket);
                                            dateList.add(date);
                                            viewStatusList.add(view_status);


                                        }

                                        try
                                        {
                                            JSONArray  jsonArray1 = jsonObject.getJSONArray("submissions");
                                            for(int j=0; j<jsonArray1.length(); j++)
                                            {
                                                 jsonObject1 = jsonArray1.getJSONObject(j);

                                              //  ticket = jsonObject1.getString("ticket");//
                                                date = jsonObject1.getString("created_at");
                                                status = jsonObject1.getString("status"); //
                                                remarks = jsonObject1.getString("remarks");//
                                                unitId = jsonObject1.getString("def_id"); //
                                                defectLocation = jsonObject1.getString("defect_location"); //

                                                if(defectLocation.equals("1"))
                                                {
                                                    defectLocation = "LIVING ROOM";
                                                }
                                                else
                                                {
                                                    defectLocation = "HALL";
                                                }



                                            }

                                            System.out.println("date::: "+date);
                                            System.out.println("status::: "+status);
                                            System.out.println("remarks::: "+remarks);
                                            System.out.println("unitId::: "+unitId);
                                            System.out.println("defectLocation::: "+defectLocation);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        for(int i=0;i<jsonArray.length();i++)
                                        {
                                            dataModel.add(new DefectsList_DataModel(ticketList.get(i), dateList.get(i), viewStatusList.get(i)));
                                        }

                                        includedLayout.setVisibility(View.VISIBLE);
                                        DefectsList_Adapter adapter = new DefectsList_Adapter(dataModel, context);
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                        {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                            {
                                                 dataModels = dataModel.get(position);
                                               // Toast.makeText(getApplicationContext()," Date : "+dataModels.getDate()+"\t Total Km : "+ dataModels.getTotalKm()+"\n Opening Km : "+dataModels.getOpKm()+"\t Closing Km : "+dataModels.getCloseKm(), Toast.LENGTH_LONG).show();

                                                System.out.println("Date:::: "+dataModels.getDate());
                                                System.out.println("Action:::: "+dataModels.getAction());
                                                System.out.println("Tickeet:::: "+dataModels.getTicket());
                                                showDeviceNameDialog();
                                            /*    try {
                                                    jsonArray1 = response.getJSONArray("submissions");
                                                    for(int i=0; i<jsonArray1.length(); i++)
                                                    {
                                                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                                                         ticket = jsonObject.getString("ticket");//
                                                         date = jsonObject.getString("created_at");
                                                         status = jsonObject.getString("status"); //
                                                         remarks = jsonObject.getString("remarks");//
                                                         unitId = jsonObject.getString("def_id"); //
                                                         defectLocation = jsonObject.getString("defect_location"); //



                                                    }
                                                    showDeviceNameDialog();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }*/



                                            }
                                        });



                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            VolleyLog.d("Error", "Error: " + error.getMessage());
                            Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonObjectRequest);


                    dialog.cancel();
                }
            }
        });

        ///


    }


    //------Get Voucher Id-------
    public String getDeviceId()
    {
        int len=userIdList.size();
        for(int j=0;j<len;j++)
        {
            if(j==cnt1)
            {
                device_id = userIdList.get(j) ;
                break;
            }
        }
        return device_id;
    }

    public void showDeviceNameDialog()
    {
        //final EditText deviceName, des;
        final TextView txtTicketId, txtUnitId,txtNameId, txtStatusId, txtSubdefLocation, txtSubdefFloor,txtSubdefRemarks;
        Button submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.submitted_defects_dialogbox);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        txtTicketId = (TextView) dialog.findViewById(R.id.txt_ticket_id);
        txtUnitId = (TextView) dialog.findViewById(R.id.txt_unit_id);
        txtNameId = (TextView) dialog.findViewById(R.id.txt_name_id);
        txtStatusId = (TextView) dialog.findViewById(R.id.txt_status_id);
        txtSubdefLocation = (TextView) dialog.findViewById(R.id.txt_subdef_location);
        txtSubdefFloor = (TextView) dialog.findViewById(R.id.txt_subdef_floor);
        txtSubdefRemarks = (TextView) dialog.findViewById(R.id.txt_subdef_remarks);


        txtTicketId.setText(dataModels.getTicket());
        txtNameId.setText("Alvin Wong");
        txtUnitId.setText(unitId);
        txtStatusId.setText(status);


        txtSubdefLocation.setText(defectLocation);
        txtSubdefFloor.setText("FLOOR");
        txtSubdefRemarks.setText(remarks);


    }
}