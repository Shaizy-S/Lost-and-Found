package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Help#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Help extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Help() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Help.
     */
    // TODO: Rename and change types and number of parameters
    public static Help newInstance(String param1, String param2) {
        Help fragment = new Help();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // You can add any additional setup code here if needed
        view.findViewById(R.id.question1).setOnClickListener((View.OnClickListener) this);
        view.findViewById(R.id.question2).setOnClickListener((View.OnClickListener) this);
        view.findViewById(R.id.question3).setOnClickListener((View.OnClickListener) this);
        view.findViewById(R.id.question4).setOnClickListener((View.OnClickListener) this);
        view.findViewById(R.id.question5).setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v) {
        // Toggle visibility of answers when a question is clicked
        int answerId = -1;
        if (v.getId() == R.id.question1) {
            answerId = R.id.answer1;
        } else if (v.getId() == R.id.question2) {
            answerId = R.id.answer2;
        } else if (v.getId() == R.id.question3) {
            answerId = R.id.answer3;
        } else if (v.getId() == R.id.question4) {
            answerId = R.id.answer4;
        } else if (v.getId() == R.id.question5) {
            answerId = R.id.answer5;
        }
        // Add more if-else statements for additional questions

        if (answerId != -1) {
            toggleVisibility(v, answerId);
        }
    }


    private void toggleVisibility(View questionView, int answerId) {
        TextView answerTextView = questionView.getRootView().findViewById(answerId);
        if (answerTextView.getVisibility() == View.VISIBLE) {
            answerTextView.setVisibility(View.GONE);
        } else {
            answerTextView.setVisibility(View.VISIBLE);
        }
    }


}