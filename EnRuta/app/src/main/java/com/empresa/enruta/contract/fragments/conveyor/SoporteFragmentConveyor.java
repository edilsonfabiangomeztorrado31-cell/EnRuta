package com.empresa.enruta.contract.fragments.conveyor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.empresa.enruta.R;

public class SoporteFragmentConveyor extends Fragment {

    @Nullable
    @Override

    public View onCreateView (@Nullable LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstatnceState){
        return inflater.inflate(R.layout.fragment_soporte_conveyor, container, false);

    }
}
