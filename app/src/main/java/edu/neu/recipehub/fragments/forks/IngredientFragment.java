package edu.neu.recipehub.fragments.forks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.home.HomeFragment;

public class IngredientFragment extends Fragment {
    public static Map<String, String> INGREDIENT_MAP = new TreeMap<>();
    private List<Integer> idList;
    private HomeFragment.OnFragmentInteractionListener mListener;
    private View rootView;
    private Button nextBtn;
    private LinearLayout parentLayout;
    private Button addBtn;
    private Button removeBtn;


    public IngredientFragment() {
        // Required empty public constructor
    }


    public static IngredientFragment newInstance() {
        IngredientFragment fragment = new IngredientFragment();
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
        INGREDIENT_MAP.clear();
        idList = new ArrayList<>();
        idList.add(R.id.ingredientUpload0);
        rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        nextBtn = rootView.findViewById(R.id.next3);
        addBtn = rootView.findViewById(R.id.ingAddBtn);
        removeBtn = rootView.findViewById(R.id.ingRemoveBtn);
        parentLayout = rootView.findViewById(R.id.ingredientParent);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer id : idList) {
                    if (checkValidHelper(id)) {
                        View parent = parentLayout.findViewById(id);
                        EditText e1 = parent.findViewById(R.id.ingredientBox);
                        EditText e2 = parent.findViewById(R.id.amountBox);
                        String ing = e1.getText().toString();
                        String amount = e2.getText().toString();
                        INGREDIENT_MAP.put(ing, amount);
                    }
                }
                mListener.changeFragmentInHomeFragment(InstructionFragment.newInstance());
            }
        });
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

        setOnTextChangeListener(rootView.findViewById(R.id.ingredientUpload0));
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        View newUploadWindow = inflator.inflate(R.layout.ingredient_upload, null);
        setOnTextChangeListener(newUploadWindow);
        int newId = View.generateViewId();
        newUploadWindow.setId(newId);
        idList.add(newId);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        newUploadWindow.setLayoutParams(layoutParams);
        parentLayout.addView(newUploadWindow, parentLayout.getChildCount() - 2);
    }

    private void onDelete() {
        if (idList.size() > 1) {
            Integer id = idList.get(idList.size() - 1);
            View v = rootView.findViewById(id);
            parentLayout.removeView(rootView.findViewById(id));
            idList.remove(idList.size() - 1);
            checkValid();
        }
    }

    private void setOnTextChangeListenerHelper(View v) {
        final EditText editText = (EditText) v;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValid();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setOnTextChangeListener(View parent) {
        EditText e1 = parent.findViewById(R.id.ingredientBox);
        EditText e2 = parent.findViewById(R.id.amountBox);
        setOnTextChangeListenerHelper(e1);
        setOnTextChangeListenerHelper(e2);
    }

    private boolean checkValidHelper(Integer id) {
        View parent = parentLayout.findViewById(id);
        EditText e1 = parent.findViewById(R.id.ingredientBox);
        EditText e2 = parent.findViewById(R.id.amountBox);
        String ing = e1.getText().toString();
        String amount = e2.getText().toString();
        if (!ing.isEmpty() && !amount.isEmpty()) return true;
        return false;
    }

    private void checkValid() {
        boolean hasValid = false;
        for (Integer id : idList) {
            if (checkValidHelper(id)) {
                hasValid = true;
                break;
            }
        }
        if (hasValid) nextBtn.setEnabled(true);
        else nextBtn.setEnabled(false);
    }


}
