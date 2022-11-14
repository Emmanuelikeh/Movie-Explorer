package com.example.movieexplorer;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieexplorer.databinding.FragmentDialogfragmentBinding;


public class dialogfragment extends DialogFragment {
    FragmentDialogfragmentBinding fragmentDialogfragmentBinding;


    public dialogfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//
        fragmentDialogfragmentBinding = FragmentDialogfragmentBinding.inflate(inflater,container,false);
        fragmentDialogfragmentBinding.btnCancel.setOnClickListener(view -> {
            dismiss();
        });

        fragmentDialogfragmentBinding.btnUpdate.setOnClickListener(view -> {
            dialogListener listener = (dialogListener) getParentFragment();
            if (listener != null) {
                listener.onFinishChangeUsername(fragmentDialogfragmentBinding.etUpdateUsername.getText().toString());
            }
            // Closes the dialog fragment
            dismiss();
        });
        return fragmentDialogfragmentBinding.getRoot();
    }
}