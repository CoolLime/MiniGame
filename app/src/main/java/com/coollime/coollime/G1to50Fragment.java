package com.coollime.coollime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class G1to50Fragment extends Fragment {
   /* private RankDAO m_dao = null;
    private ListView m_lv = null;
    private BaseAdapter m_ba = null;
    private ArrayList<HashMap<String,String>> _DataSource = null;
*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.g1to50, container, false);
        Button select;
        select = (Button) rootView.findViewById(R.id.start);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oneto50 = new Intent(getActivity(), G1to50Activity.class);
                startActivity(oneto50);
            }
        });
/*
        try{
            m_dao = RankDAO.GetInstance(getActivity());
        }catch(Exception e){
            e.printStackTrace();
        }

        Refresh();

        m_ba = new SimpleAdapter( getActivity()
                ,_DataSource
                ,android.R.layout.simple_list_item_2
                ,new String[]{"Name","Score"}
                ,new int[]{android.R.id.text1, android.R.id.text2}
        );


        m_lv = (ListView)rootView.findViewById(R.id.g1to50Rank);
        m_lv.setAdapter(m_ba);
*/
        return rootView;
    }

    public class select extends AppCompatActivity {
        public static final int REQUEST_CODE_ANOTHER = 1001;

        public void onstartClick(View v) {
            Intent intent = new Intent(getApplicationContext(), G1to50Activity.class);
            startActivityForResult(intent, REQUEST_CODE_ANOTHER);

        }
        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);

        }

    }

    /*public void Refresh(){
        _DataSource = m_dao.GetRankData("g1to50");
    }*/
}
