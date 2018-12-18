package com.nibrasco.freshksa;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nibrasco.freshksa.Model.Cart;
import com.nibrasco.freshksa.Model.Session;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheepFragment extends Fragment {

    private Spinner spSlicing, spWeight, spPackaging;
    private TextView txtTotal;
    private EditText edtQuantity;
    TextInputEditText edtNotes;
    private RadioGroup rdGrpIntestine;
    Cart.Item currentItem;
    public SheepFragment() {
        currentItem = Session.getInstance().Item();
        currentItem.setWeight(0);
        currentItem.setIntestine(false);
        currentItem.setSlicing(Cart.eSlicing.Fridge.Value());
        currentItem.setPackaging(Cart.ePackaging.None.Value());
        currentItem.setTotal(Session.getInstance().Item().getDefaultPrice());
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_naemiorder, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final View v = getView();
        assert v != null;
        LoadContent(v);
    }

    private void LinkControls(View v)
    {
        spSlicing = (Spinner)v.findViewById(R.id.spSlicing);
        spWeight = (Spinner)v.findViewById(R.id.spWeight);
        spPackaging = (Spinner)v.findViewById(R.id.spPackaging);
        edtQuantity = (EditText)v.findViewById(R.id.edtQuantity);
        rdGrpIntestine = (RadioGroup)v.findViewById(R.id.rdGrpInt);
        edtNotes = (TextInputEditText)v.findViewById(R.id.edtSNotes);
        txtTotal = (TextView)v.findViewById(R.id.txtTotalItem);
    }
    private void LinkListeners()
    {
        rdGrpIntestine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rdInt_Yes:
                        currentItem.setIntestine(true);
                        break;
                    case R.id.rdInt_No:
                        currentItem.setIntestine(false);
                        break;
                }
                Session.getInstance().Item(currentItem);
            }
        });
        spPackaging.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spPackaging.getSelectedItemPosition()) {
                    Session.getInstance().Item().setPackaging((int)(parent.getItemAtPosition(position)));
                }
                //No specific pricing
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != parent.getSelectedItemPosition()) {
                    Session.getInstance().Item().setWeight((int)(parent.getItemAtPosition(position)));
                    txtTotal.setText(Float.toString(Session.getInstance().Item().getTotal()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSlicing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != spSlicing.getSelectedItemPosition()) {
                    Session.getInstance().Item().setSlicing((int)parent.getItemAtPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Session.getInstance().Item().setNotes(s.toString());
            }
        });
    }
    private void LoadContent(View v)
    {
        LinkControls(v);

        ArrayList<String> list = Cart.Lists.GetWeightNames(Session.getInstance().Item().getCategory());
        ArrayAdapter adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(adapter);
        spWeight.setSelection(0);

        list = Cart.Lists.GetSlicingNames();
        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSlicing.setAdapter(adapter);
        spSlicing.setSelection(0);

        list = Cart.Lists.GetPackagingNames();
        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPackaging.setAdapter(adapter);
        spPackaging.setSelection(0);
        txtTotal.setText(Float.toString(Session.getInstance().Item().getTotal()));

        LinkListeners();
    }
}
