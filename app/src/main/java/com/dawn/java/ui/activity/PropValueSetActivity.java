package com.dawn.java.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dawn.java.R;
import com.dawn.java.ui.MyApplication;
import com.dawn.java.ui.activity.base.DawnActivity;
import com.dawn.java.util.BeansUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropValueSetActivity extends DawnActivity {
    // private AttrHelpBean mAttrHelpBean;
    private String mCodeName;
    private String mAttrName;
    private LinearLayout layout_return;
    private TextView tv_title;
    private TextView tv_confirm;
    private LinearLayout ll_container;
    private TextView tv_attr_name;
    private TextView tv_radio_name;
    private TextView tv_drop_name;

    private EditText edit_attr_value;
    private LinearLayout ll_edit;
    private LinearLayout ll_radio;
    private LinearLayout ll_drop;
    private Spinner sp_drop;
    private RadioGroup rg_radio;
    private List<RadioButton> radioButtonList = new ArrayList<>();
    private Map<Integer, Integer> mapPropValuePosition = new HashMap<>();   //value  -  position
    // private List<PropValueHelpBean> listPropValueBean = new ArrayList<>();
    // private List<AttrHelpBean> listAttrHelpBean = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String[] weight = new String[4];
//    private int checkPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_prop_value_set;
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mCodeName = bundle.getString(MyApplication.BUNDLE_KEY_CODE_NAME);
        mAttrName = bundle.getString(MyApplication.BUNDLE_KEY_ATTR_NAME);


    }

    private void initView() {
        /*if (mAttrHelpBean == null)
            return;*/
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(mCodeName);
        ll_container = findViewById(R.id.ll_container);
        layout_return = findViewById(R.id.layout_return);
        layout_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_edit = findViewById(R.id.ll_edit);
        ll_radio = findViewById(R.id.ll_radio);
        ll_drop = findViewById(R.id.ll_drop);
        tv_attr_name = findViewById(R.id.tv_attr_name);
        tv_radio_name = findViewById(R.id.tv_radio_name);
        tv_drop_name = findViewById(R.id.tv_drop_name);
        edit_attr_value = findViewById(R.id.edit_attr_value);
        rg_radio = findViewById(R.id.rg_radio);
        sp_drop = findViewById(R.id.sp_drop);


        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveValue();
                finish();
            }
        });

//        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                Log.v(MyApplication.TAG, "onCheckedChanged:" + i);
//                checkPos = i;
//            }
//        });


    }


    private void saveValue() {
        String strValue = "";


        Log.v(MyApplication.TAG, "strValue:" + strValue);
        if (strValue.isEmpty()) {
            return;
        } else {
            // test
        }
    }
}
