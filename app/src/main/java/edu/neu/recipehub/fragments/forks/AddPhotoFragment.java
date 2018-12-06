package edu.neu.recipehub.fragments.forks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.GoBackFragment;
import edu.neu.recipehub.fragments.home.HomeFragment;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFragment extends Fragment {
    public static Map<Integer, Uri> URI_MAP = new LinkedHashMap<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Integer> windowIdList;
    private View rootView;
    private LinearLayout parentLayout;
    private Button addBtn;
    private Button removeBtn;
    private Button nextBtn;
    private HomeFragment.OnFragmentInteractionListener mListener;
    private ImageView curImageView;


    public AddPhotoFragment() {
    }


    public static AddPhotoFragment newInstance() {
        AddPhotoFragment fragment = new AddPhotoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        URI_MAP.clear();
        rootView = inflater.inflate(R.layout.fragment_add_photo, container, false);
        parentLayout = rootView.findViewById(R.id.photoParent);
        windowIdList = new ArrayList<>();
        windowIdList.add(R.id.photoUpload0);
        addBtn = rootView.findViewById(R.id.addBtn);
        removeBtn = rootView.findViewById(R.id.removeBtn);
        nextBtn = rootView.findViewById(R.id.next1);
        ImageView uploadWindow = rootView.findViewById(R.id.uploadWindow);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField();
            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete();
            }
        });

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.upperFrameLayout, GoBackFragment.newInstance("Add Photo")).commit();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(IngredientFragment.newInstance());
            }
        });

        setOnWindowClickedListener(uploadWindow);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void onAddField() {
        LayoutInflater inflator = (LayoutInflater) getActivity().
                getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View newUploadWindow = inflator.inflate(R.layout.image_upload, null);
        setOnWindowClickedListener(newUploadWindow.findViewById(R.id.uploadWindow));
        int newId = View.generateViewId();
        newUploadWindow.setId(newId);
        windowIdList.add(newId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        newUploadWindow.setLayoutParams(layoutParams);
        parentLayout.addView(newUploadWindow, parentLayout.getChildCount() - 2);
    }

    private void onDelete() {
        if (windowIdList.size() > 1) {
            Integer id = windowIdList.get(windowIdList.size() - 1);
            View v = rootView.findViewById(id);
            parentLayout.removeView(rootView.findViewById(id));
            windowIdList.remove(windowIdList.size() - 1);
            URI_MAP.remove(id);
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            View parent = (View) curImageView.getParent();
            URI_MAP.put(parent.getId(), imageUri);
            Picasso.get().load(imageUri).into(curImageView);
            nextBtn.setEnabled(true);
        }
    }

    public void setOnWindowClickedListener(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curImageView = (ImageView) v;
                openFileChooser();
            }
        });
    }


}
