package com.example.exoplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {

    public static final String TAG = "BottomDialogFragment";
    public View[] isControls;


    private Button button_edit;
    private Button button_setting;
    private SwitchMaterial button_progress_placeholder;
    private SwitchMaterial button_rewind;
    private SwitchMaterial button_fastforward;
    private SwitchMaterial button_subtitles;
    private SwitchMaterial button_forward;



    public static CustomBottomSheetDialogFragment newInstance(View[] isControls){
        return new CustomBottomSheetDialogFragment(isControls);
    }

    public CustomBottomSheetDialogFragment(View[] isControls) {
        this.isControls = isControls;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_edit = view.findViewById(R.id.edit_button);
        button_setting = view.findViewById(R.id.button_settings);
        button_progress_placeholder = view.findViewById(R.id.switch_material_progress_placeholder);
        button_rewind = view.findViewById(R.id.switch_material_rewind);
        button_fastforward = view.findViewById(R.id.switch_material_fastforward);
        button_subtitles = view.findViewById(R.id.switch_material_subtitles);
        button_forward = view.findViewById(R.id.switch_material_forward);

        button_progress_placeholder.setChecked(true);
        if(isControls[1].getVisibility() == View.VISIBLE) button_rewind.setChecked(true);
        if(isControls[2].getVisibility() == View.VISIBLE) button_fastforward.setChecked(true);
        if(isControls[3].getVisibility() == View.VISIBLE) button_subtitles.setChecked(true);
        if(isControls[4].getVisibility() == View.VISIBLE) button_forward.setChecked(true);

        button_edit.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        button_progress_placeholder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    button_progress_placeholder.setChecked(true);
                    Toast.makeText(buttonView.getContext(), "Not implemented",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_rewind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) isControls[1].setVisibility(View.VISIBLE);
                else isControls[1].setVisibility(View.INVISIBLE);
            }
        });
        button_fastforward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) isControls[2].setVisibility(View.VISIBLE);
                else isControls[2].setVisibility(View.INVISIBLE);
            }
        });
        button_subtitles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) isControls[3].setVisibility(View.VISIBLE);
                else isControls[3].setVisibility(View.INVISIBLE);
            }
        });
        button_forward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) isControls[4].setVisibility(View.VISIBLE);
                else isControls[4].setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialogd = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialogd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetBehavior bottomSheet = BottomSheetBehavior
                        .from(dialogd.findViewById(com.google.android.material.R.id.design_bottom_sheet));
                setupBehavior(bottomSheet);
            }
        });
        Log.d("DIALOG", "SHOW");
        return dialogd;
    }

    private void setupBehavior(BottomSheetBehavior bottomSheet) {
        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheet.setHideable(true);
        bottomSheet.setSkipCollapsed(true);
    }

    /**
     * Не используется, пригодится когда нибудь
     */
    public void Back(){
        getChildFragmentManager().popBackStack();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edit_button) {
            Toast.makeText(v.getContext(), "Not implemented",
                    Toast.LENGTH_SHORT).show();
            /*PlayerActivity.Edit(isControls[1].getRootView());*/
            dismiss();
            Log.d("EDIT", "Click");
        }
        if (id == R.id.button_settings) {
            Toast.makeText(v.getContext(), "Not implemented",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
