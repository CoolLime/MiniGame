package com.coollime.coollime;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class cardFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card, container, false);
        Button select;
        select = (Button) rootView.findViewById(R.id.start);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent card = new Intent(getActivity(), CardActivity.class);
                startActivity(card);
            }
        });


        return rootView;
    }


    public class select extends AppCompatActivity {
        public static final int REQUEST_CODE_ANOTHER = 1001;

        public void onButton2Click(View v) {
            Intent intent = new Intent(getApplicationContext(), CardActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);

        }
    }
}