package com.cbb.seeandroid.skin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cbb.seeandroid.R;

/**
 * Created by 坎坎.
 * Date: 2020/6/5
 * Time: 14:06
 * describe:
 */
public class SkinFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_skin, container);
        return inflate;
    }
}
